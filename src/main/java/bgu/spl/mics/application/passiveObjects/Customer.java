package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Passive data-object representing a customer of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Customer implements Serializable {

	// Fields
	private int id, distance;
	private String name, address;
	private List<OrderReceipt> receipts;
	private Vector<orderSchedule> orderSchedule;
	private CreditCard creditCard;

	// Constructor
	public Customer(int id, int distance, CreditCard creditCard, String name, String address, List<OrderReceipt> receipts, orderSchedule[] orderSchedule) {
		this.id = id;
		this.distance = distance;
		this.creditCard = creditCard;
		this.name = name;
		this.address = address;
		this.receipts = receipts;
		this.orderSchedule.addAll(Arrays.asList(orderSchedule));
	}

	/**
     * Retrieves the name of the customer.
     */
	public String getName() {
		return name;
	}

	/**
     * Retrieves the ID of the customer  . 
     */
	public int getId() {
		return id;
	}
	
	/**
     * Retrieves the address of the customer.  
     */
	public String getAddress() {
		return address;
	}
	
	/**
     * Retrieves the distance of the customer from the store.  
     */
	public int getDistance() {
		return distance;
	}

	/**
     * Retrieves a list of receipts for the purchases this customer has made.
     * <p>
     * @return A list of receipts.
     */
	public List<OrderReceipt> getCustomerReceiptList() {
		return receipts;
	}
	
	/**
     * Retrieves the amount of money left on this customers credit card.
     * <p>
     * @return Amount of money left.   
     */
	public synchronized int getAvailableCreditAmount() {
		return creditCard.getCreditBalance();
	}

	public int getCreditNumber() {
		return creditCard.getCreditCardIdNumber();
	}

	// TODO : SHOULD WE FIX ?
	public void setAvailableAmountInCreditCard(int availableAmountInCreditCard) {
		this.creditCard.chargeCreditCard(getAvailableCreditAmount() - availableAmountInCreditCard);
	}

	public Vector<orderSchedule> getOrderSchedule() {
		return orderSchedule;
	}

	public void addReceipt(OrderReceipt r) {
		if (receipts == null) {
			receipts = new LinkedList<>();
		}
		receipts.add(r);
	}
}
