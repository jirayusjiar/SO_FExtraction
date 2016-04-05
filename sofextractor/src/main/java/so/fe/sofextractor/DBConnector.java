package so.fe.sofextractor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnector {

    protected Properties prop = null;
    protected Connection dbConnection = null;
    private static final SOFELogger Logger = new SOFELogger(DBConnector.class);

    public DBConnector() {

	// DB config is not loaded
	if (prop == null) {

	    // Load DB property file
	    try {
		prop = new Properties();
		prop.load(new FileInputStream(
			"./resources/main/dbconf.properties"));
		Logger.info("Finish loading db config");
	    } catch (FileNotFoundException e) {
		Logger.error(
			"DB config file not found on /resources/main/dbconf.properties",
			e);
		System.exit(0);
	    } catch (IOException e) {
		Logger.error("Fail to load db config file", e);
		System.exit(0);
	    }

	}

	// Load parameters
	String ipaddr = prop.getProperty("db.ipaddr");
	String port = prop.getProperty("db.port");
	String dbname = prop.getProperty("db.dbname");
	String dbid = prop.getProperty("db.id");
	String dbpwd = prop.getProperty("db.passwd");

	// Connect to DB
	try {
	    dbConnection = DriverManager.getConnection("jdbc:postgresql://"
		    + ipaddr + ":" + port + "/" + dbname, dbid, dbpwd);
	} catch (SQLException e) {
	    Logger.error("Cannot connect to DB", e);
	    System.exit(0);
	}

	Logger.info("Successfully connect to DB");
    }

    public ResultSet executeQuery(String inputQuery) {

	try {
	    return this.dbConnection.createStatement().executeQuery(inputQuery);
	} catch (SQLException e) {
	    Logger.error("Unable to execute query\n" + inputQuery, e);
	    return null;
	}

    }
}
