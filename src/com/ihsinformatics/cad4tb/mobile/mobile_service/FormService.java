package com.ihsinformatics.cad4tb.mobile.mobile_service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openmrs.Encounter;
import org.openmrs.EncounterRole;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.PersonName;
import org.openmrs.User;
import org.openmrs.api.ConceptService;
import org.openmrs.api.LocationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;

import com.ihsinformatics.cad4tb.mobile.AES256Endec;
import com.ihsinformatics.cad4tb.mobile.JSONUtils;
import com.ihsinformatics.cad4tb.mobile.ParamNames;
import com.ihsinformatics.cad4tb.mobile.SMSHelper;
import com.ihsinformatics.cad4tb.mobile.Utils;

public class FormService
{
  private static FormService instance;
  
  SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
  SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss a");
  
  public static FormService getInstance()
  {
    if (instance == null) {
      instance = new FormService();
    }
    return instance;
  }
  
  public Patient createPatient(String username, JSONArray data, String requestType) throws ParseException
  {
    JSONUtils jsonUtils = JSONUtils.getInstance();
    String dateEntered = jsonUtils.getParamValue(data, "cur_date");
    String sid = jsonUtils.getParamValue(data, "sid");
    String mrNumber = jsonUtils.getParamValue(data, "mr_no");
    Date dateFromMobileForm = null;
    String participantFirstName = jsonUtils.getParamValue(data, "pfname");
    String participantGivenName = jsonUtils.getParamValue(data, "pgname");
    String contaactName = jsonUtils.getParamValue(data, "contact_name");
    String participant_number = jsonUtils.getParamValue(data, "participant_number");
    String contactNumber = jsonUtils.getParamValue(data, "contact_number");
    String str_time = jsonUtils.getParamValue(data, "str_time");
    String gender = jsonUtils.getParamValue(data, "prtcpt_gen");
    String age = jsonUtils.getParamValue(data, "prtcpt_dob");
    
    String tb_treated = jsonUtils.getParamValue(data, "tb_treated");
    String tbtreated_past = jsonUtils.getParamValue(data, "tbtreated_past");
    String years_treated = jsonUtils.getParamValue(data, "years_treated");
    String cough = jsonUtils.getParamValue(data, "cough");
    String cough_past = jsonUtils.getParamValue(data, "cough_past");
    String days_cough = jsonUtils.getParamValue(data, "days_cough");
    String specific_tbDrug = jsonUtils.getParamValue(data, "specific_tbDrug");
    String weight_loss = jsonUtils.getParamValue(data, "weight_loss");
    String short_breath = jsonUtils.getParamValue(data, "short_breath");
    String dailyActivities = jsonUtils.getParamValue(data, "DAILY_ACTIVITIES");
    String night_sweats = jsonUtils.getParamValue(data, "night_sweats");
    String hemoptysis = jsonUtils.getParamValue(data, "hemoptysis");
    String sputum = jsonUtils.getParamValue(data, "sputum");
    String fever = jsonUtils.getParamValue(data, "fever");
    String chest_pain = jsonUtils.getParamValue(data, "chest_pain");
    String tb_diagnose_sleep = jsonUtils.getParamValue(data, "tb_diagnose_sleep");
    String tb_diagnose_fmember = jsonUtils.getParamValue(data, "tb_diagnose_fmember");
    String consent_given = jsonUtils.getParamValue(data, "consent_given");
    String diabetes = jsonUtils.getParamValue(data, "diabetes");
    String hiv_aid = jsonUtils.getParamValue(data, "hiv_aid");
    String asthama = jsonUtils.getParamValue(data, "asthama");
    String bronchitis = jsonUtils.getParamValue(data, "bronchitis");
    String oth_condition = jsonUtils.getParamValue(data, "oth_condition");
    String smoke_tobacco = jsonUtils.getParamValue(data, "smoke_tobacco");
    String smoking_age = jsonUtils.getParamValue(data, "smoking_age");
    String cigarettes = jsonUtils.getParamValue(data, "cigarettes");
    String beedi = jsonUtils.getParamValue(data, "beedi");
    String pipe = jsonUtils.getParamValue(data, "pipe");
    String cigar = jsonUtils.getParamValue(data, "cigar");
    String hookah = jsonUtils.getParamValue(data, "hookah");
    String oth_form_tobacco = jsonUtils.getParamValue(data, "oth_form_tobacco");
    String oth_tobacco = jsonUtils.getParamValue(data, "oth_tobacco");
    String mtime_day = jsonUtils.getParamValue(data, "mtime_day");
    String current_occup = jsonUtils.getParamValue(data, "current_occup");
    String years_occup = jsonUtils.getParamValue(data, "years_occup");
    String oth_occup = jsonUtils.getParamValue(data, "oth_occup");
    String pr_occup1 = jsonUtils.getParamValue(data, "pr_occup1");
    String years_pr_occup1 = jsonUtils.getParamValue(data, "years_pr_occup1");
    String pr_occup2 = jsonUtils.getParamValue(data, "pr_occup2");
    String years_pr_occup2 = jsonUtils.getParamValue(data, "years_pr_occup2");
    String proccup3 = jsonUtils.getParamValue(data, "proccup3");
    String years_pr_occup3 = jsonUtils.getParamValue(data, "years_pr_occup3");
    String cook_fuel = jsonUtils.getParamValue(data, "cook_fuel");
    String oth_fuel = jsonUtils.getParamValue(data, "oth_fuel");
    String prtcpt_height = jsonUtils.getParamValue(data, "prtcpt_height");
    String prtcpt_weight = jsonUtils.getParamValue(data, "prtcpt_weight");
    String fstick_glucose = jsonUtils.getParamValue(data, "fstick_glucose");
    
   
    try
    {
      dateFromMobileForm = formatterDateTime.parse(dateEntered);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(dateFromMobileForm);
      calendar.add(Calendar.MINUTE, -5);
      dateFromMobileForm = calendar.getTime();
      
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    Patient patient = new Patient();
    Location location = findOrCreateLocation("Indus Hospital");
    
    PatientIdentifier mhIdentifier = null;
    PatientIdentifier szcIdentifier = null;
    PatientIdentifier indusMRIdentifier = null;
    
    List<PatientIdentifierType> patientIdentifierTypes = Context.getPatientService().getAllPatientIdentifierTypes();
	for (PatientIdentifierType type : patientIdentifierTypes) {
		if (type.getName().equals("SID")) {
			System.out.println("SID received");
			mhIdentifier = new PatientIdentifier(sid, type, location);
			patient.addIdentifier(mhIdentifier);
		}else if ((type.getName().equals("MR"))&&mrNumber!=null) {
			
			System.out.println("MR received");
			szcIdentifier = new PatientIdentifier(mrNumber, type, location);
			patient.addIdentifier(szcIdentifier);
		} 
	}
	// if(mrNumber!=null) {
		mhIdentifier.setPreferred(Boolean.valueOf(true));
	// }
    // Person Address
   /* PersonAddress personAddress = new PersonAddress();
    personAddress.setAddress1(address);
    if (town != null) {
      personAddress.setAddress2(town);
    }
    personAddress.setCityVillage(city);
    patient.addAddress(personAddress);*/
    
    PersonName personName = new PersonName();
    if(participantFirstName != null) {
	    personName.setGivenName(participantFirstName);
	    personName.setFamilyName(participantGivenName);
    } else {
    	if(consent_given==null) {
    		personName.setGivenName("Not");
    	    personName.setFamilyName("Eligible");
    	} else {
    		personName.setGivenName("No");
    	    personName.setFamilyName("Consent");
    	}
    }
    patient.addName(personName);
    
    patient.setBirthdate(formatterDateTime.parse(age));
    // patient.setBirthdateEstimated(Boolean.valueOf(true));
    if(gender!=null) {
	    if (gender.toLowerCase().equals("male")) {
	      gender = "M";
	    } else {
	      gender = "F";
	    }
	    patient.setGender(gender);
    } else {
    	patient.setGender("M");
    }
    
    patient.setDateCreated(dateFromMobileForm);

    PersonService personService = Context.getPersonService();
    /*PersonAttributeType attributeTypeContact = personService.getPersonAttributeTypeByName("Age");
    PersonAttribute ageAttr = new PersonAttribute();
    ageAttr.setAttributeType(attributeTypeContact);
    ageAttr.setValue(age);
    ageAttr.setPerson(patient);
    patient.addAttribute(ageAttr);*/
    
    if(participant_number!=null)
    {
        PersonAttributeType attributeTypeContact = personService.getPersonAttributeTypeByName("Participant Number");
        PersonAttribute personContact = new PersonAttribute();
        personContact.setAttributeType(attributeTypeContact);
        personContact.setValue(participant_number);
        personContact.setPerson(patient);
        patient.addAttribute(personContact);
      }
    if (contaactName != null)
    {
      PersonAttributeType attributeTypeContact = personService.getPersonAttributeTypeByName("Contact Name");
      PersonAttribute personContactName = new PersonAttribute();
      personContactName.setAttributeType(attributeTypeContact);
      personContactName.setValue(contaactName);
      personContactName.setPerson(patient);
      patient.addAttribute(personContactName);
    }
    if (contactNumber != null)
    {
      PersonAttributeType attributeTypeContact = personService.getPersonAttributeTypeByName("Contact");
      PersonAttribute personContact = new PersonAttribute();
      personContact.setAttributeType(attributeTypeContact);
      personContact.setValue(contactNumber);
      personContact.setPerson(patient);
      patient.addAttribute(personContact);
    }
    PatientService patientService = null;
    patientService = Context.getPatientService();
    
    Patient pat = patientService.savePatient(patient);
    
    
    
    Encounter medicalQuestionnaireEncounter = new Encounter();
    medicalQuestionnaireEncounter.setEncounterType(Context.getEncounterService().getEncounterType(requestType));
    medicalQuestionnaireEncounter.setPatient(pat);
    medicalQuestionnaireEncounter.setDateCreated(new Date());
    medicalQuestionnaireEncounter.setEncounterDatetime(dateFromMobileForm);
    medicalQuestionnaireEncounter.setLocation(location);
  //creating user 
    /*User user = Context.getUserService().getUserByUsername(username);
    medicalQuestionnaireEncounter.addProvider(
    		Context.getEncounterService().getEncounterRoleByUuid(EncounterRole.UNKNOWN_ENCOUNTER_ROLE_UUID), 
    		Context.getProviderService().getProvidersByPerson(user.getPerson()).iterator().next());*/ // get the first in collection
    
    ConceptService conceptService = Context.getConceptService();
    Person person = Context.getPersonService().getPerson(pat.getPersonId());
    
    if(tb_treated!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("tb_treated"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(tb_treated));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    
    if(tbtreated_past!=null) {
    	  
	    Obs obstbtreated_past = new Obs(person, conceptService.getConcept("tbtreated_past"), dateFromMobileForm, location);
	    obstbtreated_past.setValueCoded(conceptService.getConcept(tbtreated_past));
	    if(tbtreated_past!=null)
	    medicalQuestionnaireEncounter.addObs(obstbtreated_past);
    }
    
    if(specific_tbDrug!=null) {
	    Obs obstbtreated_past = new Obs(person, conceptService.getConcept("specific_tbDrug"), dateFromMobileForm, location);
	    obstbtreated_past.setValueCoded(conceptService.getConcept(specific_tbDrug));
	    if(tbtreated_past!=null)
	    medicalQuestionnaireEncounter.addObs(obstbtreated_past);
    }
    if(str_time!=null) {
	    Obs obstbtreated_past = new Obs(person, conceptService.getConcept("str_time"), dateFromMobileForm, location);
	    obstbtreated_past.setValueText(str_time);// (/* TODO Should be int*/Double.parseDouble(years_treated));
	    if(str_time!=null)
	    medicalQuestionnaireEncounter.addObs(obstbtreated_past);
    }
    if(years_treated!=null) {
	    Obs obstbtreated_past = new Obs(person, conceptService.getConcept("years_treated"), dateFromMobileForm, location);
	    obstbtreated_past.setValueNumeric(/* TODO Should be int*/Double.parseDouble(years_treated));
	    if(years_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstbtreated_past);
    }
    if(cough!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("cough"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(cough));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(cough_past!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("cough_past"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(cough_past));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(days_cough!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("days_cough"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(/* TODO Should be int*/Double.parseDouble(days_cough));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(weight_loss!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("weight_loss"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(weight_loss));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(short_breath!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("short_breath"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(short_breath));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(night_sweats!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("night_sweats"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(night_sweats));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(hemoptysis!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("hemoptysis"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(hemoptysis));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(sputum!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("sputum"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(sputum));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(fever!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("fever"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(fever));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(chest_pain!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("chest_pain"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(chest_pain));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(tb_diagnose_sleep!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("tb_diagnose_sleep"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(tb_diagnose_sleep));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(tb_diagnose_fmember!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("tb_diagnose_fmember"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(tb_diagnose_fmember));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(consent_given!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("consent_given"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(consent_given));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(diabetes!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("diabetes"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(diabetes));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(hiv_aid!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("hiv_aid"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(hiv_aid));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(asthama!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("asthama"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(asthama));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(bronchitis!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("bronchitis"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(bronchitis));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(oth_condition!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("oth_condition"), dateFromMobileForm, location);
	    obstb_treated.setValueText(oth_condition);
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(smoke_tobacco!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("smoke_tobacco"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(smoke_tobacco));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(smoking_age!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("smoking_age"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(/* TODO Should be int*/Double.parseDouble(smoking_age));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(cigarettes!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("cigarettes"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(/* TODO Should be int*/Double.parseDouble(cigarettes));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(beedi!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("beedi"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(/* TODO Should be int*/Double.parseDouble(beedi));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(pipe!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("pipe"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(/* TODO Should be int*/Double.parseDouble(pipe));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(cigar!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("cigar"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(/* TODO Should be int*/Double.parseDouble(cigar));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(hookah!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("hookah"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(/* TODO Should be int*/Double.parseDouble(hookah));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(oth_form_tobacco!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("oth_form_tobacco"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(oth_form_tobacco));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(oth_tobacco!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("oth_tobacco"), dateFromMobileForm, location);
	    obstb_treated.setValueText(oth_tobacco);
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(mtime_day!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("mtime_day"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(/* TODO Should be int*/Double.parseDouble(mtime_day));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(current_occup!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("current_occup"), dateFromMobileForm, location);
	    obstb_treated.setValueText(current_occup);
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(years_occup!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("years_occup"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(/* TODO Should be int*/Double.parseDouble(years_occup));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(oth_occup!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("oth_occup"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(oth_occup));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(pr_occup1!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("pr_occup1"), dateFromMobileForm, location);
	    obstb_treated.setValueText(pr_occup1);
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(years_pr_occup1!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("years_pr_occup1"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(/* TODO Should be int*/Double.parseDouble(years_pr_occup1));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(pr_occup2!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("pr_occup2"), dateFromMobileForm, location);
	    obstb_treated.setValueText((pr_occup2));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(years_pr_occup2!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("years_pr_occup2"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(/* TODO Should be int*/Double.parseDouble(years_pr_occup2));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(proccup3!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("proccup3"), dateFromMobileForm, location);
	    obstb_treated.setValueText(proccup3);
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(years_pr_occup3!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("years_pr_occup3"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(/* TODO Should be int*/Double.parseDouble(years_pr_occup3));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(cook_fuel!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("cook_fuel"), dateFromMobileForm, location);
	    obstb_treated.setValueCoded(conceptService.getConcept(cook_fuel));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(oth_fuel!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("oth_fuel"), dateFromMobileForm, location);
	    obstb_treated.setValueText(oth_fuel);
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(prtcpt_height!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("prtcpt_height"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(/* TODO Should be int*/Double.parseDouble(prtcpt_height));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(prtcpt_weight!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("prtcpt_weight"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(Double.parseDouble(prtcpt_weight));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    if(fstick_glucose!=null) {
	    Obs obstb_treated = new Obs(person, conceptService.getConcept("fstick_glucose"), dateFromMobileForm, location);
	    obstb_treated.setValueNumeric(Double.parseDouble(fstick_glucose));
	    if(obstb_treated!=null)
	    medicalQuestionnaireEncounter.addObs(obstb_treated);
    }
    Context.getEncounterService().saveEncounter(medicalQuestionnaireEncounter);
    
    return pat;
  }
  
  public Encounter createScreeningEncounter(String username, JSONArray data) throws ParseException
  {
    JSONUtils jsonUtils = JSONUtils.getInstance();
    //String dateEntred = jsonUtils.getParamValue(data, "DATE_ENTERED");
    Date date = null;
    String sid = jsonUtils.getParamValue(data, "PATIENT_MH_ID");
    String method1 = jsonUtils.getParamValue(data, "method1");
    String t_collected1 = jsonUtils.getParamValue(data, "t_collected1");
    String d_collected1 = jsonUtils.getParamValue(data, "d_collected1");
    String induct_success = jsonUtils.getParamValue(data, "induct_success");
    String adverse_event = jsonUtils.getParamValue(data, "adverse_event");
    
    Location location = findOrCreateLocation("Default location");
    Patient pat = findPatient(sid);
    date = new Date();
    Encounter screeningEncounter = new Encounter();
  //creating user 
    /*User user = Context.getUserService().getUserByUsername(username);
    screeningEncounter.addProvider(
    		Context.getEncounterService().getEncounterRoleByUuid(EncounterRole.UNKNOWN_ENCOUNTER_ROLE_UUID), 
    		Context.getProviderService().getProvidersByPerson(user.getPerson()).iterator().next());*/ // get the first in collection
    screeningEncounter.setEncounterType(Context.getEncounterService().getEncounterType(ParamNames.ENCOUNTER_TYPE_SPUTUM_COLLECTION));
    screeningEncounter.setPatient(pat);
    screeningEncounter.setDateCreated(new Date());
    screeningEncounter.setEncounterDatetime(date);
    screeningEncounter.setLocation(location);
    
    ConceptService conceptService = Context.getConceptService();
    Person person = Context.getPersonService().getPerson(pat.getPersonId());
    if(method1!=null) {
        Obs obsCry = new Obs(person, conceptService.getConcept("method1"), date, location);
        obsCry.setValueCoded(conceptService.getConcept(method1));
        screeningEncounter.addObs(obsCry);
    }
    if(t_collected1!=null) {
        Obs obsDailyActivities = new Obs(person, conceptService.getConcept("t_collected1"), date, location);
        obsDailyActivities.setValueText(t_collected1.toUpperCase());
        screeningEncounter.addObs(obsDailyActivities);
    }
    if(d_collected1!=null) {
        Obs obsDecision = new Obs(person, conceptService.getConcept("d_collected1"), date, location);
        obsDecision.setValueTime(formatterDateTime.parse(d_collected1));
        screeningEncounter.addObs(obsDecision);
    }
    if(induct_success!=null) {
        Obs obsDecision = new Obs(person, conceptService.getConcept("induct_success"), date, location);
        obsDecision.setValueCoded(conceptService.getConcept(induct_success));
        screeningEncounter.addObs(obsDecision);
    }
    if(adverse_event!=null) {
        Obs obsDecision = new Obs(person, conceptService.getConcept("adverse_event"), date, location);
        obsDecision.setValueCoded(conceptService.getConcept(adverse_event));
        screeningEncounter.addObs(obsDecision);
    }
    Encounter e = Context.getEncounterService().saveEncounter(screeningEncounter);
    
    return e;
  }
  
  
  public Date findDOBFromAge(int age)
  {
	  
    String toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    String dob = toDate.substring(4, 19);
    int currentYear = Integer.parseInt(toDate.substring(0, 4));
    dob = currentYear - age + dob;
    try
    {
      return Utils.openMrsDateFormat.parse(dob);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  public static Patient findPatient(String patientId)
  {
    //List<PatientIdentifierType> typeList = new ArrayList();
    PatientService patientService = Context.getPatientService();
    // typeList.add(patientService.getPatientIdentifierTypeByName("SZC ID"));
    List<Patient> patients = Context.getPatientService().getPatients(null, patientId, patientService.getAllPatientIdentifierTypes(), false);
    if (patients.size() > 0) {
      return (Patient)patients.get(0);
    }
    return null;
  }
  
  public Location findOrCreateLocation(String location)
  {
    LocationService locationService = Context.getLocationService();
    Location oLocation = locationService.getLocation(location);
    if (oLocation == null)
    {
      oLocation = new Location();
      oLocation.setName(location);
      oLocation.setDateCreated(new Date());
      locationService.saveLocation(oLocation);
    }
    return oLocation;
  }
  
  public void validateUser(JSONObject jsonReponse, JSONArray data)
  {
    try
    {    	
      String username = JSONUtils.getInstance().getParamValue(data, "USERNAME");
      String password = JSONUtils.getInstance().getParamValue(data, "PASSWORD");
      //!!!
      SecretKey secKey = AES256Endec.getInstance().generateKey();
      String decPassword = AES256Endec.getInstance().decrypt(password, secKey);
      Context.authenticate(username, decPassword);
      jsonReponse.put("result", "success: Logedin");
    }
    catch (Exception e)
    {
      jsonReponse.put("result", "failure: Invalid Username or Password");
    }
  }

  public void getPatientData(String username, JSONArray data) {
	  JSONUtils jsonUtils = JSONUtils.getInstance();
	    String dateEntred = jsonUtils.getParamValue(data, "DATE_ENTERED");
	    Date date = null;
	    String patientMHId = jsonUtils.getParamValue(data, "PATIENT_MH_ID");
  }
}
