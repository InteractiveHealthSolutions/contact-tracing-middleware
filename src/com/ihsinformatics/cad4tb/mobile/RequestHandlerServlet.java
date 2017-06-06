package com.ihsinformatics.cad4tb.mobile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;

import com.ihsinformatics.cad4tb.dao.FormDAO;
import com.ihsinformatics.cad4tb.mobile.service.FormService;
import com.ihsinformatics.cad4tb.model.Form;
import com.ihsinformatics.cad4tb.utils.Logger;

public class RequestHandlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String requestType;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		handleMobileRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		handleMobileRequest(request, response);
	}

	@SuppressWarnings("unchecked")
	private synchronized void handleMobileRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
		FormService formService = FormService.getInstance();

		JSONObject jsonReponse = null;
		Context.openSession();
		try {
			jsonReponse = new JSONObject();
			String stringData = request.getParameter(ParamNames.DATA);
			Logger.log(stringData); // ??? TODO Slow approach, needs to be made better
			Object obj = JSONValue.parse(stringData);
			JSONArray data = (JSONArray) obj;
			String username = JSONUtils.getInstance().getParamValue(data, ParamNames.USERNAME);
			String password = JSONUtils.getInstance().getParamValue(data, ParamNames.PASSWORD);
			
			//!!!
			SecretKey secKey = AES256Endec.getInstance().generateKey();
			String decPassword = AES256Endec.getInstance().decrypt(password, secKey);
			Context.authenticate(username, decPassword.trim());
			
			//!!!
			String user_name="", userSid="";
			user_name = JSONUtils.getInstance().getParamValue(data, ParamNames.USERNAME);
			userSid = JSONUtils.getInstance().getParamValue(data, "sid");
			
			this.requestType = JSONUtils.getInstance().getParamValue(data, "ENCONTER_TYPE");
			if (this.requestType.equals(ParamNames.ENCOUNTER_TYPE_MEDICAL_QUESTIONAIRE)) {
				System.out.println("Registering patient");
				String sid = JSONUtils.getInstance().getParamValue(data, "sid");
				String mrNumber = JSONUtils.getInstance().getParamValue(data, "mr_no");
				Patient patient = FormService.findPatient(sid);
				
				if (patient == null) {
					if (mrNumber != null) {
						patient = FormService.findPatient(mrNumber);
						if (patient == null) {
//							formService.createPatient(username, data, this.requestType);
							Form form = new Form(this.requestType,data.toJSONString(), userSid, user_name, false);
							FormDAO.saveForm(form);
							jsonReponse.put(ParamNames.SERVER_RESPONSE, "success: Patient registered successfully, please note patient ID \"" + sid + "\"");
						} else {
							jsonReponse.put(ParamNames.SERVER_RESPONSE, "Error: Duplicate MR Number");
						}
					} else {
						Form form = new Form(this.requestType,data.toJSONString(), userSid, user_name, false);
						FormDAO.saveForm(form);
//						formService.createPatient(username, data, this.requestType);
						jsonReponse.put(ParamNames.SERVER_RESPONSE, "success: Patient registered successfully, please note patient ID \"" + sid + "\"");
					}
				} else {
					jsonReponse.put(ParamNames.SERVER_RESPONSE, "Error: Duplicate SID");
				}
			} else if (this.requestType.equals(ParamNames.ENCOUNTER_TYPE_SPUTUM_COLLECTION)) {
//				formService.createScreeningEncounter(username, data);
				Form form = new Form(this.requestType,data.toJSONString(), userSid, user_name, false);
				FormDAO.saveForm(form);
				jsonReponse.put(ParamNames.SERVER_RESPONSE, "success: Screening form saved successfully");
			} else if (this.requestType.equals("FIRST FOLLOWUP")) {
				//formService.createFollowupEncounter(username, data, true, this.requestType);
				jsonReponse.put(ParamNames.SERVER_RESPONSE, "success: First followup form saved successfully");
			} else if (this.requestType.equals("FOLLOWUP")) {
				//formService.createFollowupEncounter(username, data, false, this.requestType);
				jsonReponse.put(ParamNames.SERVER_RESPONSE, "success: Folow-up form saved successfully");
			} else if (this.requestType.equals("INSTANT TERMINATION")) {
				//formService.createTerminationEncounter(username, data, this.requestType);
				jsonReponse.put(ParamNames.SERVER_RESPONSE, "success: Early termination form saved successfully");
			} else if (this.requestType.equals("TERMINATION")) {
				//formService.createTerminationEncounter(username, data, this.requestType);
				jsonReponse.put(ParamNames.SERVER_RESPONSE, "success: Termination form saved successfully");
			} else if (this.requestType.equals(ParamNames.GET_PATIENT_INFO)) {
				String mhId = JSONUtils.getInstance().getParamValue(data, ParamNames.SID);
				if (mhId != null) {
					Patient patient = formService.findPatient(mhId);
					if (patient != null) {
						jsonReponse.put(ParamNames.AGE, patient.getAge());
						jsonReponse.put(ParamNames.GENDER, patient.getGender());
						Set<PersonName> names = patient.getNames();
						PersonName name = (PersonName) names.iterator().next();
						String stringName = name.getGivenName();
						jsonReponse.put(ParamNames.PAT_NAME, stringName);
						jsonReponse.put(ParamNames.SID, mhId);
						jsonReponse.put(ParamNames.SERVER_RESPONSE, "success");
						List<Encounter> encounters = Context.getEncounterService().getEncountersByPatient(patient);
						JSONArray enctrs = new JSONArray();
						for (Encounter e : encounters) {
							JSONObject jObj = new JSONObject();
							if (e.getEncounterType().getName().equals(ParamNames.ENCOUNTER_TYPE_REGISTRATION)) {
								
								jObj.put(ParamNames.SCREENING_SCORE, Context.getAdministrationService().executeSQL("select value_numeric from obs "
										+ "where encounter_id = "+e.getEncounterId()
									+ " and concept_id = 6168;", true).get(0).get(0));
								jObj.put(ParamNames.SCORE_TYPE, ParamNames.SRQ10);
							} else if(e.getEncounterType().getName().equals(ParamNames.ENCOUNTER_TYPE_FOLLOWUP) || e.getEncounterType().getName().equals(ParamNames.ENCOUNTER_TYPE_FIRST_FOLLOWUP)) {
								jObj.put(ParamNames.PAT_COMP, Context.getAdministrationService().executeSQL("select value_text from obs "
										+ "where encounter_id = "+e.getEncounterId()
									+ " and concept_id = 6122;", true).get(0).get(0));
							} else if(e.getEncounterType().getName().equals(ParamNames.ENCOUNTER_TYPE_TERMINATION) || e.getEncounterType().getName().equals(ParamNames.ENCOUNTER_TYPE_INSTANT_TERMINATION)) {
								jObj.put(ParamNames.TERMST, Context.getAdministrationService().executeSQL("select value_text from obs "
										+ "where encounter_id = "+e.getEncounterId()
									+ " and concept_id = 6169 and voided = 0;", true).get(0).get(0));
							}  else if(e.getEncounterType().getName().equals(ParamNames.ENCOUNTER_TYPE_SCREENING)) {
								boolean isSRQ = true;
								List<List<Object>> resultSet = Context.getAdministrationService().executeSQL("select value_numeric from obs "
										+ "where "
											+ "(select value_text from obs "
											+ "where concept_id = 6211 "
											+ "and encounter_id = "+e.getEncounterId()+") = \"SRQ-10\" "
										+ "and  encounter_id = "+e.getEncounterId()+" "
										+ "and concept_id = 6168", true);
								jObj.put(ParamNames.SCORE_TYPE, ParamNames.SRQ10);
								if(resultSet.size()<=0) {
									isSRQ = false;
									resultSet = Context.getAdministrationService().executeSQL("select value_numeric from obs "
											+ "where "
												+ "(select value_text from obs "
												+ "where concept_id = 6211 "
												+ "and encounter_id = "+e.getEncounterId()+") = \"AKUADS\" "
											+ "and  encounter_id = "+e.getEncounterId()+" "
											+ "and concept_id = 6168", true);
									jObj.put(ParamNames.SCORE_TYPE, ParamNames.AKUADS);
								}
								if(resultSet.size()<=0) {
									isSRQ = true;
									resultSet = Context.getAdministrationService().executeSQL("select value_numeric from obs "
											+ "where "
											+ "encounter_id = "+e.getEncounterId()+" "
											+ "and concept_id = 6168", true);
									jObj.put(ParamNames.SCORE_TYPE, ParamNames.SRQ10);
								}
								if(resultSet.size()>0) {
									if(isSRQ) {
										jObj.put(ParamNames.SCREENING_SCORE, resultSet.get(0).get(0));
									} else {
										jObj.put(ParamNames.SCREENING_SCORE, resultSet.get(0).get(0));
									}
								}
							}
							jObj.put(ParamNames.ENCOUNER_NAME, e.getEncounterType().getName());
							jObj.put(ParamNames.DATE_ENTERED, e.getEncounterDatetime().getTime());
							enctrs.add(jObj);
						}
						jsonReponse.put("patient_encounters", enctrs);
					} else {
						jsonReponse.put(ParamNames.SERVER_RESPONSE, "failure: Patient not found");
					}
				} else {
					jsonReponse.put(ParamNames.SERVER_RESPONSE, "failure: Patient Id not specified");
				}
			} else if (this.requestType.equals(ParamNames.FIND_PATIENT)) {
				String id = JSONUtils.getInstance().getParamValue(data, ParamNames.SID);
				
				List<List<Object>> result = Context.getAdministrationService().executeSQL("select identifier from openmrs.patient_identifier "
						+ "where patient_id = "
							+ "(SELECT p.person_id FROM openmrs.person p "
							+ "inner join openmrs.patient_identifier i on p.person_id = i.patient_id "
							+ "where i.identifier = \""+id+"\") "
						+ "and identifier_type = 3;", true);
				
				if (result.size() > 0) {
					jsonReponse.put(ParamNames.SERVER_RESPONSE, "success");
					String mhId = result.get(0).get(0).toString();
					jsonReponse.put(ParamNames.SID, mhId);
				} else {
					jsonReponse.put(ParamNames.SERVER_RESPONSE, "failure: This id is not linked to any patient");
				}
			} else if (this.requestType.equals(ParamNames.VALIDATE_USER)) {
				System.out.println("Logging in");
				formService.validateUser(jsonReponse, data);
			} else {
				jsonReponse.put("  ", Context.getPatientService().getAllPatientIdentifierTypes().toArray());
			}
		} catch (Exception eee) {
			eee.printStackTrace();
			jsonReponse.put(ParamNames.SERVER_RESPONSE, eee.getMessage());
		}

		Context.closeSession();
		response.getWriter().print(jsonReponse.toString());
	}
}
