package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PriceCheckEvent;
import bgu.spl.mics.application.messages.TakeBookEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import bgu.spl.mics.application.passiveObjects.RunningCounter;

/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService{

	private Inventory inventory;

	public InventoryService(String name) {
		super(name);
		inventory = Inventory.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, t -> {
			if (t.isLastTick())
				terminate();
		});

		subscribeEvent(PriceCheckEvent.class, c -> {
			complete(c, (inventory.checkAvailabiltyAndGetPrice(c.getBookName())));
		});

		subscribeEvent(TakeBookEvent.class, c -> {
			inventory.take(c.getBookName());
			complete(c, null);
		});
		RunningCounter.getInstance().addRunningThread();
	}

}
