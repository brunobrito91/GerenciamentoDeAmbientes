package com.dao;

import java.util.List;
import java.util.Map;

public interface GenericDAO<T> {
	void save(T entity);

	void delete(Object id, Class<T> classe);

	T update(T entity);

	T find(Object entityID);

	List<T> findAll();

	T findOneResult(String namedQuery, Map<String, Object> parameters);
	
	List<T> findResult(String namedQuery, Map<String, Object> parameters);

}
