package so.fe.sofextractor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DBConnector {

    protected Properties prop = new Properties();

    public DBConnector(String confPath) {
	try {
	    prop.load(new FileInputStream(confPath));
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
