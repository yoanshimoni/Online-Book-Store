package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.*;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService{

	private MoneyRegister moneyRegister;
	private int currentTick;

	public SellingService(String name) {
		super(name);
		this.moneyRegister = MoneyRegister.getInstance();
	}

	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, t -> {
			if (t.isLastTick())
				terminate();
			else
				this.currentTick = t.getCurrentTick();
		});

		subscribeEvent(BookOrderEvent.class, callback -> {
			Future<Integer> futureBookPrice = sendEvent(new PriceCheckEvent(callback.getBookName()));
			int availableAmountInCreditCard = callback.getCustomer().getAvailableCreditAmount();
			int price = futureBookPrice.get();

			if (futureBookPrice.get() != null && futureBookPrice.get() > -1 && futureBookPrice.get() <= availableAmountInCreditCard) {
				moneyRegister.chargeCreditCard(callback.getCustomer(), futureBookPrice.get());
				OrderReceipt receipt = new OrderReceipt(CountReceipts.getInstance().getNextReceiptsNumber(), callback.getCustomer().getId(), futureBookPrice.get(), getName(), callback.getBookName(), currentTick, callback.getOrderTick(), currentTick);
				moneyRegister.file(receipt);
				sendEvent(new TakeBookEvent(callback.getBookName()));
				complete(callback, receipt);
			}
			else {
				complete(callback, null);
			}
		});
		RunningCounter.getInstance().addRunningThread();
	}

}
