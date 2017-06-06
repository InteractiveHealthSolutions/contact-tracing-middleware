/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ihsinformatics.cad4tb.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;

import com.mysql.jdbc.StringUtils;

/**
 * Hibernate Utility class with a convenient method to get Session Factory
 * object.
 * 
 * @author Imran
 */
@SuppressWarnings("deprecation")
public class HibernateUtil
{

	private static SessionFactory sessionFactory = null;

	static
	{
		try
		{
			// Create the SessionFactory from standard (hibernate.cfg.xml)
			// config file.

			sessionFactory = new AnnotationConfiguration().configure("cad-hibernate.cfg.xml").buildSessionFactory();
			// sessionFactory = new Configuration().configure().buildSessionFactory();
		}
		catch (Throwable ex)
		{
			// Log the exception.
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}


/*@SuppressWarnings("deprecation")
public synchronized static SessionFactory getSessionFactory (Properties properties, String configFileName) {
	if (sessionFactory == null) {
		Configuration conf = new Configuration();
		
		if(properties != null){
			conf.setProperties(properties);
		}
		
		if(!StringUtils.isEmptyOrWhitespaceOnly(configFileName)){
			conf.configure(configFileName);
		}
		else {
			conf.configure();
		}
		
		sessionFactory = conf.buildSessionFactory();
	}
	return sessionFactory;
}*/

}