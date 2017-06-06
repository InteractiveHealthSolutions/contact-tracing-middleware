package com.ihsinformatics.cad4tb.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.ihsinformatics.cad4tb.dao.FormDAO;
import com.ihsinformatics.cad4tb.mobile.AES256Endec;
import com.ihsinformatics.cad4tb.mobile.ParamNames;
import com.ihsinformatics.cad4tb.mobile.mobile_service.FormSaveService;
import com.ihsinformatics.cad4tb.model.Form;
import com.ihsinformatics.cad4tb.utils.DataProviderUtil;
import com.ihsinformatics.cad4tb.utils.HibernateUtil;

@Controller
public class MainController {

	@RequestMapping(value="/test.html",method=RequestMethod.GET)
	public ModelAndView test(){
		System.out.print("************** Hello ***************");
		return new ModelAndView("forms");
	}
	
	@RequestMapping(value="/showFormParams.html",method=RequestMethod.GET)
	public ModelAndView showFormParams(Integer id){
		Form form = FormDAO.getFormById(id);
		return new ModelAndView("test");
	}
	
	@RequestMapping(value="/redirect.html",method=RequestMethod.GET)
	public ModelAndView redirect(){
		System.out.print("************** Test ***************");
		return new ModelAndView("test");
	}
	
	@RequestMapping(value="/getForms.html",method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
	public @ResponseBody String getForms() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Form> forms = session.createQuery("from Form").list();
		Map<String,Object> h = new HashMap<String,Object>();
		h.put("obj", forms);
		Gson gson = new Gson();
		return gson.toJson(forms);
	}
	
	
	@RequestMapping(value="/getFormById.html",method=RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
	public @ResponseBody String getFormById(@RequestParam Integer id) {
		System.out.print(id);
		// GET FORM BY ID
		Form form = FormDAO.getFormById(id);
		String json = form.getForm_json();
		DataProviderUtil.createMap();
		JSONArray jsonArray = null;
		JSONArray obj = new JSONArray();
		JSONObject formParams = new JSONObject();
		
		JSONParser parser = new JSONParser();
		try {
			obj.add(0, form.getForm_type());
			jsonArray = (JSONArray) parser.parse(json);
			obj.add(1,jsonArray);
			for(int i=0; i<jsonArray.size(); i++){
				JSONObject jsonObj = (JSONObject)jsonArray.get(i);
				Iterator<?> it = jsonObj.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry pair = (Map.Entry)it.next();
					String key = (String) pair.getKey();
					String question = DataProviderUtil.questions.get(key);
					JSONObject jObj = new JSONObject();
					if(question == null){
						jObj.put("question", key);
					}else{
						jObj.put("question", question);
					}
					jObj.put("answer", jsonObj.get(key));
					formParams.put(key, jObj);
					obj.add(2,formParams);
				}
			}
			return obj.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value="/submitForm.html", method=RequestMethod.POST)
	public @ResponseBody String saveForm(@RequestBody String obj ) throws JSONException{
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) parser.parse(obj);
		} catch (org.json.simple.parser.ParseException e1) {
			e1.printStackTrace();
		}
		Integer formId = ((Long)((JSONObject)jsonArray.get(0)).get("formId")).intValue();
		JSONObject jsonObj = (JSONObject)jsonArray.get(1);
//		String formType = (String)jsonArray.get(0);
		
		String formType = getFormTypeFromJSON(jsonObj);
		if(formType.equalsIgnoreCase(ParamNames.ENCOUNTER_TYPE_MEDICAL_QUESTIONAIRE)){
			try {
				startContextAndAuthenticate(jsonObj);
				Patient pat = FormSaveService.getInstance().createPatient(null, jsonObj, formType);
				if(pat.getId() > 0){
					Boolean formUpdated = FormDAO.updateFormSubmitted(formId);
				}
				return pat.getId().toString();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}finally{
				Context.closeSession();
			}
		}
		else if(formType.equalsIgnoreCase(ParamNames.ENCOUNTER_TYPE_SPUTUM_COLLECTION)){
			try {
				startContextAndAuthenticate(jsonObj);
				Encounter enc = FormSaveService.getInstance().createScreeningEncounter(null, jsonObj);
				if(enc.getId() > 0){
					Boolean formUpdated = FormDAO.updateFormSubmitted(formId);
				}
				return enc.getId().toString();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}finally{
				Context.closeSession();
			}
		}
		else{
			return null;
		}
		
	}
	
	private void startContextAndAuthenticate(JSONObject jsonObj) throws Exception{
		Context.openSession();
		String username = getUserName(jsonObj);
		String password = getPassword(jsonObj);
		
		//!!!
		SecretKey secKey = AES256Endec.getInstance().generateKey();
		String decPassword = AES256Endec.getInstance().decrypt(password, secKey);
		Context.authenticate(username, decPassword.trim());
	}
	
	private String getFormTypeFromJSON(JSONObject jsonObj) {
		String name=null;
		for(Iterator iterator = jsonObj.keySet().iterator(); iterator.hasNext();) {
		    String key = (String) iterator.next();
		    if(key.equalsIgnoreCase("ENCONTER_TYPE")){
		    	name = (String)((JSONObject) jsonObj.get(key)).get("answer");
		    	break;
		    }
		}
		return name;
	}

	private String getUserName(JSONObject jsonObj){
		String username = null;
		for(Iterator iterator = jsonObj.keySet().iterator(); iterator.hasNext();) {
		    String key = (String) iterator.next();
		    if(key.equalsIgnoreCase(ParamNames.USERNAME)){
		    	username = (String) ((JSONObject) jsonObj.get(key)).get("answer");
		    	break;
		    }
		}
		return username;
	}
	
	private String getPassword(JSONObject jsonObj){
		String password = null;
		for(Iterator iterator = jsonObj.keySet().iterator(); iterator.hasNext();) {
		    String key = (String) iterator.next();
		    if(key.equalsIgnoreCase(ParamNames.PASSWORD)){
		    	password = (String) ((JSONObject) jsonObj.get(key)).get("answer");
		    	break;
		    }
		}
		return password;
	}
	
	public JSONObject findJSONObjectInJSONArray(JSONArray data, String key) {
		JSONObject object;
		for(int i =0; i<data.size(); i++) {
			object = (JSONObject) data.get(i);
			if(object.containsKey(key)) {
				return object;
			}
		}
		
		return null;
	}
	
	public String getParamValue(JSONArray data, String key) {
		JSONObject param = findJSONObjectInJSONArray(data, key);
		if(param!=null && param.containsKey(key)) {
			return param.get(key).toString();
		}
		
		return null;
	}
}
