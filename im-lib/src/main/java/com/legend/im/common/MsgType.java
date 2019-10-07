package com.legend.im.common;

/**
 * @author Legend
 * @data by on 19-9-9.
 * @description
 */
public class MsgType {

    /** 单聊消息 */
    public static final int TO_USER = 0;
    /** 群聊消息 */
    public static final int TO_GROUP = 1;

    /** 消息内容: 文本 */
    public static final int CONTENT_TEXT = 0;
    /** 消息内容: 图片 */
    public static final int CONTENT_PICTURE = 1;
    /** 消息内容: 语音 */
    public static final int CONTENT_AUDIO = 2;
    /** 消息内容: 文件 */
    public static final int CONTENT_FILE = 3;
}
