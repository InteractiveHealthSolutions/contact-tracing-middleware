package com.ihsinformatics.cad4tb.mobile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Properties;



public class Utils {
	public static SimpleDateFormat openMrsDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static int getPatientId() {
		int toReturn = 0;
		String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		 File propsFile =  Startup.propsFile;
		 
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(propsFile));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	
	    String DB_URL = (String) properties.get("connection.url");//"jdbc:mysql://localhost/unfepi3";

	   //  Database credentials
	    String USER = (String) properties.get("connection.username");// "root";
	    String PASS = (String) properties.get("connection.password");// "123456";
		
		Connection conn = null;
		   Statement stmt = null;
		   try{
		      //STEP 2: Register JDBC driver
		      Class.forName(JDBC_DRIVER);

		      //STEP 3: Open a connection
		      System.out.println("Connecting to database...");
		      conn = DriverManager.getConnection(DB_URL,USER,PASS);

		      //STEP 4: Execute a query
		      System.out.println("Creating statement...");
		      stmt = conn.createStatement();
		      String sql;
		      sql = "SELECT MAX(patient_id) as maxLevel FROM patient";
		      ResultSet rs = stmt.executeQuery(sql);

		      //STEP 5: Extract data from result set
		      while(rs.next()){
		         //Retrieve by column name
		         toReturn  = rs.getInt("maxLevel");
		         System.out.println(", Last: " + toReturn);
		      }
		      //STEP 6: Clean-up environment
		      rs.close();
		      stmt.close();
		      conn.close();
		   }catch(SQLException se){
		      //Handle errors for JDBC
		      se.printStackTrace();
		   }catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		            conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   
		return toReturn;
	}
}
