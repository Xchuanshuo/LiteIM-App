package com.worldtreestd.finder.event;

/**
 * @author Legend
 * @data by on 19-1-20.
 * @description
 */
public class CollectEvent {

    private boolean isCollected;

    public CollectEvent(boolean isCollected) {
        this.isCollected = isCollected;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }
}
