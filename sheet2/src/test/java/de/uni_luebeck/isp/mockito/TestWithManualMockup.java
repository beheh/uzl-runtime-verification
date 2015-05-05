package de.uni_luebeck.isp.mockito;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Test suite using a mock class written manually.
 *
 * @author Benedict Etzel <benedict.etzel@informatik.uni-luebeck.de>
 *
 */
public class TestWithManualMockup {

    /**
     * Test of notify method, of class Listener.
     */
    @Test
    public void testNotify() {
        System.out.println("notify");
        // create provider
        Provider provider = new Provider();
        // create mock listeners
        MockListener a = new MockListener();
        MockListener b = new MockListener();
        // add listeners to provider
        provider.addListener(a);
        provider.addListener(b);
        provider.addListener(b);
        // assert 0 notifications
        assertEquals(0, a.getNotifications("One"));
        assertEquals(0, a.getNotifications("Two"));
        // notify listeners
        provider.notifyListeners("Two");
        provider.notifyListeners("One");
        provider.notifyListeners("Two");
        // assert notification counts
        assertEquals(1, a.getNotifications("One"));
        assertEquals(2, a.getNotifications("Two"));
        assertEquals(2, b.getNotifications("One"));
        assertEquals(4, b.getNotifications("Two"));
    }
    
    @Test
    public void testRemoveListener() {
    	Provider provider = new Provider();
    	// create mock listener
        MockListener listener = new MockListener();
        // add listener to provider
    	provider.addListener(listener);
    	// notify listener
    	provider.notifyListeners("One");
    	provider.removeListener(listener);
    	provider.notifyListeners("One");
    	// assert counts
    	assertEquals(1, listener.getNotifications("One"));
    }

    @Test
    public void testClearListeners() {
    	Provider provider = new Provider();
    	// create mock listener
        MockListener listener = new MockListener();
        // add listeners to provider
    	provider.addListener(listener);
    	// notify listener
    	provider.notifyListeners("One");
    	provider.clearListeners();
    	provider.notifyListeners("One");
    	// assert counts
    	assertEquals(1, listener.getNotifications("One"));
    }
    
    // we would usually keep this class in a separate file, but are keeping this here for the sake of readability
    private class MockListener implements Listener {

        Map<Object, Integer> notificationMap = new HashMap<>();

        @Override
        public void notify(Object argument) {
            if (notificationMap.containsKey(argument)) {
                int previousValue = notificationMap.get(argument);
                notificationMap.put(argument, previousValue + 1);
            } else {
                notificationMap.put(argument, 1);
            }
        }

        public int getNotifications(Object argument) {
            if (!notificationMap.containsKey(argument)) {
                return 0;
            }
            return notificationMap.get(argument);
        }

    }
}
