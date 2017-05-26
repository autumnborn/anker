package anker;

import static java.lang.System.out;

import java.awt.EventQueue;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Entry {
	public String dir = "test"; //project directory
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Entry ent = new Entry();
					//ent.test(args);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		//System.exit(0);
	}

	public Entry() {
		Path src = Paths.get(dir+"/src");
		Path dst = Paths.get(dir+"/dst");
		Builder builder = new Builder();
		
		try {
			checkDirs(src, dst);
			builder.build(src, dst);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
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
	
	private void exit() {
		System.exit(0);
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
