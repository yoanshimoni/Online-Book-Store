package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class TakeBookEvent implements Event {

    // Fields
    private String bookName;
    private int tick;

    // Constructor
    public TakeBookEvent(String bookName) {
        this.bookName = bookName;
    }

    // Methods
    public String getBookName() {
        return bookName;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
}
