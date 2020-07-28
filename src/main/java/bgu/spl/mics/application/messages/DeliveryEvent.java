package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;

public class DeliveryEvent implements Event {

    // Fields
    private Customer customer;

    // Constructor
    public DeliveryEvent(Customer customer) {
        this.customer = customer;
    }

    // Methods
    public Customer getCustomer() {
        return this.customer;
    }


}
