package com.ihsinformatics.cad4tb.utils;

import java.util.HashMap;
import java.util.Map;

public class DataProviderUtil {

	public static final Map<String,String> questions = new HashMap<String,String>();
	
	public static Map<String, String> createMap()
    {
		questions.put("cur_date", "Today's date");
		questions.put("sid", "Study identifier");
		questions.put("prtcpt_dob", "Participant's date of birth");
		questions.put("tb_treated", "Are you currently being treated with medication for TB?");
		questions.put("specific_tbDrug", "Are you taking any of the following drugs: * Myrin-P, or * Rimstar Forte; or are you taking a medicine that makes your urine a dark orange colour?");
		questions.put("tbtreated_past", "Have you ever been treated for TB in the past?");
		questions.put("years_treated", "(If yes,) how many years ago was that?");
		questions.put("", "Select each symptom that the participant has");
		questions.put("score", "Participant score");
		questions.put("cough", "Cough?");
		questions.put("days_cough", "How many days have you had this cough?");
		questions.put("cough_past", "If not coughing TODAY, in the past month have you been coughing");
		questions.put("weight_loss", "Weight loss?");
		questions.put("night_sweats", "Night sweats?");
		questions.put("hemoptysis", "Hemoptysis (any time in the last 6 months)?");
		questions.put("fever", "Fever?");
		questions.put("tb_diagnose_sleep", "Has someone in the house been diagnosed or treated for TB in the past 2 years?");
		questions.put("consent_given", "Consent form signed?");
		questions.put("mr_no", "MR Number");
		questions.put("pgname", "Participant’s Father/Husband’s Name");
		questions.put("pfname", "PARTICIPANT’S GIVEN NAME");
		questions.put("participant_number", "Participant’s telephone number");
		
		questions.put("prtcpt_gen", "Participant’s gender");
		questions.put("contact_name", "NAME OF CONTACT");
		questions.put("contact_number", "PHONE NUMBER");
		questions.put("str_time", "Interview Start time");
		questions.put("", "Do you have any of the following medical conditions?");
		questions.put("diabetes", "Diabetes?");
		questions.put("hiv_aid", "HIV/AIDS?");
		questions.put("asthama", "Asthma?");
		questions.put("bronchitis", "Bronchitis?");
		questions.put("oth_condition", "Any other condition? Please specify");
		questions.put("smoke_tobacco", "Do you smoke tobacco?");
		questions.put("smoking_age", "How old were you when you started smoking?");
		questions.put("", "How do you smoke tobacco?");
		questions.put("cigarettes", "Cigarettes per day?");
		questions.put("beedi", "Beedi per day?");
		questions.put("pipe", "Pipe per day?");
		questions.put("cigar", "Cigar per day?");
		questions.put("hookah", "Hookah per day?");
		questions.put("oth_form_tobacco", "Do you take any other form of tobacco?");
		questions.put("oth_tobacco", "Any other form of tobacco? Specify type");
		questions.put("mtime_day", "Aap din mien kitni baar tambako noshi kartay hien");
		questions.put("current_occup", "What is your current occupation?");
		questions.put("years_occup", "How many years have you held that occupation?");
		questions.put("oth_occup", "Did you have any other occupation prior to this one?");
		questions.put("", "If yes, please list the last 3 occupations prior to the current one.");
		
		questions.put("pr_occup1", "(Prior occupation 1) What occupation was it?");
		questions.put("years_pr_occup1", "How many years did you hold that occupation?");
		questions.put("pr_occup2", "(Prior occupation 2) What occupation was it?");
		questions.put("years_pr_occup2", "How many years did you hold that occupation?");
		questions.put("proccup3", "(Prior occupation 3) What occupation was it?");
		questions.put("years_pr_occup3", "How many years did you hold that occupation?");
		questions.put("cook_fuel", "What kind of cooking fuel do you use at home?");
		questions.put("oth_fuel", "Specify other type of cooking fuel used at home");
		questions.put("prtcpt_height", "Measure participant's HEIGHT in cm");
		questions.put("prtcpt_weight", "Measure participant's WEIGHT in kg");
		questions.put("fstick_glucose", "Measure and record FINGER-STICK GLUCOSE");
		questions.put("method1", "METHOD");
		questions.put("t_collected1", "TIME Collected");
		questions.put("d_collected1", "Date collected");
		questions.put("induct_success", "Induction successful?");
		questions.put("adverse_event", "Adverse Event?");
		return null;
    }
}
