package anker;

import java.util.Arrays;
import java.util.List;

public class ConfigCmd implements IConfig {
	private List<String> args;
	
	public ConfigCmd(String[] args) {
		// TODO Auto-generated constructor stub
		this.args = Arrays.asList(args);
	}
	
	@Override
	public String getConfigFile() {
		return getValue("-cfg");
	}
	
	@Override
	public String getProjectDirectory() {
		// TODO Auto-generated method stub
		return getValue("-p");
	}

	@Override
	public String getBlockTpl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSrcDirectory() {
		// TODO Auto-generated method stub
		return getValue("-src");
	}

	@Override
	public String getDstDirectory() {
		// TODO Auto-generated method stub
		return getValue("-dst");
	}

	private String getValue(String key) {
		int ptr = args.indexOf(key);
		if(ptr == -1 || (ptr+1) > args.size()) return null;
		ptr++;
		return args.get(ptr);
	}
}
