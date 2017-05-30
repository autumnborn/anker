package anker;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigFile implements IConfig {
	private Properties prop = new Properties();
	private FileInputStream fis;	
	
	/**
	 * Class constructor
	 * @param path - configuration file location
	 */
	public ConfigFile(String path) {
		// TODO Auto-generated constructor stub
		try {
			fis = new FileInputStream(path);
			prop.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public String getConfigFileLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProjectDirectory() {
		// TODO Auto-generated method stub
		return prop.getProperty("dir.project");
	}

	@Override
	public String getBlockTpl() {
		// TODO Auto-generated method stub
		return prop.getProperty("block.tpl");
	}

	@Override
	public String getSrcDirectory() {
		// TODO Auto-generated method stub
		return prop.getProperty("dir.src");
	}

	@Override
	public String getDstDirectory() {
		// TODO Auto-generated method stub
		return prop.getProperty("dir.dst");
	}

	@Override
	public String getWatcherConfig() {
		// TODO Auto-generated method stub
		return null;
	}

}
