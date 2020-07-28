package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;

public class BookOrderEvent implements Event<OrderReceipt> {

    // Fields
    private String bookName;
    private Customer customer;
    private int orderTick;

    // Constructor
    public BookOrderEvent(String bookName, Customer customer, int orderTick) {
        this.bookName = bookName;
        this.customer = customer;
        this.orderTick = orderTick;
    }

    public String getBookName() {
        return bookName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getOrderTick() {
        return orderTick;
    }
}
