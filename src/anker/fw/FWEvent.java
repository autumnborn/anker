package anker.fw;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.EventObject;

public class FWEvent extends EventObject {
	private static final long serialVersionUID = 3827972947355505472L;
	private WatchEvent<?> event;
	private WatchKey key;

	public FWEvent(WatchEvent<?> event, WatchKey key) {
		super(event);
		this.event = event;
		this.key = key;
	}
	
	public Path getFilename() {
		return (Path) event.context();
	}
	
	public Path getPath() {
		return (Path) key.watchable();
	}
	
	public String getKind() {
		return event.kind().name();
	}

}
