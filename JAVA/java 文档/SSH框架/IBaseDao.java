package com.tz.hibernate.dao;

import java.util.List;

public interface IBaseDao<T> {

	void insert(T t);
	
	void deleteById(Long id);
	
	void update(T t);
	
	T findById(Long id);
	
	List<T> getAll();
	
	List<T> pagingQuery(int start,int length);
	
}
