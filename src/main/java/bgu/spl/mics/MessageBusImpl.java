package bgu.spl.mics;

import java.util.Enumeration;
import java.util.concurrent.*;
import java.util.concurrent.BlockingQueue;
import bgu.spl.mics.application.passiveObjects.RunningCounter;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	// Fields

	//map
	private ConcurrentHashMap<MicroService, BlockingQueue<Message>> mapServiceToMsgQueue;
	//map of subscribers Event
	private ConcurrentHashMap<Class<? extends Event>, LinkedBlockingQueue<MicroService>> mapEventToSubscribers;
	//map of subscribers Broadcast
	private ConcurrentHashMap<Class<? extends Broadcast>, LinkedBlockingQueue<MicroService>> mapBroadcastToSubscribers;
	// future per micro
	private ConcurrentHashMap<Event, Future> mapEventToFuture;

	private static class MessageBusImplHolder {
		private static MessageBusImpl instance = new MessageBusImpl();
	}

	private MessageBusImpl() {
		mapServiceToMsgQueue = new ConcurrentHashMap<>();
		mapEventToSubscribers = new ConcurrentHashMap<>();
		mapBroadcastToSubscribers = new ConcurrentHashMap<>();
		mapEventToFuture = new ConcurrentHashMap<>();

	}

	// static method to create instance of Singleton class
	public static MessageBusImpl getInstance() {
		return MessageBusImplHolder.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
	    mapEventToSubscribers.putIfAbsent(type, new LinkedBlockingQueue<>());
		try {
			mapEventToSubscribers.get(type).put(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		mapBroadcastToSubscribers.putIfAbsent(type, new LinkedBlockingQueue<>());
		try {
			mapBroadcastToSubscribers.get(type).put(m);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		mapEventToFuture.get(e).resolve(result);
	}


	@Override
	public void sendBroadcast(Broadcast b) {
		if (mapBroadcastToSubscribers.get(b.getClass()) != null) {
			for (MicroService m : mapBroadcastToSubscribers.get(b.getClass())) {
				try {
					mapServiceToMsgQueue.get(m).put(b);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}

	@Override
	public <T> Future<T> sendEvent(Event<T> e) {

		Future<T> futureObj;

		if (mapEventToSubscribers.get(e.getClass()) != null) {
			try {
				synchronized (mapEventToSubscribers.get(e.getClass())) {
					if (!mapEventToSubscribers.get(e.getClass()).isEmpty()) {
						futureObj = new Future<>();
						mapEventToFuture.put(e, futureObj);
						MicroService m = mapEventToSubscribers.get(e.getClass()).poll();
						mapServiceToMsgQueue.get(m).put(e);
						mapEventToSubscribers.get(e.getClass()).put(m);


						return futureObj;
					}
				}

			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void register(MicroService m) {
		mapServiceToMsgQueue.putIfAbsent(m, new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
		mapServiceToMsgQueue.remove(m);
		Enumeration<Class<? extends Broadcast>> iterator1 = mapBroadcastToSubscribers.keys();
		while (iterator1.hasMoreElements()) {
			Class<? extends Broadcast> type = iterator1.nextElement();
			mapBroadcastToSubscribers.get(type).remove(m);
		}

		Enumeration<Class<? extends Event>> iterator2 = mapEventToSubscribers.keys();
		while (iterator2.hasMoreElements()) {
			Class<? extends Event> type = iterator2.nextElement();
			mapEventToSubscribers.get(type).remove(m);
		}
		RunningCounter.getInstance().reduceRunningThread();
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		return mapServiceToMsgQueue.get(m).take();
	}

	

}
