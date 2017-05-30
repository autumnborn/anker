package anker.fw;

import java.util.EventListener;

public interface IFWEventListener extends EventListener {
	public void dirModifyEvent(FWEvent e);
}

