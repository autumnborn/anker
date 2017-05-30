package anker;

public interface IConfig {
	String getConfigFileLocation();
	String getProjectDirectory();
	String getBlockTpl();
	String getSrcDirectory();
	String getDstDirectory();
	String getWatcherConfig();
}
