package de.uni_luebeck.isp.mockito;

import java.util.LinkedList;
import java.util.List;

/**
 * A Provider can keep track of registered {@link Listener} and
 * {@link Listener#notify(Object)} them. The provider passes on one such
 * notification to all its listeners.
 * 
 * @author Malte Schmitz (Institute for Software Engineering and Programming
 *         Languages, Universitaet zu Luebeck)
 *
 */
public class Provider {
	/**
	 * List of registered listeners.
	 */
	private List<Listener> listeners = new LinkedList<Listener>();

	/**
	 * Add a new listener. One listener can be added multiple times. In that
	 * case it will receive the notification multiple times as well.
	 * 
	 * @param listener
	 *            The new listener.
	 */
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove the given listener. This will remove the given listener
	 * completely, even if it was registered multiple times.
	 * 
	 * @param listener
	 *            The listener to be removed.
	 */
	public void removeListener(Listener listener) {
		while (listeners.remove(listener));
	}

	/**
	 * Remove all registered listeners.
	 */
	public void clearListeners() {
		listeners.clear();
	}

	/**
	 * Notify all registered listeners and pass them the given argument.
	 * 
	 * @param argument
	 *            Generic argument that will be passed on to every registered
	 *            listener.
	 */
	public void notifyListeners(Object argument) {
		for (Listener listener : listeners) {
			listener.notify(argument);
		}
	}
}
