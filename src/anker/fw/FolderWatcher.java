import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FolderWatcher extends FWEventSet {
	private WatchService watcher;
	private Kind<?>[] kinds = {ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY};
	private List<String> types = Arrays.asList("text/plain", "text/html", "text/css");//by default
	private Thread WatchThread;
	
	/**
	 * FolderWatcher class constructor
	 * @throws IOException
	 */
	public FolderWatcher() throws IOException {
		watcher = FileSystems.getDefault().newWatchService();
	}
	
	/**
	 * Sets MIME-types of files, which events will be triggered 
	 * @param types
	 */
	public void setMIME(List<String> types) {
		this.types = types;
	}
	
	/**
	 * Sets event kinds, which will be a trigger
	 * @param kinds - array of kinds
	 * <p>by default <code>kinds = {ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY}</code></p>
	 * @see StandardWatchEventKinds
	 */
	public void setKinds(Kind<?>[] kinds) {
		this.kinds = kinds;
	}

	/**
	 * Sets event kinds, which will be a trigger
	 * @param kinds - string array of kinds: "create", "delete" (or/and) "modify"
	 * <p>by default <code>kinds = new String[] {"create", "delete", "modify"}</code></p>
	 * <strong>Note: </strong>If passed array is empty, then default kinds will be used!
	 * Also, wrong kinds will be skipped.
	 */
	public void setKinds(String[] kinds) {
		List<Kind<?>> result = new ArrayList<>();
		if(kinds.length == 0) return;
	
		for (String item : kinds) {
			switch (item.toLowerCase()) {
			case "create":
				result.add(ENTRY_CREATE);
				break;
			case "delete":
				result.add(ENTRY_DELETE);
				break;
			case "modify":
				result.add(ENTRY_MODIFY);
				break;
			default:
				System.out.format("Error at '%s': Wrong kind -> '%s' | Skipped...%n", this.getClass(), item);
				break;
			}
		}
		
		if(!result.isEmpty()) this.kinds =  result.toArray(new Kind<?>[0]);
	}
	
	/**
	 * Sets event kinds, which will be a trigger
	 * @param create boolean
	 * @param delete boolean
	 * @param modify boolean
	 * <p>by default all kinds changed</p>
	 * <strong>Note: </strong>If all parameters set to <code>false</code>,
	 * then default kinds will be used!
	 */
	public void setKinds(boolean create, boolean delete, boolean modify) {
		List<Kind<?>> kinds = new ArrayList<>();
		if(!(create || delete || modify)) return;
		if(create) kinds.add(ENTRY_CREATE);
		if(delete) kinds.add(ENTRY_DELETE);
		if(modify) kinds.add(ENTRY_MODIFY);
		this.kinds =  kinds.toArray(new Kind<?>[0]);
	}
	
	/**
	 * Register directory for watching
	 * @param path - directory path
	 * @param tree - include sub-directories
	 * @throws IOException
	 */
	public void registerDirectory(String path, boolean tree) throws IOException {
		Path dir = FileSystems.getDefault().getPath(path, "");
		registerDirectory(dir, tree);
	}
	
	/**
	 * Register directory for watching
	 * @param path - directory path
	 * @param tree - include sub-directories
	 * @throws IOException
	 */
	public void registerDirectory(Path path, boolean tree) throws IOException {
		if(tree) registerAll(path); else register(path);
	}
	
	/**
	 * Start watching
	 */
	public void start() {
		WatchThread = new Thread(WatchProcess);
		WatchThread.setDaemon(true);
		WatchThread.start();
	}
	
	/**
	 * Stop watching
	 */
	public void stop() {
		if(!WatchThread.isAlive()) return;
		WatchThread.interrupt();
	}
	
	/**
	 * Register directory
	 * @throws IOException
	 */
	private void register(Path dir) throws IOException {
		dir.register(watcher, kinds);
	}
	
	/**
	 * Register directory with sub-directories
	 * @throws IOException
	 */
	private void registerAll(Path startDir) throws IOException {
		Files.walkFileTree(startDir, new SimpleFileVisitor<Path>() {
	        @Override
	        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
	            throws IOException {
	                dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
	        		return FileVisitResult.CONTINUE;
	        }
	    });
	}
	
	/**
	 * Watch processing
	 */
	Runnable WatchProcess = new Runnable() {
		public void run() {
			while(true) {

			    // wait for key to be signaled
			    WatchKey key;
			    try {
			        key = watcher.take();
			    } catch (InterruptedException x) {
			        return;
			    }

			    for (WatchEvent<?> event: key.pollEvents()) {
			        WatchEvent.Kind<?> kind = event.kind();

			        if (kind == StandardWatchEventKinds.OVERFLOW) {
			            continue;
			        }

			        @SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>)event;
			        Path filename = ev.context();

			        try {

			        	Path child = ( (Path) key.watchable() ).resolve(filename);
			        	System.out.println(child);
		                if ( !types.contains(Files.probeContentType(child)) ) {
		                	if(Files.isDirectory(child)) System.out.println(child);		                
		                	continue;
			            }
			            
			        } catch (IOException x) {
			            System.err.println(x);
			            continue;
			        }
			       
			        fireDirModifyEvent(new FWEvent(event, key));
			       
			        System.out.format("--> %s%n", filename);
			        
			    }

			    boolean valid = key.reset();
			    if (!valid) break;
			    
			}	
		}
	};


}
