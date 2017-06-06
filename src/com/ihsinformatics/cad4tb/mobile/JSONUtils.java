package com.ihsinformatics.cad4tb.mobile;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONUtils {

	private static JSONUtils instance;
	private JSONUtils() {
	}
	
	public static JSONUtils getInstance() {
		if(instance == null) {
			instance = new JSONUtils();
		}
		
		return instance;
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
	
	//!!!
	public JSONArray getParamArrayValue(JSONArray data, String key) {
		return (JSONArray) findJSONObjectInJSONArray(data, key).get(key);
	}
	
	//!!!
	public String getValue(JSONObject data, String key) {
		String param;
		try {
			param = findValueinJSONObject(data, key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			param = null;
		}
		if(param != null && !param.isEmpty()) {
			return param;
		}
		return null;
	}
	
	public String findValueinJSONObject(JSONObject data, String key) throws JSONException {
		JSONObject object;
		if(data.containsKey(key)){
			return (String) ((JSONObject) data.get(key)).get("answer");
		}
		else return null;
	}
}
