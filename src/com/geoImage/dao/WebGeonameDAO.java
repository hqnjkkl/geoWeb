package com.geoImage.dao;

import java.util.List;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * WebGeoname entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.geoImage.dao.WebGeoname
 * @author MyEclipse Persistence Tools
 */
public class WebGeonameDAO extends BaseHibernateDao {
	private static final Logger log = LoggerFactory
			.getLogger(WebGeonameDAO.class);
	// property constants
	public static final String WEB_GEONAME_CONTENT = "webGeonameContent";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String OCCURENCE = "occurence";

	public void save(WebGeoname transientInstance) {
		log.debug("saving WebGeoname instance");
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(WebGeoname persistentInstance) {
		log.debug("deleting WebGeoname instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public WebGeoname findById(java.lang.Integer id) {
		log.debug("getting WebGeoname instance with id: " + id);
		try {
			WebGeoname instance = (WebGeoname) getSession().get(
					"com.geoImage.dao.WebGeoname", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(WebGeoname instance) {
		log.debug("finding WebGeoname instance by example");
		try {
			List results = getSession()
					.createCriteria("com.geoImage.dao.WebGeoname")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding WebGeoname instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from WebGeoname as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByWebGeonameContent(Object webGeonameContent) {
		return findByProperty(WEB_GEONAME_CONTENT, webGeonameContent);
	}

	public List findByLatitude(Object latitude) {
		return findByProperty(LATITUDE, latitude);
	}

	public List findByLongitude(Object longitude) {
		return findByProperty(LONGITUDE, longitude);
	}

	public List findByOccurence(Object occurence) {
		return findByProperty(OCCURENCE, occurence);
	}

	public List findAll() {
		log.debug("finding all WebGeoname instances");
		try {
			String queryString = "from WebGeoname";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public WebGeoname merge(WebGeoname detachedInstance) {
		log.debug("merging WebGeoname instance");
		try {
			WebGeoname result = (WebGeoname) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(WebGeoname instance) {
		log.debug("attaching dirty WebGeoname instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(WebGeoname instance) {
		log.debug("attaching clean WebGeoname instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}