package com.legend.im.client;

import com.legend.im.bean.MsgModel;
import com.legend.im.client.action.MsgActionManager;
import com.legend.im.client.handler.GroupMsgResponseHandler;
import com.legend.im.client.handler.HeartBeatTimeHandler;
import com.legend.im.client.handler.UserMsgResponseHandler;
import com.legend.im.codec.PacketDecoder;
import com.legend.im.codec.PacketEncoder;
import com.legend.im.codec.Spliter;
import com.legend.im.common.IMConfig;
import com.legend.im.common.SessionUtil;
import com.legend.im.common.handler.IMIdleStateHandler;
import com.legend.im.common.listener.ClientStateListener;
import com.legend.im.common.listener.MsgListener;
import com.legend.im.protoctol.command.Command;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Legend
 * @data by on 19-9-9.
 * @description
 */
public class IMClient implements IClient {

    private MsgActionManager commandManager = MsgActionManager.getInstance();
    private Bootstrap bootstrap;
    private NioEventLoopGroup group;
    private Channel channel;
    private ExecutorService workerService;
    // 是否主动关闭
    private AtomicBoolean isDestroyed = new AtomicBoolean(true);
    private AtomicBoolean isClosed = new AtomicBoolean(true);
    private AtomicBoolean isConnecting = new AtomicBoolean(false);
    private IMConfig imConfig;


    /**
     * 消息监听器
     */
    private MsgListener msgListener;
    /**
     * 客户端状态监听器
     */
    private ClientStateListener stateListener;

    public static IMClient getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static IMClient INSTANCE = new IMClient();
    }

    public static void main(String[] args) {
        IMClient client = new IMClient();
        client.startClient();
    }

    public void setup(IMConfig config) {
        this.imConfig = config;
    }

    public void startClient() {
        // 先把之前的关闭
        if (imConfig == null) return;
        destroy();
        init();
        reconnect();
    }

    private void init() {
        this.bootstrap = new Bootstrap();
        this.group = new NioEventLoopGroup();
        this.workerService = new ThreadPoolExecutor(8, 200, 3, TimeUnit.MILLISECONDS
                , new LinkedBlockingQueue<>(1024), new NameableThreadFactory("worker-")
                , new ThreadPoolExecutor.AbortPolicy());
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new IMIdleStateHandler())
                                .addLast(new Spliter())
                                .addLast(new PacketDecoder())
                                .addLast(new UserMsgResponseHandler())
                                .addLast(new GroupMsgResponseHandler())
                                .addLast(new PacketEncoder())
                                .addLast(new HeartBeatTimeHandler());
                    }
                });
        isDestroyed.set(false);
    }

    private void connect(String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功，启动控制台线程……");
                this.channel = ((ChannelFuture)future).channel();
                isClosed.set(false);
                isConnecting.set(false);
                stateListener.connected();
            } else if (retry == 0) {
                System.err.println("连接失败, 已经到达最大重试次数!");
                isConnecting.set(false);
            } else {
                int order = (imConfig.getMaxRetry() - retry) + 1;
                int delay = 1 << order;
                System.err.println("客户端连接失败, 第"+order+"次重试!");
                bootstrap.config().group().schedule(() ->
                        connect(host, port, retry-1), delay, TimeUnit.SECONDS);
            }
        });
    }

    public void setMsgListener(MsgListener msgListener) {
        this.msgListener = msgListener;
    }

    public void setStateListener(ClientStateListener stateListener) {
        this.stateListener = stateListener;
    }

    public IMConfig getImConfig() {
        return imConfig;
    }

    public MsgListener getMsgListener() {
        return msgListener;
    }

    public ClientStateListener getStateListener() {
        return stateListener;
    }

    class MsgActionHandler implements Runnable {

        private MsgModel model;

        public MsgActionHandler(MsgModel model) {
            this.model = model;
        }

        @Override
        public void run() {
            if (!SessionUtil.isOnline(channel)) {
                if (model.getCommand() == Command.LOGIN_REQUEST) {
                    commandManager.exec(model, channel);
                } else {
                    System.out.println("请先通过登录验证");
                    stateListener.unAuth();
                }
            } else {
                commandManager.exec(model, channel);
            }
        }
    }

    @Override
    public boolean isConnecting() {
        return isConnecting.get();
    }

    @Override
    public void reconnect() {
        if (!isDestroyed() && !isConnecting.getAndSet(true)) {
            connect(imConfig.getHost(), imConfig.getPort(), imConfig.getMaxRetry());
        }
    }

    @Override
    public void destroy() {
        if (workerService != null && !workerService.isShutdown()) {
            workerService.shutdown();
        }
        if (group != null && !group.isShutdown()) {
            group.shutdownGracefully();
        }
        isDestroyed.set(true);
        isClosed.set(true);
        isConnecting.set(false);
        if (channel != null) {
            SessionUtil.unBindChannel(channel);
            channel.close();
            channel = null;
        }
        stateListener.closed();
    }

    public void setClosed(boolean val) {
        this.isClosed.set(val);
    }

    @Override
    public boolean isClosed() {
        return isClosed.get();
    }

    @Override
    public boolean isDestroyed() {
        return isDestroyed.get();
    }

    @Override
    public void sendMsg(MsgModel model) {
        // 提交一条消息到工作线程池
        if (!isDestroyed()) {
            workerService.execute(new MsgActionHandler(model));
            if (isClosed()) {
                // 没被销毁 但是被关闭 尝试重连
                reconnect();
            }
        } else {
            // 异常状态下直接改变消息状态
            if (msgListener != null) {
                if (model.getCommand() == Command.USER_MSG_REQUEST) {
                    msgListener.sendUserMsgFailure(model.buildMsg());
                } else if (model.getCommand() == Command.GROUP_MSG_REQUEST) {
                    msgListener.sendGroupMsgFailure(model.buildMsg());
                }
            }
            // 客户端已经销毁 直接重新启动
            startClient();
        }
    }

    @Override
    public int getMaxConnectionRetry() {
        return imConfig.getMaxRetry();
    }

    @Override
    public int getResendCount() {
        return 0;
    }

    @Override
    public int getMaxIdleTime() {
        return imConfig.getIdleTime();
    }

    @Override
    public int getHeartbeatInterval() {
        return imConfig.getHeartbeatInterval();
    }
}
