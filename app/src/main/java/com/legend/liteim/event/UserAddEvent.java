package com.legend.liteim.event;

/**
 * @author Legend
 * @data by on 19-9-18.
 * @description
 */
public class UserAddEvent {

    private boolean isSuccess;

    public UserAddEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
