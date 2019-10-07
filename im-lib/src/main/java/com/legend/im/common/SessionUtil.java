package com.legend.im.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * @author Legend
 * @data by on 19-2-13.
 * @description
 */
public class SessionUtil {

    private static final AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
    private static final Map<Long, Channel> idToChannelMap = new ConcurrentHashMap<>();
    private static final Map<Channel, Long> channelToIdMap = new ConcurrentHashMap<>();

//    private static final Map<Long, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>();
//    private static final Map<ChannelGroup, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>();

    public static void bindChannel(Session session, Channel channel) {
        idToChannelMap.put(session.getId(), channel);
        channelToIdMap.put(channel, session.getId());
        channel.attr(SESSION).set(session);
    }

    public static void unBindChannel(Channel channel) {
        if (isOnline(channel)) {
            Long userId = getUserId(channel);
            idToChannelMap.remove(userId);
            channelToIdMap.remove(channel);
            channel.attr(SESSION).set(null);
            System.out.println(userId + " 退出登录!");
        }
    }

    public static boolean isOnline(Channel channel) {
        if (channel == null) {
            return false;
        }
        return getSession(channel) != null;
    }

    public static Long getUserId(Channel channel) {
        return channelToIdMap.get(channel);
    }

    public static Session getSession(Channel channel) {
        return channel.attr(SESSION).get();
    }

    public static Channel getChannel(Long userId) {
        return idToChannelMap.get(userId);
    }
}
