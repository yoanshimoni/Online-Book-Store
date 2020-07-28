package bgu.spl.mics.application.passiveObjects;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryTest {

    Inventory inv = null;
    BookInventoryInfo a, b, c;
    BookInventoryInfo[] inventory;

    @Before
    public void setUp() throws Exception {
        inv = Inventory.getInstance();
        a = new BookInventoryInfo("Harry Poter", 50, 60);
        b = new BookInventoryInfo("Harry Poter 2", 10, 80);
        c = new BookInventoryInfo("Harry Poter 3", 5, 100);
        inventory = new BookInventoryInfo[]{a, b};
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getInstance() {
        assertNotNull(inv);
        assertEquals(inv, Inventory.getInstance());
    }

    @Test
    public void load() {
        inv.load(inventory);

        assertEquals(inv.checkAvailabiltyAndGetPrice(a.getBookTitle()), a.getPrice());
        assertEquals(inv.checkAvailabiltyAndGetPrice(b.getBookTitle()), b.getPrice());
        assertEquals(inv.checkAvailabiltyAndGetPrice(c.getBookTitle()), (-1));

        assertEquals(inv.take(a.getBookTitle()), OrderResult.SUCCESSFULLY_TAKEN);
        assertEquals(inv.take(b.getBookTitle()), OrderResult.SUCCESSFULLY_TAKEN);
        assertEquals(inv.take(c.getBookTitle()), OrderResult.NOT_IN_STOCK);

    }

    @Test
    public void take() {
        inv.load(inventory);

        int checkAmount = a.getAmountInInventory();
        assertEquals(inv.take(a.getBookTitle()), OrderResult.SUCCESSFULLY_TAKEN);
        assertEquals(a.getAmountInInventory(), (checkAmount - 1));

        checkAmount = b.getAmountInInventory();
        assertEquals(inv.take(b.getBookTitle()), OrderResult.SUCCESSFULLY_TAKEN);
        assertEquals(b.getAmountInInventory(), (checkAmount - 1));

        checkAmount = c.getAmountInInventory();
        assertEquals(inv.take(c.getBookTitle()), OrderResult.NOT_IN_STOCK);
        assertEquals(c.getAmountInInventory(), checkAmount);
    }

    @Test
    public void checkAvailabiltyAndGetPrice() {
        inv.load(inventory);

        assertEquals(inv.checkAvailabiltyAndGetPrice(a.getBookTitle()), a.getPrice());
        assertEquals(inv.checkAvailabiltyAndGetPrice(b.getBookTitle()), b.getPrice());
        assertEquals(inv.checkAvailabiltyAndGetPrice(c.getBookTitle()), (-1));
    }

    @Test
    public void printInventoryToFile() {

    }
}