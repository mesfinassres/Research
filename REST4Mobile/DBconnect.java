package rest.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBconnect {
    private Connection connect = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
    String ConnDriver="com.mysql.jdbc.Driver";
    String ConnString="jdbc:mysql://localhost/rest_demo?user=root&password=Hadaslema1";

    //Connect to database
    public Connection open(){
        Connection con= null;
        try{
            Class.forName(ConnDriver);
            con= DriverManager.getConnection(ConnString);
        } 
        catch (Exception e){ 
            System.out.println(e); 
        }
        return con;
    }
    
    // Close the resultSet, statement, or connection
    public void close() {
        try {
                if (resultSet != null) {
                        resultSet.close();
                }

                if (statement != null) {
                        statement.close();
                }

                if (connect != null) {
                        connect.close();
                }
        } 
        catch (Exception e) {

        }
    }
}