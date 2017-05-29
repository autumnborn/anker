package anker;

public class Config implements IConfig{
	public String dir = "test";
	public String src = "src";
	public String dst = "dst";
	public String blockTpl = "[%s]";
	private IConfig cfg;
	
	/**
	 * Class constructor
	 * @param args - command-line arguments
	 */
	public Config(String[] args) {
		// TODO Auto-generated constructor stub
		cfg = new ConfigCmd(args);
		String configPath = cfg.getConfigFileLocation();
		if(configPath != null) {
			cfg = new ConfigFile(configPath);
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
		return (cfg.getProjectDirectory() != null) ? cfg.getProjectDirectory() : this.dir;
	}

	@Override
	public String getBlockTpl() {
		// TODO Auto-generated method stub
		return (cfg.getBlockTpl() != null) ? cfg.getBlockTpl() : this.blockTpl;
	}

	@Override
	public String getSrcDirectory() {
		// TODO Auto-generated method stub
		return (cfg.getSrcDirectory() != null) ? cfg.getSrcDirectory() : this.src;
	}

	@Override
	public String getDstDirectory() {
		// TODO Auto-generated method stub
		return (cfg.getDstDirectory() != null) ? cfg.getDstDirectory() : this.dst;
	}
	

	
}
