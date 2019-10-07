package com.legend.im.bean;

/**
 * @author Legend
 * @data by on 19-9-14.
 * @description 携带需要发送的消息的类
 */
public class MsgModel {

    private Long fromId;
    private Long toId;
    private String content;
    private Byte command;
    private Integer contentType;
    private String username;
    private String portrait;
    private String attach;
    private String flag;

    public MsgModel(Long fromId, Long toId, String content,
                    String attach, Byte command,
                    Integer contentType, String username,
                    String portrait, String flag) {
        this.fromId = fromId;
        this.toId = toId;
        this.content = content;
        this.attach = attach;
        this.command = command;
        this.contentType = contentType;
        this.username = username;
        this.portrait = portrait;
        this.flag = flag;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte getCommand() {
        return command;
    }

    public void setCommand(Byte command) {
        this.command = command;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long fromId;
        private Long toId;
        private String content;
        private String attach;
        private Byte command;
        private Integer contentType;
        private String username;
        private String portrait;
        private String flag;

        public Builder fromId(Long fromId) {
            this.fromId = fromId;
            return this;
        }

        public Builder toId(Long toId) {
            this.toId = toId;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder command(Byte command) {
            this.command = command;
            return this;
        }

        public Builder contentType(Integer contentType) {
            this.contentType = contentType;
            return this;
        }


        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder attach(String attach) {
            this.attach = attach;
            return this;
        }

        public Builder portrait(String portrait) {
            this.portrait = portrait;
            return this;
        }

        public Builder flag(String flag) {
            this.flag = flag;
            return this;
        }

        public MsgModel build() {
            return new MsgModel(fromId, toId, content, attach,
                    command, contentType, username, portrait, flag);
        }

    }

    public Msg buildMsg() {
        Msg msg = new Msg();
        msg.setFromId(fromId);
        msg.setToId(toId);
        msg.setContentType(contentType);
        msg.setUsername(username);
        msg.setPortrait(portrait);
        msg.setMsg(content);
        msg.setAttach(attach);
        msg.setFlag(flag);
        return msg;
    }

    @Override
    public String toString() {
        return "MsgModel{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", content='" + content + '\'' +
                ", command=" + command +
                ", contentType=" + contentType +
                ", username='" + username + '\'' +
                ", portrait='" + portrait + '\'' +
                '}';
    }
}
