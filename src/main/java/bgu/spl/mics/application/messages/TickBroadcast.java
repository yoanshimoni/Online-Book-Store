package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {

    // Fields
    private int currentTick;

    // Constructor
    public TickBroadcast(int currentTick) {
        this.currentTick = currentTick;
    }

    // Methods
    public int getCurrentTick() {
        return this.currentTick;
    }

    public boolean isLastTick(){
        return currentTick == 0;
    }

}
