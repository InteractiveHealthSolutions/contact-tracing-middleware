package com.ihsinformatics.cad4tb.mobile;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleMustStartException;
import org.openmrs.util.DatabaseUpdateException;
import org.openmrs.util.InputRequiredException;
import org.openmrs.util.OpenmrsUtil;

/**
 * Servlet implementation class Startup
 */
public class Startup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Startup() {
        super();
		
		startup();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	// /usr/share/tomcat6/.OpenMRS/openmrs-runtime.properties
	//Windows
	public static final File propsFile = new File("C:\\Users\\Admin\\AppData\\Roaming\\OpenMRS\\openmrs-runtime.properties");
	// public static final File propsFile = new File(OpenmrsUtil.getApplicationDataDirectory(), "openmrs-runtime.properties");
	//Linux
	// public static final File propsFile = new File("/usr/share/tomcat6/.OpenMRS/openmrs-runtime.properties");
			
	private void startup() {
		
		System.out.println("props found");
		Properties props = new Properties();
		OpenmrsUtil.loadProperties(props, propsFile);
		
		System.out.println("Starting context with " + (String) props.get("connection.url"));
		System.out.println("Username " + (String) props.get("connection.username"));
		System.out.println("Password " + (String) props.get("connection.password"));
		// Context.updateDatabase(null);
		try {
			System.out.println("Context starting");
			Context.startup((String) props.get("connection.url"), (String) props.get("connection.username"), (String) props.get("connection.password"), props);
			System.out.println("Context started");
		} catch (ModuleMustStartException e) {
			System.out.println("Context not started - module not started");
			e.printStackTrace();
		} catch (DatabaseUpdateException e) {
			System.out.println("Context not started - DB exception");
			e.printStackTrace();
		} catch (InputRequiredException e) {
			System.out.println("Context not started - input required");
			e.printStackTrace();
		}
		
	}
}
