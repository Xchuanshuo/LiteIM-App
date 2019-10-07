package com.legend.liteim.event;

/**
 * @author Legend
 * @data by on 19-9-24.
 * @description
 */
public class JumpEvent {

    private int position;

    public JumpEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
