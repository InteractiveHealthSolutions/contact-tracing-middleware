package com.ihsinformatics.cad4tb.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ihsinformatics.cad4tb.model.Form;
import com.ihsinformatics.cad4tb.utils.HibernateUtil;

public class FormDAO {

	public static boolean saveForm(Form form)
	{
		boolean allSaved = false;
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(form);
			tx.commit();
			allSaved = true;
		}
		catch (Exception e)
		{
			tx.rollback();
			// TODO Auto-generated catch block
			e.printStackTrace();
			return allSaved;
		}
		finally{
			if(session.isOpen())
				session.close();
		}

		return allSaved;
	}
	
	public static Form getFormById(Integer id){
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			Form form = (Form)session.createQuery("from Form where id = "+id).setMaxResults(1).uniqueResult();
			return form;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally{
			if(session.isOpen())
				session.close();
		}
	}

	public static Boolean updateFormSubmitted(Integer id){
		Session session = null;
		Transaction tx = null;
		try
		{
			session = HibernateUtil.getSessionFactory().openSession();
			tx = session.beginTransaction();
			Form form = (Form)session.createQuery("from Form where id = "+id).setMaxResults(1).uniqueResult();
			form.setSubmitted(true);
			session.update(form);
			tx.commit();
			return true;
		}
		catch (Exception e)
		{
			tx.rollback();
			e.printStackTrace();
			return false;
		}
		finally{
			if(session.isOpen())
				session.close();
		}
	}
}
