package so.fe.sofextractor;

import java.sql.Connection;

public class MainModule {

    private static final SOFELogger Logger = new SOFELogger(MainModule.class);

    public static void main(String[] args) {

	// Init connection with DB
	DBConnector dbConnection = new DBConnector(false);
	
	// TODO Get Data from DB
	
	// TODO Preprocessing and Extraction
	
	// TODO Update the processed features to DB
	
    }

}
