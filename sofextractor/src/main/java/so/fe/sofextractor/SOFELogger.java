package so.fe.sofextractor;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class SOFELogger {

    private Logger logger = null;

    public SOFELogger(Class inputClass) {

	this.logger = Logger.getLogger(inputClass);
	String current;
	try {
	    current = new java.io.File( "." ).getCanonicalPath();
	        System.out.println("Current dir:"+current);
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	PropertyConfigurator.configure("./resources/main/log4j.properties");

    }

    public void info(String inputText) {
	this.logger.info(inputText);
    }

    public void error(String inputText) {
	this.logger.error(inputText);
    }

}
