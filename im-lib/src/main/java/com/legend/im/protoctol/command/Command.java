package com.legend.im.protoctol.command;

/**
 * @author Legend
 * @data by on 19-9-8.
 * @description 命令
 */
public class Command {

    public static final byte LOGIN_REQUEST = 1;

    public static final byte LOGIN_RESPONSE = 2;

    public static final byte USER_MSG_REQUEST = 3;

    public static final byte USER_MSG_RESPONSE = 4;

    public static final byte OFFLINE_REQUEST = 5;

    public static final byte OffLINE_RESPONSE = 6;

    public static final byte CREATE_GROUP_REQUEST = 7;

    public static final byte CREATE_GROUP_RESPONSE = 8;

    public static final byte LIST_GROUP_MEMBERS_REQUEST = 9;

    public static final byte LIST_GROUP_MEMBERS_RESPONSE = 10;

    public static final byte JOIN_GROUP_REQUEST = 11;

    public static final byte JOIN_GROUP_RESPONSE = 12;

    public static final byte QUIT_GROUP_REQUEST = 13;

    public static final byte QUIT_GROUP_RESPONSE = 14;

    public static final byte GROUP_MSG_REQUEST = 15;

    public static final byte GROUP_MSG_RESPONSE = 16;

    public static final byte HEARTBEAT_REQUEST = 17;

    public static final byte HEARTBEAT_RESPONSE = 18;

    public static final byte MSG_ACK_REQUEST = 19;
}
