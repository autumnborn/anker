package anker;

import static java.lang.System.out;

import java.awt.EventQueue;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import anker.fw.FWEvent;
import anker.fw.IFWEventListener;
import anker.fw.FolderWatcher;



public class Entry {
	public String dir; //project directory
	public FolderWatcher fw;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Config config = new Config(args);
					Entry ent = new Entry(config);
					//ent.test(args);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//System.exit(0);
	}
	
	/**
	 * Class constructor
	 * @param config - Config class instance
	 * @throws IOException 
	 */
	public Entry(Config config) throws IOException {
		this.dir = config.getProjectDirectory();
		Path src = Paths.get(String.format("%s/%s", dir, config.getSrcDirectory()));
		Path dst = Paths.get(String.format("%s/%s", dir, config.getDstDirectory()));
		checkDirs(src, dst);
		Builder builder = new Builder(src, dst, config.getBlockTpl());
		
		System.out.println(config.isWatchEnabled());
		
		fw = new FolderWatcher();
		fw.setKinds(false, false, true);
		fw.addFWEventListener(new IFWEventListener() {
			@Override
			public void dirModifyEvent(FWEvent e) {
				// TODO Auto-generated method stub
				try {
					builder.build();
					System.out.println(e.getKind());
					System.out.println(e.getFilename());
					System.out.println(e.getPath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	
		if(true) {
			fw.registerDirectory(src, config.isWatchTree());
			System.out.println(config.isWatchTree());
			fw.start();
			consoleIn();
			fw.stop();
		} else {
			builder.build();
		}
		
	}
	
	/**
	 * Checks important directories exists in project folder 
	 * @param src - Path to project sources folder
	 * @param dst - Path to project destination folder
	 * <p><strong>Note: </strong> If destination folder not exist, it will be made;
	 * If source folder not exist, then exit.</p>
	 */
	private void checkDirs(Path src, Path dst) {
		boolean result = false;
		File dir = src.toFile();
		
		if(!dir.isDirectory()) {
			out.printf("Error: '%s' not exist. Stopped.\n", dir.getPath());
			exit();
		}
		
		dir = dst.toFile();
		if(!dir.isDirectory()) result = dir.mkdir();
		out.println( dir.getPath() + ((result) ? " | Created." : " | Skipped." ) );
	}
	
	/**
	 * Exit
	 */
	private void exit() {
		System.exit(0);
	}
	
	private void consoleIn() {
//		Console con = System.console();
//		while(true) {
//			String cmd = con.readLine();
//			cmd.toLowerCase();
//			if(cmd.equals("quit") || cmd.equals("q")) break;
//			out.println(cmd);
//		}
		while(true){	
		}
		
	}
//	private void test(String[] args) {
//		out.println("anker");
//		if(args.length != 0) {
//			for (String a : args) {
//				out.println(a);
//			}
//		}
//	
//		Console con = System.console();
//		con.printf("hello %s", 1);
//
//		while(true) {
//			String cmd = con.readLine();
//			if(cmd.equals("quit")) break;
//			out.println(cmd);
//		}
//
//		out.println("Quit.");
//		out.println("---");
//		System.exit(0);
//	}

}
