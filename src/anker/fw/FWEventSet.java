package anker.fw;

import javax.swing.event.EventListenerList;

public class FWEventSet {
	protected EventListenerList listenerList = new EventListenerList();
	
	/**
	 * Adds FolderWatcher event listener
	 * @param listener
	 */
	public void addFWEventListener(IFWEventListener listener) {
		listenerList.add(IFWEventListener.class, listener);
	}

	/**
	 * Removes FolderWatcher event listener
	 * @param listener
	 */
	public void removeFWEventListener(IFWEventListener listener) {
		listenerList.remove(IFWEventListener.class, listener);
	}

	/**
	 * Fires directory modify event
	 * @param evt
	 */
	void fireDirModifyEvent(FWEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == IFWEventListener.class) {
				((IFWEventListener) listeners[i + 1]).dirModifyEvent(evt);
			}
		}
	}

}
