package de.uni_luebeck.isp.mockito;

import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Test suite using Mockito to generate mock objects.
 *
 * @author Benedict Etzel <benedict.etzel@informatik.uni-luebeck.de>
 *
 */
public class TestWithMockito {

    /**
     * Test of notify method, of class Listener.
     */
    @Test
    public void testNotify() {
        System.out.println("notify");
        // create provider
        Provider provider = new Provider();
        // create mock listeners
        Listener a = mock(Listener.class);
        Listener b = mock(Listener.class);
        // add listeners to provider
        provider.addListener(a);
        provider.addListener(b);
        provider.addListener(b);
        // assert 0 notifications
        verify(a, never()).notify("One");
        verify(a, never()).notify("One");
        // notify listeners
        provider.notifyListeners("Two");
        provider.notifyListeners("One");
        provider.notifyListeners("Two");
        // assert notification counts
        verify(a, times(1)).notify("One");
        verify(a, times(2)).notify("Two");
        verify(b, times(2)).notify("One");
        verify(b, times(4)).notify("Two");

    }
    
    @Test
    public void testRemoveListener() {
    	Provider provider = new Provider();
    	// create mock listener
    	Listener listener = mock(Listener.class);
        // add listener to provider
    	provider.addListener(listener);
    	// assert no calls yet
    	verify(listener, never()).notify("One");
    	// notify listener
    	provider.notifyListeners("One");
    	provider.removeListener(listener);
    	provider.notifyListeners("One");
    	// assert counts
    	verify(listener, times(1)).notify("One");
    }

    @Test
    public void testClearListeners() {
    	Provider provider = new Provider();
    	// create mock listener
    	Listener listener = mock(Listener.class);
        // add listeners to provider
    	provider.addListener(listener);
    	// assert no calls yet
    	verify(listener, never()).notify("One");
    	// notify listener
    	provider.notifyListeners("One");
    	provider.clearListeners();
    	provider.notifyListeners("One");
    	// assert counts
       	verify(listener, times(1)).notify("One");
   }
}
