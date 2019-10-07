package com.legend.im.protoctol;

import com.legend.im.protoctol.request.GroupMsgRequestPacket;
import com.legend.im.protoctol.request.HeartBeatRequestPacket;
import com.legend.im.protoctol.request.LoginRequestPacket;
import com.legend.im.protoctol.request.MsgAckRequestPacket;
import com.legend.im.protoctol.request.UserMsgRequestPacket;
import com.legend.im.protoctol.response.GroupMsgResponsePacket;
import com.legend.im.protoctol.response.HeartBeatResponsePacket;
import com.legend.im.protoctol.response.UserMsgResponsePacket;
import com.legend.im.serialize.Serializer;
import com.legend.im.serialize.impl.JSONSerializer;

import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;

import static com.legend.im.protoctol.command.Command.GROUP_MSG_REQUEST;
import static com.legend.im.protoctol.command.Command.GROUP_MSG_RESPONSE;
import static com.legend.im.protoctol.command.Command.HEARTBEAT_REQUEST;
import static com.legend.im.protoctol.command.Command.HEARTBEAT_RESPONSE;
import static com.legend.im.protoctol.command.Command.LOGIN_REQUEST;
import static com.legend.im.protoctol.command.Command.MSG_ACK_REQUEST;
import static com.legend.im.protoctol.command.Command.USER_MSG_REQUEST;
import static com.legend.im.protoctol.command.Command.USER_MSG_RESPONSE;

/**
 * @author Legend
 * @data by on 19-9-8.
 * @description
 */
public class PacketCodeC {

    public static final int MAGIC_NUMBER = 0x12345678;
    public static final PacketCodeC INSTANCE = new PacketCodeC();

    private final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private final Map<Byte, Serializer>  serializerMap;

    private PacketCodeC() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(USER_MSG_REQUEST, UserMsgRequestPacket.class);
        packetTypeMap.put(USER_MSG_RESPONSE, UserMsgResponsePacket.class);
        packetTypeMap.put(HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
        packetTypeMap.put(HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);
        packetTypeMap.put(GROUP_MSG_REQUEST, GroupMsgRequestPacket.class);
        packetTypeMap.put(GROUP_MSG_RESPONSE, GroupMsgResponsePacket.class);
        packetTypeMap.put(MSG_ACK_REQUEST, MsgAckRequestPacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    public void encode(ByteBuf byteBuf, Packet packet) {
        // 1.创建 ByteBuf对象
        // 2.序列化 java对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 3.实际编码过程
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    public Packet decode(ByteBuf byteBuf) {
        // 跳过Magic number
        byteBuf.skipBytes(4);
        // 跳过版本号
        byteBuf.skipBytes(1);
        // 序列化算法
        byte serializeAlgorithm = byteBuf.readByte();
        // 指令
        byte command = byteBuf.readByte();
        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        // 读取数据
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }
        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }
}
