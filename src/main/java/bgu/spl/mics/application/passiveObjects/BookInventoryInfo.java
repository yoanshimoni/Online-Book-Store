package bgu.spl.mics.application.passiveObjects;

import com.google.gson.annotations.SerializedName;

/**
 * Passive data-object representing a information about a certain book in the inventory.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class BookInventoryInfo {

	// Fields
	private String bookTitle;
	@SerializedName("amount")
	private int amountInInventory;
	private int price;

	// Constructor
	public BookInventoryInfo(String bookTitle, int amountInInventory, int price) {
		this.bookTitle = bookTitle;
		this.amountInInventory = amountInInventory;
		this.price = price;
	}


	/**
     * Retrieves the title of this book.
     * <p>
     * @return The title of this book.   
     */
	public String getBookTitle() {
		return bookTitle;
	}

	/**
     * Retrieves the amount of books of this type in the inventory.
     * <p>
     * @return amount of available books.      
     */
	public synchronized int getAmountInInventory() {
		return amountInInventory;
	}
	/**
     * Retrieves the price for  book.
     * <p>
     * @return the price of the book.
     */
	public int getPrice() {
		return price;
	}

	public void setBookTitle (String bookTitle) {
		this.bookTitle = bookTitle;
	}

	public synchronized void setAmountInInventory (int amountInInventory) {
		this.amountInInventory = amountInInventory;
	}

	public void setPrice (int price) {
		this.price = price;
	}

}
