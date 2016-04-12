package so.fe.sofextractor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class DBConnector {

    private static final SOFELogger Logger = new SOFELogger(DBConnector.class);
    private static final Integer commitThreshold = 1000; // Commit every 1000
							 // action

    protected Integer actionCounter = 0;
    protected Properties prop = null;
    protected Connection dbConnection = null;
    protected PreparedStatement preparedStatement = null;
    protected String preparedStatementQuery = null;

    /**
     * Execute and clear the preparedStatement
     */
    private void executeAndClearBatch() {
	try {
	    preparedStatement.executeBatch();
	    Logger.info("Succesfully commit the preparedStatement");

	    // Reset the preparedStatement
	    preparedStatement.clearBatch();
	    actionCounter = 0;
	} catch (SQLException e) {
	    Logger.error("Cannot commit the preparedStatement", e);
	}
    }

    /**
     * Constructor of DBConnector class
     * 
     * @param testMode
     *            -> True : for testing purpose -> False : real execution
     */
    public DBConnector(boolean testMode) {

	// DB config is not loaded
	if (prop == null) {

	    // Load DB property file
	    try {
		prop = new Properties();
		
		if (!testMode)
		    prop.load(new FileInputStream(
			    "./resources/main/dbconf.properties"));
		else
		    prop.load(new FileInputStream(
			    "./resources/test/dbconf.properties"));
		
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
	    dbConnection.setAutoCommit(false);
	} catch (SQLException e) {
	    Logger.error("Cannot connect to DB", e);
	    System.exit(0);
	}

	Logger.info("Successfully connect to DB");
    }

    /**
     * 
     * @param inputQuery
     *            - query text as string
     * @return Result of the executed query
     */
    public ResultSet executeQuery(String inputQuery) {

	try {

	    this.preparedStatementQuery = inputQuery;
	    return this.dbConnection.createStatement().executeQuery(inputQuery);

	} catch (SQLException e) {
	    Logger.error("Unable to execute query\n" + inputQuery, e);
	    return null;
	}

    }

    /**
     * Create a preparedStatement instance from inputStatement
     * 
     * @param inputStatement
     */
    public void prepareStatement(String inputStatement) {
	try {
	    this.preparedStatement = this.dbConnection
		    .prepareStatement(inputStatement);
	} catch (SQLException e) {
	    Logger.error("Cannot prepare the input statement\n"
		    + inputStatement, e);
	}
    }

    /**
     * Add an instance to the preparedStatement then execute when the number of
     * instances reaches the commit threshold
     * 
     * @param inputInstance
     */

    public void addBatchToStatement(List<Object> inputInstance) {

	int index = 1;

	// Add a single instance to the preparedStatement
	for (Object o : inputInstance) {

	    try {
		if (o instanceof Integer) {
		    // Integer type
		    this.preparedStatement.setInt(index, (Integer) o);
		} else if (o instanceof String) {
		    // String type
		    this.preparedStatement.setString(index, String.valueOf(o));
		}
	    } catch (SQLException e) {
		Logger.error("Cannot add instance to preparedStatement\n"
			+ this.preparedStatementQuery + "\n" + o.toString());
	    }
	    ++index;
	}

	// Add batch
	try {
	    preparedStatement.addBatch();
	    ++actionCounter;
	} catch (SQLException e) {
	    Logger.error("Cannot add batch to the preparedStatement" + "\n"
		    + preparedStatementQuery, e);
	}

	// If the counter reach threshold then commit the preparedStatement
	if (actionCounter == commitThreshold)
	    executeAndClearBatch();
    }

    /**
     * Check whether the preparedStatement is committed or not -> Committed :
     * Close the preparedStatement -> Not committed : Execute then close the
     * preparedStatement
     */
    public void closePreparedStatement() {
	if (actionCounter != 0)
	    executeAndClearBatch();
	try {
	    preparedStatement.close();
	    preparedStatement = null;
	    preparedStatementQuery = null;
	} catch (SQLException e) {
	    Logger.error("Unable to close preparedStatement", e);
	}
    }

    /**
     * Close the connection with DB
     */
    public void close() {
	try {
	    dbConnection.close();
	} catch (SQLException e) {
	    Logger.error("Unable to close the connection with DB", e);
	}
    }

}
