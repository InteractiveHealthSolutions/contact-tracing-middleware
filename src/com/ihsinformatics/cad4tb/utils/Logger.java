package com.ihsinformatics.cad4tb.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {
	
	public static void log(Exception e) {
		try {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String toWrite = sw.toString();
			
			// File externalStorageDir = Environment.getExternalStorageDirectory();
			File myFile = new File(System.getProperty("user.home"), "JennerX_log.txt");

			if (!myFile.exists()) {
				myFile.createNewFile();
			} 
			
			FileOutputStream fOut = new FileOutputStream(myFile, true);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append("\n\n\n\n\n------------------------------------------------------------------------------------\n");
			myOutWriter.append((new Date().toString()));
			myOutWriter.append("\n------------------------------------------------------------------------------------\n");
			myOutWriter.append(toWrite);
			myOutWriter.close();
			fOut.close();
			
		} catch (IOException ee) {
			ee.printStackTrace();
		}
	}
	
	public static void log(String s) {
		try {
			// File externalStorageDir = Environment.getExternalStorageDirectory();
			File myFile = new File(System.getProperty("user.home"), "JennerX_log.txt");

			if (!myFile.exists()) {
				myFile.createNewFile();
			} 
			
			FileOutputStream fOut = new FileOutputStream(myFile, true);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.append("\n\n\n\n\n------------------------------------------------------------------------------------\n");
			myOutWriter.append((new Date().toString()));
			myOutWriter.append("\n------------------------------------------------------------------------------------\n");
			myOutWriter.append(s);
			myOutWriter.close();
			fOut.close();
			
		} catch (IOException ee) {
			ee.printStackTrace();
		}
	}
}
