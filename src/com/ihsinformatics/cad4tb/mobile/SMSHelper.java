package com.ihsinformatics.cad4tb.mobile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;
import org.irdresearch.smstarseel.service.SMSService;
import org.json.simple.JSONObject;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PersonService;

import net.jmatrix.eproperties.EProperties;

public class SMSHelper {

	public static final String EVENT_TYPE = "event_type";
	public static final String RECIPIENT_NUMBER = "recipient_number";
	public static final String CHW_NAME = "chw_name";
	public static final String SESSION_DATE = "session_date";
	public static final String MH_ID = "mh_id";
	public static final String REGISTRATION = "registration";
	public static final String NEW_SESSION = "new_session";
	private DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	
	private static final String		TARSEEL_CFG		= "smstarseel.cfg.xml";
	private static final String		TARSEEL_PROP	= "smstarseel.properties";
	private TarseelServices	tarseelService;
	
	private SMSHelper() {
		try {
			InputStream f = Thread.currentThread ().getContextClassLoader ().getResourceAsStream (TARSEEL_PROP);
			EProperties root = new EProperties ();
			root.load (f);
			Properties prop = new Properties ();
			prop.putAll (convertEntrySetToMap (root.entrySet ()));
		
			TarseelContext.instantiate (prop, TARSEEL_CFG);
			System.out.println ("TarseelContext instantiated");
			
		} catch (InstanceAlreadyExistsException e) {
			e.printStackTrace();
			System.out.println ("TarseelContext not instantiated");
		} catch (IOException e) {
			System.out.println ("TarseelContext not instantiated, failed to load `smstarseel.properties` file");
			e.printStackTrace();
		}
		// tarseelService = TarseelContext.getServices ();
		System.out.println ("SMS Service is running...");
		
	}
	
	public static SMSHelper instance;
	public static SMSHelper getInstance() {
		if(instance == null) {
			instance = new SMSHelper();
		}
		
		return instance;
	}
	
	
	
	public synchronized void scheduleReminders (final JSONObject data, Person p, PersonService personService) {
		
				
					
				    try {
				    	PersonAttributeType t = personService.getPersonAttributeTypeByName("Contact");
				    	PersonAttribute pa = p.getAttribute(t);
				    	String cellNumber = "";
				    	if(pa!=null) {
				    	cellNumber = pa.getValue();
				    	tarseelService = TarseelContext.getServices();
				    	SMSService smsService = tarseelService.getSmsService();
				    	String mhId = data.get(MH_ID).toString();
				    	String sessionDate = data.get(SESSION_DATE).toString();
				    	DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
				    	DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				    	try {
				    		Date appDate = format.parse(sessionDate);
				    		Date firstReminderDate = null;
					    	Date secondReminderDate = null;
					    	
				    		Calendar calendar = Calendar.getInstance();
				    		calendar.setTime((appDate));
				    		
				    		calendar.set(Calendar.HOUR_OF_DAY, 9);
				    		calendar.set(Calendar.MINUTE, 0);
				    		calendar.add(Calendar.DAY_OF_MONTH, -2);
				    		firstReminderDate = calendar.getTime();
				    		
				    		calendar.add(Calendar.DAY_OF_MONTH, 2);
				    		secondReminderDate = calendar.getTime();
				    		
				    		smsService.createNewOutboundSms(cellNumber, "Pursukoon Zindagi say khush amdeed. Aap ka appointment "+dateFormat.format(appDate) +" ko hai aur ID number "+mhId+" hai. Hum say Raabta karnay kay liye 03352839588 par ruju farmayen", firstReminderDate, Priority.MEDIUM, 24, PeriodType.HOUR, 1, null);
				    		smsService.createNewOutboundSms(cellNumber, "Pursukoon Zindagi say khush amdeed. Aap ka appointment aaj hai aur ID number "+mhId+" hai. Hum say Raabta karnay kay liye 03352839588 par ruju farmayen", secondReminderDate, Priority.MEDIUM, 24, PeriodType.HOUR, 1, null);
							
				    		smsService.createNewOutboundSms("03311384842", mhId+" has appontment at "+dateFormat.format(appDate) /*+" on time "+ timeFormat.format(appDate)*/, firstReminderDate, Priority.MEDIUM, 24, PeriodType.HOUR, 1, null);
				    		smsService.createNewOutboundSms("03311384842", mhId+" has appontment today "+dateFormat.format(appDate), secondReminderDate, Priority.MEDIUM, 24, PeriodType.HOUR, 1, null);
							
				    		
				    		tarseelService.commitTransaction();
				    		System.out.println("\n\n\nCreated outbounds\n\n\n");
				    	} catch (ParseException e) {
				    		e.printStackTrace();
				    	} finally{
				    		tarseelService.closeSession();
				    	}
				    	} else {
				    		System.out.println("No Contact number found for patient personID: "+p.getPersonId()+"\nDate:"+new Date());
				    	}
				    	
				    } catch (Exception e) {

				      e.printStackTrace();
				      // return null;

				    }
		 
				
			
			
		
	}
	
	
	public synchronized void sendPostOld (final JSONObject data) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				
					
				    try {
				      
				      String url = "http://localhost:8080/mentalhealthsms/TransactionsHandler";
				      
						HttpClient client = new DefaultHttpClient();
						HttpPost post = new HttpPost(url);
				 
						// add header
						post.setHeader("User-Agent", "Mozilla/5.0");
				 
						List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
						urlParameters.add(new BasicNameValuePair("data", data.toJSONString()));
				 
						post.setEntity(new UrlEncodedFormEntity(urlParameters));
				 
						HttpResponse response = client.execute(post);
						System.out.println("\nSending 'POST' request to URL : " + url);
						System.out.println("Post parameters : " + post.getEntity());
						System.out.println("Response Code : " + 
				                                    response.getStatusLine().getStatusCode());
				 
						BufferedReader rd = new BufferedReader(
				                        new InputStreamReader(response.getEntity().getContent()));
				 
						StringBuffer result = new StringBuffer();
						String line = "";
						while ((line = rd.readLine()) != null) {
							result.append(line);
						}
				 
						System.out.println(result.toString());
				    } catch (Exception e) {

				      e.printStackTrace();
				      // return null;

				    }
		 
				
			
			}
		}).start();
	}
	
	public static Map<Object, Object> convertEntrySetToMap (Set<Entry<Object, Object>> entrySet) {
		Map<Object, Object> mapFromSet = new HashMap<Object, Object> ();
		for (Entry<Object, Object> entry : entrySet)
		{
			mapFromSet.put (entry.getKey (), entry.getValue ());
		}
		return mapFromSet;
	}
}
