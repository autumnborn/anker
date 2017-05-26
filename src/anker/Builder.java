package anker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Builder {
	private List<Path> blocks = new ArrayList<Path>();
	private List<Path> templates = new ArrayList<Path>();
	
	public Builder() {
		// TODO Auto-generated constructor stub
		
	}
	
	public void build(Path src, Path dst) throws IOException {
		try {
			findParts(src);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//building begin
		for (Path tpl : templates) {
			String content = readText(tpl);
			for (Path block : blocks) {
				String str = "[" + block.getFileName() + "]";
				content = content.replace(str, readText(block));
			}
			writeText(dst.resolve(tpl.getFileName()), content);
		}
		//building end
		
	}
	
	public List<Path> getBlocks(){
		return blocks;
	}
	
	public List<Path> getTemplates(){
		return templates;
	}	
	
	private void findParts(Path src) throws IOException {
		Files.walkFileTree(src, new SimpleFileVisitor<Path>(){
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				// TODO Auto-generated method stub
				if(isBlock(file.getFileName().toString())) {
					blocks.add(file);
				} else {
					templates.add(file);
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}

	private boolean isBlock(String filename) {
		Pattern pat = Pattern.compile("^ank\\..+");
		Matcher m = pat.matcher(filename);
		return m.matches();
	}
	
	private String readText(Path path) throws IOException {
		byte[] bytes = Files.readAllBytes(path);
		return new String(bytes, StandardCharsets.UTF_8);
	}
	
	public boolean writeText(Path path, String content) {
		System.out.printf("Writing -> '%s'", path);
		
		try {
			Files.write(path, content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		System.out.printf("\tcomplete.\n");
		return true;
	}
}
