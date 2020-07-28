package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class PriceCheckEvent implements Event<Integer> {

    // Fields
    private String bookName;

    // Constructor
    public PriceCheckEvent(String bookName) {
        this.bookName = bookName;
    }

    // Methods
    public String getBookName() {
        return bookName;
    }
}
