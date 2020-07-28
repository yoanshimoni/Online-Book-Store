package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FutureTest<T> {

    Future<T> f;
    T result;

    @Before
    public void setUp() throws Exception {
        f = new Future<T>();
        result = null;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void get() {
        assertFalse(f.isDone());
        f.resolve(result);
        assertEquals(result, f.get());
        assertTrue(f.isDone());

    }

    @Test
    public void resolve() {
        assertFalse(f.isDone());
        f.resolve(result);
        assertEquals(result, f.get());
        assertTrue(f.isDone());
    }

    @Test
    public void isDone() {
        assertFalse(f.isDone());
        f.get();
        assertTrue(f.isDone());
    }

    @Test
    public void get1() {
        assertNull(f.get(0, TimeUnit.MILLISECONDS));
        assertFalse(f.isDone());
    }
}