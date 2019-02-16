package com.geoImage.dao;

import org.hibernate.Session;

import com.geoImage.dao.IBaseHibernateDAO;


/**
 * Data access object (DAO) for domain model
 * @author MyEclipse Persistence Tools
 */
public class BaseHibernateDao implements IBaseHibernateDAO {
	
	public Session getSession() {
		//FIXME: Implement this method
		return HibernateSessionFactory.getSession();
	}
	
}