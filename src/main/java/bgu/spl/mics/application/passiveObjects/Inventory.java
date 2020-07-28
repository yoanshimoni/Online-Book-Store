package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive data-object representing the store inventory.
 * It holds a collection of {@link BookInventoryInfo} for all the
 * books in the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory implements Serializable {

	// Fields
	private ConcurrentHashMap<String, BookInventoryInfo> mapNameToBookInventory;


	/**
     * Retrieves the single instance of this class.
     */

	private static class InventoryHolder {
		private static Inventory instance = new Inventory();
	}

	private Inventory() {
		mapNameToBookInventory = new ConcurrentHashMap<>();
	}

	// static method to create instance of Singleton class
	public static Inventory getInstance() {
		return InventoryHolder.instance;
	}


	/**
     * Initializes the store inventory. This method adds all the items given to the store
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
	public void load (BookInventoryInfo[ ] inventory ) {
		for (BookInventoryInfo book: inventory) {
			mapNameToBookInventory.put(book.getBookTitle(), book);
		}
	}
	
	/**
     * Attempts to take one book from the store.
     * <p>
     * @param book 		Name of the book to take from the store
     * @return 	an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN.
     * 			The first should not change the state of the inventory while the 
     * 			second should reduce by one the number of books of the desired type.
     */
	public OrderResult take (String book) {
		if (mapNameToBookInventory.containsKey(book)) {
			if (mapNameToBookInventory.get(book).getAmountInInventory() > 0) {
				int prevAmount = mapNameToBookInventory.get(book).getAmountInInventory();
				mapNameToBookInventory.get(book).setAmountInInventory(prevAmount - 1);
				return OrderResult.SUCCESSFULLY_TAKEN;
			}
		}
		return OrderResult.NOT_IN_STOCK;
	}
	
	
	
	/**
     * Checks if a certain book is available in the inventory.
     * <p>
     * @param book 		Name of the book.
     * @return the price of the book if it is available, -1 otherwise.
     */
	public int checkAvailabiltyAndGetPrice(String book) {
		if (mapNameToBookInventory.containsKey(book) && mapNameToBookInventory.get(book).getAmountInInventory() > 0){
			return mapNameToBookInventory.get(book).getPrice();
		}
		else
			return -1;
	}
	
	/**
     * 
     * <p>
     * Prints to a file name @filename a serialized object HashMap<String,Integer> which is a 
     * Map of all the books in the inventory. The keys of the Map (type {@link String})
     * should be the titles of the books while the values (type {@link Integer}) should be
     * their respective available amount in the inventory. 
     * This method is called by the main method in order to generate the output.
     */
	public void printInventoryToFile(String filename){
		HashMap<String, Integer> printInventoryHashMap = new HashMap<>();
		for (String book : this.mapNameToBookInventory.keySet()) {
			printInventoryHashMap.put(this.mapNameToBookInventory.get(book).getBookTitle(), this.mapNameToBookInventory.get(book).getAmountInInventory());
		}
		PrintSerializeToFile printer = new PrintSerializeToFile(filename);
		printer.printSerializedHashMap(printInventoryHashMap);
	}
}
