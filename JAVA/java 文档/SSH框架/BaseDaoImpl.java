package com.tz.hibernate.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.SessionFactory;

import com.tz.hibernate.dao.IBaseDao;
import com.tz.hibernate.utils.HibernateUtils;

public class BaseDaoImpl<T> implements IBaseDao<T>{

	private Class<T> clazz;
	
	protected SessionFactory sessionFactory=HibernateUtils.getSessionFactory();
	
	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		clazz=(Class<T>) parameterizedType.getActualTypeArguments()[0];
	}
	
	@Override
	public void insert(T t) {
		sessionFactory.getCurrentSession().save(t);
	}

	@Override
	public void deleteById(Long id) {
		sessionFactory.getCurrentSession().delete(findById(id));
	}

	@Override
	public void update(T t) {
		sessionFactory.getCurrentSession().update(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findById(Long id) {
		return (T) sessionFactory.getCurrentSession().get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll() {
		return sessionFactory.getCurrentSession().createQuery("from "+clazz.getSimpleName()).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> pagingQuery(int start, int length) {
		return sessionFactory.getCurrentSession().createQuery("from "+clazz.getSimpleName()).setMaxResults(length).setFirstResult(start).list();
	}

}
