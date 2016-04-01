package so.fe.sofextractor;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class TestSOFELogger {

    private Logger logger = null;

    public TestSOFELogger(Class inputClass) {

	this.logger = Logger.getLogger(inputClass);
	PropertyConfigurator.configure("log4j.properties");

    }

    public void info(String inputText) {
	this.logger.info(inputText);
    }

    public void error(String inputText) {
	this.logger.error(inputText);
    }

}
