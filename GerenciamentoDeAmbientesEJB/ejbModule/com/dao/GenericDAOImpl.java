package com.dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

public abstract class GenericDAOImpl<T> implements GenericDAO<T> {
	private final static String UNIT_NAME = "CrudGerenciamentoDeAmbientesPU";

	@PersistenceContext(unitName = UNIT_NAME)
	protected EntityManager em;

	private Class<T> entityClass;

	public GenericDAOImpl(Class<T> entityClass) {
		System.out.println("GenericDAOImpl");
		this.entityClass = entityClass;
	}

	public void save(T entity) {
		System.out.println("GenericDAOImpl - save " + entity.toString());
		em.persist(entity);
	}

	public void delete(Object id, Class<T> classe) {
		System.out.println("GenericDAOImpl - delete " + classe.getName());
		T entityToBeRemoved = em.getReference(classe, id);

		em.remove(entityToBeRemoved);
	}

	public T update(T entity) {
		System.out.println("GenericDAOImpl - merge " + entity.toString());
		return em.merge(entity);
	}

	public T find(Object entityID) throws RuntimeException {
		System.out.println("GenericDAOImpl - find " + entityID);
		T t = null;
		try {
			t = em.find(entityClass, entityID);
		} catch (RuntimeException e) {
			throw e;
		}
		return t;
	}

	// Using the unchecked because JPA does not have a
	// em.getCriteriaBuilder().createQuery()<T> method
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findAll() {
		System.out.println("GenericDAOImpl - findAll ");
		CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		return em.createQuery(cq).getResultList();
	}

	// Using the unchecked because JPA does not have a
	// ery.getSingleResult()<T> method
	@SuppressWarnings("unchecked")
	public T findOneResult(String namedQuery, Map<String, Object> parameters) {
		System.out.println("GenericDAOImpl - findOneResult ");
		T result = null;

		try {
			Query query = em.createNamedQuery(namedQuery);

			// Method that will populate parameters if they are passed not null
			// and empty
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}

			result = (T) query.getSingleResult();

		} catch (Exception e) {
			System.out.println("Error while running query: " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<T> findResult(String namedQuery, Map<String, Object> parameters) {
		System.out.println("GenericDAOImpl - findResult ");
		List<T> result = null;

		try {
			Query query = em.createNamedQuery(namedQuery);

			// Method that will populate parameters if they are passed not null
			// and empty
			if (parameters != null && !parameters.isEmpty()) {
				populateQueryParameters(query, parameters);
			}

			result = (List<T>) query.getResultList();

		} catch (Exception e) {
			System.out.println("Error while running query: " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}

	private void populateQueryParameters(Query query, Map<String, Object> parameters) {

		for (Entry<String, Object> entry : parameters.entrySet()) {
			query.setParameter(entry.getKey(), entry.getValue());
		}
	}
}
