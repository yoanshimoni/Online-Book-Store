package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import bgu.spl.mics.application.passiveObjects.RunningCounter;

import java.util.Timer;
import static java.lang.Thread.sleep;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

	// Fields
	private int speed;
	private int duration;
	private Timer timer;

	public TimeService(int speed, int duration) {
		super("Time");
		this.speed = speed;
		this.duration = duration;
		this.timer = new Timer();
	}

	@Override
	protected void initialize() {
		int currentTick = 1;

		while (currentTick < duration) {
			sendBroadcast(new TickBroadcast(currentTick));
			try {
				sleep(speed);
				currentTick++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		sendBroadcast(new TickBroadcast(0));
		terminate();
		RunningCounter.getInstance().reduceRunningThread();
	}

}
