package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Passive object representing the resource manager.
 * You must not alter any of the given public methods of this class.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class ResourcesHolder {

	// Fields
	private ConcurrentLinkedQueue<Future<DeliveryVehicle>> vehiclesWaiting;
	private ConcurrentLinkedQueue<DeliveryVehicle> vehicles;

	/**
     * Retrieves the single instance of this class.
     */

	private static class ResourcesInstanceHolder {
		private static ResourcesHolder instance = new ResourcesHolder();
	}

	private ResourcesHolder() {
		vehiclesWaiting = new ConcurrentLinkedQueue<>();
		vehicles = new ConcurrentLinkedQueue<>();
	}

	// static method to create instance of Singleton class
	public static ResourcesHolder getInstance() {
		return ResourcesInstanceHolder.instance;
	}


	/**
     * Tries to acquire a vehicle and gives a future object which will
     * resolve to a vehicle.
     * <p>
     * @return 	{@link Future<DeliveryVehicle>} object which will resolve to a 
     * 			{@link DeliveryVehicle} when completed.   
     */
	public Future<DeliveryVehicle> acquireVehicle() {
		synchronized (vehicles) {
			Future<DeliveryVehicle> futureObj = new Future<>();
			if (!vehicles.isEmpty()) {
				futureObj.resolve(vehicles.poll());
			}
			else {
				vehiclesWaiting.add(futureObj);
			}
			return futureObj;
		}
	}
	
	/**
     * Releases a specified vehicle, opening it again for the possibility of
     * acquisition.
     * <p>
     * @param vehicle	{@link DeliveryVehicle} to be released.
     */
	public void releaseVehicle(DeliveryVehicle vehicle) {
		synchronized (vehicles) {
			if (vehiclesWaiting.isEmpty()) {
				vehicles.add(vehicle);
			}
			else {
				vehiclesWaiting.poll().resolve(vehicle);
			}
		}
	}
	
	/**
     * Receives a collection of vehicles and stores them.
     * <p>
     * @param vehicles	Array of {@link DeliveryVehicle} instances to store.
     */
	public void load(DeliveryVehicle[] vehicles) {
		Collections.addAll(this.vehicles, vehicles);
	}

}
