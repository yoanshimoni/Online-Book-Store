package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BookOrderEvent;
import bgu.spl.mics.application.messages.DeliveryEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.*;
import java.util.*;

/**
 * APIService is in charge of the connection between a client and the store.
 * It informs the store about desired purchases using {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class APIService extends MicroService{

	// Fields
	private List<BookOrderEvent> orderSchedule;
	private Customer customer;

	// Constructor
	public APIService(String name, Customer customer, List<BookOrderEvent> orderSchedule) {
		super(name);
		this.customer = customer;
		this.orderSchedule = new LinkedList<>();
		this.orderSchedule.addAll(orderSchedule);
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, t->{
			if(t.isLastTick())
				terminate();
			else {
				for (BookOrderEvent obj : this.orderSchedule) {
					if (obj.getOrderTick() == t.getCurrentTick()) {
						Future<OrderReceipt> futureObj = sendEvent(obj);
						OrderReceipt receipt = futureObj.get();
						if (receipt != null) {
							customer.addReceipt(receipt);
							sendEvent(new DeliveryEvent(customer));
						}
					}
				}
			}
		});
		RunningCounter.getInstance().addRunningThread();
	}


}


