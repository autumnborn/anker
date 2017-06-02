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
	private Path src;
	private Path dst;
	private String blockTpl;
	
	/**
	 * Class constructor
	 * @param src - source directory path
	 * @param dst - destination directory path
	 * @param blockTpl - block-template
	 */
	public Builder(Path src, Path dst, String blockTpl) {
		// TODO Auto-generated constructor stub
		this.blockTpl = blockTpl;
		this.src = src;
		this.dst = dst;
		
		try {
			findParts(src);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Builds project
	 * @throws IOException
	 */
	public void build() throws IOException {

		findParts(src);

		for (Path tpl : templates) {
			String content = readText(tpl);
			for (Path block : blocks) {
				String str = String.format(blockTpl, block.getFileName());
				content = content.replace(str, readText(block));
			}
			writeText(dst.resolve(tpl.getFileName()), content);
		}

	}
	
	/**
	 * Gets project blocks paths list
	 * @return List&lt;Path&gt;
	 * @throws IOException 
	 */
	public List<Path> getBlocks() {
		return blocks;
	}
	
	/**
	 * Gets project templates paths list
	 * @return List&lt;Path&gt;
	 */
	public List<Path> getTemplates(){
		return templates;
	}	
	
	/**
	 * Finds and sets project parts (blocks, templates) 
	 * @param src
	 * @throws IOException
	 */
	private void findParts(Path src) throws IOException {
		blocks.clear();
		templates.clear();
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

	/**
	 * Checks file for project-block prefix
	 * @param filename String
	 * @return boolean <code>true</code>, if file has prefix
	 */
	private boolean isBlock(String filename) {
		Pattern pat = Pattern.compile("^ank\\..+");
		Matcher m = pat.matcher(filename);
		return m.matches();
	}
	
	/**
	 * Reads all file to String
	 * @param path - Path, with filename
	 * @return String file text
	 * @throws IOException
	 */
	private String readText(Path path) throws IOException {
		byte[] bytes = Files.readAllBytes(path);
		return new String(bytes, StandardCharsets.UTF_8);
	}
	
	/**
	 * Writes text String to file
	 * @param path - Path, with filename
	 * @param content - String
	 * @return boolean <code>true</code>, if success; <code>false</code>, if error
	 */
	public boolean writeText(Path path, String content) {
		System.out.printf("Writing -> '%s'", path);
		
		try {
			Files.write(path, content.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		System.out.printf(" complete.\n");
		return true;
	}
}
