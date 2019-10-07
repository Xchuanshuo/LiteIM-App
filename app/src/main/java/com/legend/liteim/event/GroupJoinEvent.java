package com.legend.liteim.event;

/**
 * @author Legend
 * @data by on 19-9-21.
 * @description
 */
public class GroupJoinEvent {

    private boolean isSuccess;

    public GroupJoinEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
