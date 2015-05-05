package de.uni_luebeck.isp.mockito;

/**
 * Generic listener that can be notified.
 * 
 * @author Malte Schmitz (Institute for Software Engineering and Programming
 *         Languages, Universitaet zu Luebeck)
 *
 */
public interface Listener {
	/**
	 * Notify the listener.
	 * 
	 * @param argument
	 *            Generic argument.
	 */
	void notify(Object argument);
}
