import java.util.EventListener;

import javax.swing.event.EventListenerList;

interface FWEventListener extends EventListener {
	public void dirModifyEvent(FWEvent e);
}

public class FWEventSet {
	protected EventListenerList listenerList = new EventListenerList();

	public void addFWEventListener(FWEventListener listener) {
		listenerList.add(FWEventListener.class, listener);
	}

	public void removeFWEventListener(FWEventListener listener) {
		listenerList.remove(FWEventListener.class, listener);
	}

	void fireDirModifyEvent(FWEvent evt) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0; i < listeners.length; i += 2) {
			if (listeners[i] == FWEventListener.class) {
				((FWEventListener) listeners[i + 1]).dirModifyEvent(evt);
			}
		}
	}

}
