package com.tradevan.apcommon.persistence;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.internal.SessionImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tradevan.apcommon.bean.PageResult;

/**
 * Title: JpaGenericRepository<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.9.10
 */
@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public abstract class JpaGenericRepository<T extends GenericEntity<ID>, ID extends Serializable> implements GenericRepository<T, ID> {

	@PersistenceContext
	protected EntityManager entityManager;

	protected Class<T> type;
	
	@SuppressWarnings("unchecked")
	public JpaGenericRepository() {
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		this.type = ((Class<T>) pt.getActualTypeArguments()[0]);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public void create(T entity) {
		entityManager.persist(entity);
	}
	
	@Override
	public void detach(T entity) {
		entityManager.detach(entity);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public T update(T entity) {
		return (T) entityManager.merge(entity);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public T save(T entity) {
		if (entity.getId() == null) {
			create(entity);
		}
		else {
			entity = update(entity);
		}
		return entity;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	@Override
	public T saveInNewTx(T entity) {
		return save(entity);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public T checkThenSave(T entity) {
		if (! exists(entity.getId())) {
			create(entity);
		}
		else {
			entity = update(entity);
		}
		return entity;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public void saveInBatch(Collection<T> entities, int batchSize) {
		int count = 0;
		for (T entity : entities) {
			save(entity);
			count++;
			if (count % batchSize == 0) {
				flushAndClear();
			}
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public int updateByJPQL(String jpql, Object... params) {
		return createQuery(jpql, params).executeUpdate();
	}
	
	protected Query createQuery(String jpql, Object... params) {
		Query query = entityManager.createQuery(jpql);
		for (int i = 0; i < params.length; i++) {
			query = query.setParameter(i + 1, params[i]);
		}
		return query;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public int updateByNamedQuery(String namedQuery, Object... params) {
		Query query = entityManager.createNamedQuery(namedQuery);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.executeUpdate();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public int updateBySQL(String sql, Object... params) {
		Query query = entityManager.createNativeQuery(sql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.executeUpdate();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public int updateBySQL_map(String sql, Map<String, Object> params) {
		Query query = entityManager.createNativeQuery(sql);
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> entry = it.next();
	        query.setParameter(entry.getKey(), entry.getValue());
	    }
		return query.executeUpdate();
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	@Override
	public int updateBySQLInNewTx(String sql, Object... params) {
		return updateBySQL(sql, params);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public void delete(T entity) {
		entityManager.remove(entityManager.merge(entity));
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public boolean delete(ID id) {
		T entity = entityManager.getReference(this.type, id);
		if (entity != null) {
			entityManager.remove(entity);
			return true;
		}
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public int deleteByProperty(String name, Object value) {
		return deleteByProperties(new String[] { name }, new Object[] { value });
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public int deleteByProperties(String[] nameArray, Object[] valueArray) {
		Query query = entityManager.createQuery(generateSimpleQL("DELETE", this.type.getSimpleName(), null, null, nameArray, null, null, null, false));
		for (int i = 0; i < valueArray.length; i++) {
			query.setParameter(i + 1, valueArray[i]);
		}
		return query.executeUpdate();
	}
	
	protected String generateSimpleQL(String queryCmd, String tableName, String[] joinProps, String[] leftJoinProps, 
			String[] nameArray, String inName, String memberName, String[] orderByArray, boolean isFetch) {
		StringBuilder queryLang = new StringBuilder(queryCmd);
		queryLang.append(" FROM ").append(tableName).append(" o ");
		queryLang.append(generateJoinFetctEntities(joinProps, false, isFetch));
		queryLang.append(generateJoinFetctEntities(leftJoinProps, true, isFetch));
		int i = 0;
		if (nameArray != null) {
			for (; i < nameArray.length; i++) {
				if (i == 0) {
					queryLang.append("WHERE ");
				}
				else {
					queryLang.append("AND ");
				}
				String name = nameArray[i];
				if (name.endsWith("!=")) {
					queryLang.append("o.").append(name.substring(0, name.length() - 2)).append("<> ?").append(i + 1).append(" ");
				}
				else if (name.endsWith(">")) {
					queryLang.append("o.").append(name.substring(0, name.length() - 1)).append("> ?").append(i + 1).append(" ");
				}
				else if (name.endsWith("<")) {
					queryLang.append("o.").append(name.substring(0, name.length() - 1)).append("< ?").append(i + 1).append(" ");
				}
				else if (name.endsWith(">=")) {
					queryLang.append("o.").append(name.substring(0, name.length() - 2)).append(">= ?").append(i + 1).append(" ");
				}
				else if (name.endsWith("<=")) {
					queryLang.append("o.").append(name.substring(0, name.length() - 2)).append("<= ?").append(i + 1).append(" ");
				}
				else if (name.endsWith("%")) {
					queryLang.append("o.").append(name.substring(0, name.length() - 1)).append(" LIKE ?").append(i + 1).append(" ");
				}
				else if (name.endsWith("!%")) {
					queryLang.append("o.").append(name.substring(0, name.length() - 2)).append(" NOT LIKE ?").append(i + 1).append(" ");
				}
				else {
					queryLang.append("o.").append(name).append(" = ?").append(i + 1).append(" ");
				}
			}
		}
		if (inName != null) {
			if (i == 0) {
				queryLang.append("WHERE ");
			}
			else {
				queryLang.append("AND ");
			}
			if (inName.endsWith("!")) {
				queryLang.append("o.").append(inName.substring(0, inName.length() - 1)).append(" not in ?").append(i + 1).append(" ");
			}
			else {
				queryLang.append("o.").append(inName).append(" in ?").append(i + 1).append(" ");
			}
			i++;
		}
		if (memberName != null) {
			if (i == 0) {
				queryLang.append("WHERE ");
			}
			else {
				queryLang.append("AND ");
			}
			if (memberName.endsWith("!")) {
				queryLang.append("?").append(i + 1).append(" not member of o.").append(memberName.substring(0, memberName.length() - 1)).append(" ");
			}
			else {
				queryLang.append("?").append(i + 1).append(" member of o.").append(memberName).append(" ");
			}
			i++;
		}
		queryLang.append(generateOrderClause(orderByArray));
		
		return queryLang.toString();
	}
	
	private String generateJoinFetctEntities(String[] joinProps, boolean isLeft, boolean isFetch) {
		StringBuilder buf = new StringBuilder("");
		if (joinProps != null && joinProps.length > 0) {
			for (String entity : joinProps) {
				if (isLeft) {
					buf.append("left ");
				}
				buf.append("join ");
				if (isFetch) {
					buf.append("fetch ");
				}
				buf.append("o.").append(entity).append(" ");
			}
		}
		return buf.toString();
	}
	
	private String generateOrderClause(String[] orderByArray) {
		StringBuilder buf = new StringBuilder("");
		if (orderByArray != null && orderByArray.length > 0) {
			for (int i = 0; i < orderByArray.length; i++) {
				if (orderByArray[i] == null) {
					break;
				}
				else if (i == 0) {
					buf.append("ORDER BY ");
				}
				else {
					buf.append(", ");
				}
				buf.append("o.").append(orderByArray[i]);
			}
		}
		return buf.toString();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public int deleteByJPQL(String jpql, Object... params) {
		return updateByJPQL(jpql, params);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	@Override
	public int deleteBySQL(String sql, Object... params) {
		return updateBySQL(sql, params);
	}
	
	@Override
	public T getEntityById(ID id) {
		return entityManager.find(this.type, id);
	}
	
	@Override
    public T getEntityReferenceById(ID id) {
		return entityManager.getReference(this.type, id);
    }
	
	@Override
	public T getEntityByProperty(String name, Object value) {
		return getEntityByProperties(new String[] { name }, new Object[] { value });
	}
	
	@Override
	public T getEntityByProperty(String name, Object value, String[] joinProps, String[] leftJoinProps) {
		return getEntityByProperties(new String[] { name }, new Object[] { value }, joinProps, leftJoinProps);
	}
	
	@Override
	public T getEntityByProperties(String[] nameArray, Object[] valueArray) {
		return returnFirstEntityOrNull(findEntityListByProperties(nameArray, valueArray, null, null, null));
	}
	
	@Override
	public T getEntityByProperties(String[] nameArray, Object[] valueArray, String[] joinProps, String[] leftJoinProps) {
		return returnFirstEntityOrNull(findEntityListByProperties(nameArray, valueArray, null, joinProps, leftJoinProps));
	}
	
	protected T returnFirstEntityOrNull(List<T> list) {
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public T getEntityByJPQL(String jpql, Object... params) {
		return returnFirstEntityOrNull(findEntityListByJPQL(jpql, params));
	}
	
	@Override
	public T getEntityBySQL(String sql, Object... params) {
		return returnFirstEntityOrNull(findEntityListBySQL(sql, params));
	}
	
	@Override
	public Object getByProperty(String[] fieldArray, String name, Object value) {
		return getByProperties(fieldArray, new String[] { name }, new Object[] { value }, null);
	}
	
	@Override
	public Object getByProperty(String[] fieldArray, String name, Object value, String joinProp) {
		return getByProperties(fieldArray, new String[] { name }, new Object[] { value }, joinProp != null ? new String[] { joinProp } : null);
	}
	
	@Override
	public Object getByProperties(String[] fieldArray, String[] nameArray, Object[] valueArray) {
		return returnFirstPropertyOrNull(findByProperties(fieldArray, nameArray, valueArray, null, null));
	}
	
	@Override
	public Object getByProperties(String[] fieldArray, String[] nameArray, Object[] valueArray, String[] joinProps) {
		return returnFirstPropertyOrNull(findByProperties(fieldArray, nameArray, valueArray, null, joinProps));
	}
	
	@Override
	public Object getByPropertyAndInValueList(String[] fieldArray, String name, Object value, String inName, List<?> valueList) {
		return returnFirstPropertyOrNull(findByPropertyAndInValueList(fieldArray, name, value, inName, valueList, null));
	}
	
	@Override
	public Object getByPropertiesAndInValueList(String[] fieldArray, String[] nameArray, Object[] valueArray, String inName, List<?> valueList) {
		return returnFirstPropertyOrNull(findByPropertiesAndInValueList(fieldArray, nameArray, valueArray, inName, valueList, null));
	}
	
	@Override
	public Object getByPropertyAndMemberOf(String[] fieldArray, String name, Object value, String memberName, Object member) {
		return returnFirstPropertyOrNull(findByPropertyAndMemberOf(fieldArray, name, value, memberName, member, null));
	}
	
	@Override
	public Object getByPropertiesAndMemberOf(String[] fieldArray, String[] nameArray, Object[] valueArray, String memberName, Object member) {
		return returnFirstPropertyOrNull(findByPropertiesAndMemberOf(fieldArray, nameArray, valueArray, memberName, member, null));
	}
	
	@SuppressWarnings("rawtypes")
	protected Object returnFirstPropertyOrNull(List list) {
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public Object getByJPQL(String jpql, Object... params) {
		return returnFirstPropertyOrNull(findByJPQL(jpql, params));
	}
	
	@Override
	public Object getByNamedQuery(String namedQuery, Object... params) {
		return returnFirstPropertyOrNull(findByNamedQuery(namedQuery, params));
	}
	
	@Override
	public Object getBySQL(String sql, Object... params) {
		return returnFirstPropertyOrNull(findBySQL(sql, params));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getMapBySQL(String sql, Object... params) {
		return (Map<String, Object>) returnFirstPropertyOrNull(findListMapBySQL(sql, params));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getMapBySQL_map(String sql, Map<String, Object> params) {
		return (Map<String, Object>) returnFirstPropertyOrNull(findListMapBySQL_map(sql, params));
	}
	
	@Override
	public boolean exists(ID id) {
		return (countByProperty("id", id) > 0) ? true : false;
	}
	
	@Override
	public Integer convert2Integer(Object result, Integer defaultVal) {
		if (result instanceof Integer) {
			return (Integer) result; 
		}
		else if (result instanceof BigDecimal) {
			return ((BigDecimal) result).intValue(); 
		}
		else if (result instanceof BigInteger) {
			return ((BigInteger) result).intValue();
		}
		else if (result instanceof Short) {
			return ((Short) result).intValue();
		}
		else if (result instanceof Long) {
			return ((Long) result).intValue();
		}
		else if (result instanceof String){
			return Integer.valueOf((String) result);
		}
		return defaultVal;
	}
	
	@Override
	public Long convert2Long(Object result) {
		if (result instanceof Long) {
			return (Long) result; 
		}
		else if (result instanceof BigDecimal) {
			return ((BigDecimal) result).longValue(); 
		}
		else if (result instanceof BigInteger) {
			return ((BigInteger) result).longValue();
		}
		else if (result instanceof Short) {
			return ((Short) result).longValue();
		}
		else if (result instanceof Integer) {
			return ((Integer) result).longValue();
		}
		else if (result instanceof String){
			return Long.valueOf((String) result);
		}
		else {
			return null;
		}
	}
	
	@Override
	public Long countByProperty(String name, Object value) {
		return countByProperties(new String[] { name }, new Object[] { value }, null);
	}
	
	@Override
	public Long countByProperty(String name, Object value, String joinProp) {
		return countByProperties(new String[] { name }, new Object[] { value }, joinProp != null ? new String[] { joinProp } : null);
	}
	
	@Override
	public Long countByProperties(String[] nameArray, Object[] valueArray) {
		return countByProperties(nameArray, valueArray, null);
	}
	
	@Override
	public Long countByProperties(String[] nameArray, Object[] valueArray, String[] joinProps) {
		Query query = entityManager.createQuery(generateSimpleQL("SELECT COUNT(o.id)", this.type.getSimpleName(), 
				joinProps != null ? joinProps : null, null, nameArray, null, null, null, false));
		for (int i = 0; i < valueArray.length; i++) {
			query.setParameter(i + 1, valueArray[i]);
		}
		return convert2Long(query.getSingleResult());
	}
	
	@Override
	public Long countByPropertyInValueList(String inName, List<?> valueList) {
		Query query = entityManager.createQuery(generateSimpleQL("SELECT COUNT(o.id)", this.type.getSimpleName(), null, null, null, inName, null, null, false));
		query.setParameter(1, valueList);
		return convert2Long(query.getSingleResult());
	}
	
	@Override
	public Long countByPropertyAndInValueList(String name, Object value, String inName, List<?> valueList) {
		Query query = entityManager.createQuery(generateSimpleQL("SELECT COUNT(o.id)", this.type.getSimpleName(), null, null, name != null ? new String[] { name } : null, 
				inName, null, null, false));
		int i = 1;
		if (name != null) {
			query.setParameter(i++, value);
		}
		query.setParameter(i, valueList);
		return convert2Long(query.getSingleResult());
	}
	
	@Override
	public Long countByPropertiesAndInValueList(String[] nameArray, Object[] valueArray, String inName, List<?> valueList) {
		Query query = entityManager.createQuery(generateSimpleQL("SELECT COUNT(o.id)", this.type.getSimpleName(), null, null, nameArray, inName, null, null, false));
		int i = 0;
		if (nameArray != null && valueArray != null) {
			for (; i < valueArray.length; i++) {
				query.setParameter(i + 1, valueArray[i]);
			}
		}
		query.setParameter(i + 1, valueList);
		return convert2Long(query.getSingleResult());
	}
	
	@Override
	public Long countByPropertyAndMemberOf(String name, Object value, String memberName, Object member) {
		Query query = entityManager.createQuery(generateSimpleQL("SELECT COUNT(o.id)", this.type.getSimpleName(), null, null, name != null ? new String[] { name } : null, 
				null, memberName, null, false));
		int i = 1;
		if (name != null) {
			query.setParameter(i++, value);
		}
		query.setParameter(i, member);
		return convert2Long(query.getSingleResult());
	}
	
	@Override
	public Long countByPropertiesAndMemberOf(String[] nameArray, Object[] valueArray, String memberName, Object member) {
		Query query = entityManager.createQuery(generateSimpleQL("SELECT COUNT(o.id)", this.type.getSimpleName(), null, null, nameArray, null, memberName, null, false));
		int i = 0;
		if (nameArray != null && valueArray != null) {
			for (; i < valueArray.length; i++) {
				query.setParameter(i + 1, valueArray[i]);
			}
		}
		query.setParameter(i + 1, member);
		return convert2Long(query.getSingleResult());
	}
	
	@Override
	public Long countEntityByJPQL(String jpql, Object... params) {
		StringBuffer queryString = new StringBuffer("SELECT COUNT(o) FROM ");
		queryString.append(this.type.getSimpleName()).append(" o ");
		queryString.append("where o in (");
		queryString.append(jpql).append(")");
		
		TypedQuery<Long> query = entityManager.createQuery(queryString.toString(), Long.class);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return convert2Long(query.getSingleResult());
	}
	
	@Override
	public Long countEntityByJPQL_map(String jpql, Map<String, Object> params) {
		StringBuffer queryString = new StringBuffer("SELECT COUNT(o) FROM ");
		queryString.append(this.type.getSimpleName()).append(" o ");
		queryString.append("where o in (");
		queryString.append(jpql).append(")");
		
		TypedQuery<Long> query = entityManager.createQuery(queryString.toString(), Long.class);
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> entry = it.next();
	        query.setParameter(entry.getKey(), entry.getValue());
	    }
		return convert2Long(query.getSingleResult());
	}
	
	@Override
	public Long countEntityByNamedQuery(String namedQuery, Object... params) {
		TypedQuery<T> typedQuery = entityManager.createNamedQuery(namedQuery, this.type);
		StringBuffer queryStr = new StringBuffer("SELECT COUNT(o) FROM ");
		queryStr.append(this.type.getSimpleName()).append(" o ");
		queryStr.append("WHERE o IN (");
		queryStr.append(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
		queryStr.append(")");
		
		TypedQuery<Long> query = entityManager.createQuery(queryStr.toString(), Long.class);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return convert2Long(query.getSingleResult());
	}
	
	@Override
	public Long countEntityByNamedQuery_map(String namedQuery, Map<String, Object> params) {
		TypedQuery<T> typedQuery = entityManager.createNamedQuery(namedQuery, this.type);
		StringBuffer queryStr = new StringBuffer("SELECT COUNT(o) FROM ");
		queryStr.append(this.type.getSimpleName()).append(" o ");
		queryStr.append("WHERE o IN (");
		queryStr.append(typedQuery.unwrap(org.hibernate.Query.class).getQueryString());
		queryStr.append(")");
		
		TypedQuery<Long> query = entityManager.createQuery(queryStr.toString(), Long.class);
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> entry = it.next();
	        query.setParameter(entry.getKey(), entry.getValue());
	    }
		return convert2Long(query.getSingleResult());
	}
	
	@Override
	public Long countBySQL(String sql, Object... params) {
		StringBuffer queryString = new StringBuffer("SELECT COUNT('a') FROM (");
		queryString.append(sql).append(") x");
		Query query = entityManager.createNativeQuery(queryString.toString());
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return convert2Long(query.getSingleResult());
	}
	
	@Override
	public Long countBySQL_map(String sql, Map<String, Object> params) {
		StringBuffer queryString = new StringBuffer("SELECT COUNT('a') FROM (");
		queryString.append(sql).append(") x");
		Query query = entityManager.createNativeQuery(queryString.toString());
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> entry = it.next();
	        query.setParameter(entry.getKey(), entry.getValue());
	    }
		return convert2Long(query.getSingleResult());
	}
	
	@Override
    public Long countAll() {
        CriteriaQuery<Long> query = entityManager.getCriteriaBuilder().createQuery(Long.class);
        query.select(entityManager.getCriteriaBuilder().count(query.from(this.type)));
        return entityManager.createQuery(query).getSingleResult();
    }
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findByProperty(String[] filedArray, String name, Object value, String orderBy) {
		return findByProperties(filedArray, name != null ? new String[] { name } : null, value != null ? new Object[] { value } : null, new String[] { orderBy }, null);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findByProperty(String[] filedArray, String name, Object value, String orderBy, String joinProp) {
		return findByProperties(filedArray, name != null ? new String[] { name } : null, value != null ? new Object[] { value } : null, new String[] { orderBy }, 
				joinProp != null ? new String[] { joinProp } : null);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findByProperties(String[] fieldArray, String[] nameArray, Object[] valueArray, String[] orderBy) {
		return findByProperties(fieldArray, nameArray, valueArray, orderBy, null);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findByProperties(String[] fieldArray, String[] nameArray, Object[] valueArray, String[] orderBy, String[] joinProps) {
		Query query = entityManager.createQuery(
				generateSimpleQL(generateSelectClause(fieldArray), this.type.getSimpleName(), 
						joinProps != null ? joinProps : null, null, nameArray, null, null, orderBy, false));
		int i = 0;
		if (nameArray != null && valueArray != null) {
			for (; i < valueArray.length; i++) {
				query.setParameter(i + 1, valueArray[i]);
			}
		}
		return query.getResultList();
	}
	
	private String generateSelectClause(String[] filedArray) {
		StringBuilder buf = new StringBuilder("SELECT ");
		int x = 0;
		for (int i = 0; i < filedArray.length; i++) {
			if (filedArray[i].indexOf("(") == -1 && filedArray[i].indexOf(")") == -1) {
				if (x != 0) {
					buf.append(", ");
				}
				if (i == 0 && filedArray[i].startsWith("*")) {
					buf.append("distinct ");
				}
				buf.append("o.");
				x++;
			}
			buf.append(filedArray[i].startsWith("*") ? filedArray[i].substring(1) : filedArray[i]);
		}
		return buf.toString();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findByPropertyInValueList(String[] fieldArray, String inName, List<?> valueList, String orderBy) {
		Query query = entityManager.createQuery(
				generateSimpleQL(generateSelectClause(fieldArray), this.type.getSimpleName(), null, null, null, inName, null, new String[] { orderBy }, false));
		query.setParameter(1, valueList);
		return query.getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findByPropertyAndInValueList(String[] fieldArray, String name, Object value, String inName, List<?> valueList, String orderBy) {
		Query query = entityManager.createQuery(
				generateSimpleQL(generateSelectClause(fieldArray), this.type.getSimpleName(), null, null, name != null ? new String[] { name } : null, 
						inName, null, new String[] { orderBy }, false));
		int i = 1;
		if (name != null) {
			query.setParameter(i++, value);
		}
		query.setParameter(i, valueList);
		return query.getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findByPropertiesAndInValueList(String[] fieldArray, String[] nameArray, Object[] valueArray, String inName, List<?> valueList, String[] orderByArray) {
		Query query = entityManager.createQuery(
				generateSimpleQL(generateSelectClause(fieldArray), this.type.getSimpleName(), null, null, nameArray, inName, null, orderByArray, false));
		int i = 0;
		if (nameArray != null && valueArray != null) {
			for (; i < valueArray.length; i++) {
				query.setParameter(i + 1, valueArray[i]);
			}
		}
		query.setParameter(i + 1, valueList);
		return query.getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findByPropertyAndMemberOf(String[] fieldArray, String name, Object value, String memberName, Object member, String orderBy) {
		Query query = entityManager.createQuery(
				generateSimpleQL(generateSelectClause(fieldArray), this.type.getSimpleName(), null, null, name != null ? new String[] { name } : null, 
						null, memberName, new String[] { orderBy }, false));
		int i = 1;
		if (name != null) {
			query.setParameter(i++, value);
		}
		query.setParameter(i, member);
		return query.getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findByPropertiesAndMemberOf(String[] fieldArray, String[] nameArray, Object[] valueArray, String memberName, Object member, String[] orderByArray) {
		Query query = entityManager.createQuery(
				generateSimpleQL(generateSelectClause(fieldArray), this.type.getSimpleName(), null, null, nameArray, null, memberName, orderByArray, false));
		int i = 0;
		if (nameArray != null && valueArray != null) {
			for (; i < valueArray.length; i++) {
				query.setParameter(i + 1, valueArray[i]);
			}
		}
		query.setParameter(i + 1, member);
		return query.getResultList();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findByJPQL(String jpql, Object... params) {
		Query query = entityManager.createQuery(jpql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();  
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findByJPQL_map(String jpql, Map<String, Object> params) {
		Query query = entityManager.createQuery(jpql);
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> entry = it.next();
	        query.setParameter(entry.getKey(), entry.getValue());
	    }
		return query.getResultList();  
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findByNamedQuery(String namedQuery, Object... params) {
		Query query = entityManager.createNamedQuery(namedQuery);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();  
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findByNamedQuery_map(String namedQuery, Map<String, Object> params) {
		Query query = entityManager.createNamedQuery(namedQuery);
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> entry = it.next();
	        query.setParameter(entry.getKey(), entry.getValue());
	    }
		return query.getResultList();  
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findBySQL(String sql, Object... params) {
		Query query = entityManager.createNativeQuery(sql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();  
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List findBySQL_map(String sql, Map<String, Object> params) {
		Query query = entityManager.createNativeQuery(sql);
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> entry = it.next();
	        query.setParameter(entry.getKey(), entry.getValue());
	    }
		return query.getResultList();  
	}
	
	@Override
	public List<T> findAllEntityList() {
		return findAllEntityList(null, null, null);
	}
	
	@Override
	public List<T> findAllEntityList(String orderBy, String[] joinProps, String[] leftJoinProps) {
		StringBuffer queryLang = new StringBuffer("SELECT o FROM ");
		queryLang.append(this.type.getSimpleName()).append(" o ");
		queryLang.append(generateJoinFetctEntities(joinProps, false, true));
		queryLang.append(generateJoinFetctEntities(leftJoinProps, true, true));
		if (orderBy != null) {
			queryLang.append("ORDER BY o.");
			queryLang.append(orderBy);
		}
		TypedQuery<T> query = entityManager.createQuery(queryLang.toString(), this.type);
		return query.getResultList();
	}
	
	@Override
	public List<T> findEntityListByProperty(String name, Object value, String orderBy) {
		return findEntityListByProperties(
				new String[] { name }, new Object[] { value }, (orderBy != null) ? new String[] { orderBy } : null, null, null);
	}
	
	@Override
	public List<T> findEntityListByProperty(String name, Object value, String orderBy, String[] joinProps, String[] leftJoinProps) {
		return findEntityListByProperties(
				new String[] { name }, new Object[] { value }, (orderBy != null) ? new String[] { orderBy } : null, joinProps, leftJoinProps);
	}
	
	@Override
	public List<T> findEntityListByProperties(String[] nameArray, Object[] valueArray, String[] orderByArray, String[] joinProps, String[] leftJoinProps) {
		return findEntityListByJPQL(
				generateSimpleQL("SELECT o", this.type.getSimpleName(), joinProps, leftJoinProps, nameArray, null, null, orderByArray, true), valueArray);
	}
	
	@Override
	public List<T> findEntityListInValueList(String inName, List<?> valueList, String[] orderByArray, String[] joinProps, String[] leftJoinProps) {
		StringBuilder queryLang = new StringBuilder("SELECT o FROM ");
		queryLang.append(this.type.getSimpleName()).append(" o ");
		queryLang.append(generateJoinFetctEntities(joinProps, false, true));
		queryLang.append(generateJoinFetctEntities(leftJoinProps, true, true));
		queryLang.append("WHERE o.").append(inName).append(" in ?1 ");
		queryLang.append(generateOrderClause(orderByArray));
		return findEntityListByJPQL(queryLang.toString(), valueList);
	}
	
	@Override
	public List<T> findEntityListByPropertyAndInValueList(String name, Object value, String inName, List<?> valueList, String[] orderByArray, String[] joinProps, String[] leftJoinProps) {
		StringBuilder queryLang = new StringBuilder("SELECT o FROM ");
		queryLang.append(this.type.getSimpleName()).append(" o ");
		queryLang.append(generateJoinFetctEntities(joinProps, false, true));
		queryLang.append(generateJoinFetctEntities(leftJoinProps, true, true));
		queryLang.append("WHERE o.").append(name).append(" = ?1 and o.").append(inName).append(" in ?2 ");
		queryLang.append(generateOrderClause(orderByArray));
		return findEntityListByJPQL(queryLang.toString(), value, valueList);
	}
	
	@Override
	public List<T> findEntityListByJPQL(String jpql, Object... params) {
		return findPagedEntityListByJPQL(jpql, null, null, params);
	}

	@Override
	public List<T> findEntityListByJPQL_map(String jpql, Map<String, Object> params) {
		return findPagedEntityListByJPQL_map(jpql, null, null, params);
	}
	
	@Override
	public List<T> findEntityListByNamedQuery(String namedQuery, Object... params) {
		return findPagedEntityListByNamedQuery(namedQuery, null, null, params);
	}
	
	@Override
	public List<T> findEntityListByNamedQuery_map(String namedQuery, Map<String, Object> params) {
		return findPagedEntityListByNamedQuery_map(namedQuery, null, null, params);
	}
	
	@Override
	public List<T> findEntityListBySQL(String sql, Object... params) {
		return findPagedEntityListBySQL(sql, null, null, params);
	}
	
	@Override
	public List<T> findEntityListBySQL_map(String sql, Map<String, Object> params) {
		return findPagedEntityListBySQL_map(sql, null, null, params);
	}
	
	@Override
	public List<T> findPagedEntityListByJPQL(String jpql, Integer page, Integer pageSize, Object... params) {
		TypedQuery<T> query = entityManager.createQuery(jpql, this.type);
		if ((pageSize != null) && (pageSize.intValue() > 0)) {
			if ((page != null) && (page.intValue() > 1)) {
				query.setFirstResult((page - 1) * pageSize);
			}
			query.setMaxResults(pageSize.intValue());
		}
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}
	
	@Override
	public List<T> findPagedEntityListByJPQL_map(String jpql, Integer page, Integer pageSize, Map<String, Object> params) {
		TypedQuery<T> query = entityManager.createQuery(jpql, this.type);
		if ((pageSize != null) && (pageSize.intValue() > 0)) {
			if ((page != null) && (page.intValue() > 1)) {
				query.setFirstResult((page - 1) * pageSize);
			}
			query.setMaxResults(pageSize.intValue());
		}
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> entry = it.next();
	        query.setParameter(entry.getKey(), entry.getValue());
	    }
		return query.getResultList();
	}
	
	@Override
	public List<T> findPagedEntityListByNamedQuery(String namedQuery, Integer page, Integer pageSize, Object... params) {
		TypedQuery<T> query = entityManager.createNamedQuery(namedQuery, this.type);
		if ((pageSize != null) && (pageSize.intValue() > 0)) {
			if ((page != null) && (page.intValue() > 1)) {
				query.setFirstResult((page - 1) * pageSize);
			}
			query.setMaxResults(pageSize.intValue());
		}
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}
	
	@Override
	public List<T> findPagedEntityListByNamedQuery_map(String namedQuery, Integer page, Integer pageSize, Map<String, Object> params) {
		TypedQuery<T> query = entityManager.createNamedQuery(namedQuery, this.type);
		if ((pageSize != null) && (pageSize.intValue() > 0)) {
			if ((page != null) && (page.intValue() > 1)) {
				query.setFirstResult((page - 1) * pageSize);
			}
			query.setMaxResults(pageSize.intValue());
		}
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> entry = it.next();
	        query.setParameter(entry.getKey(), entry.getValue());
	    }
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findPagedEntityListBySQL(String sql, Integer page, Integer pageSize, Object... params) {
		Query query = entityManager.createNativeQuery(sql, this.type);
		if ((pageSize != null) && (pageSize.intValue() > 0)) {
			if ((page != null) && (page.intValue() > 1)) {
				query.setFirstResult((page - 1) * pageSize);
			}
			query.setMaxResults(pageSize.intValue());
		}
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findPagedEntityListBySQL_map(String sql, Integer page, Integer pageSize, Map<String, Object> params) {
		Query query = entityManager.createNativeQuery(sql, this.type);
		if ((pageSize != null) && (pageSize.intValue() > 0)) {
			if ((page != null) && (page.intValue() > 1)) {
				query.setFirstResult((page - 1) * pageSize);
			}
			query.setMaxResults(pageSize.intValue());
		}
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> entry = it.next();
	        query.setParameter(entry.getKey(), entry.getValue());
	    }
		return query.getResultList();
	}
	
	@Override
	public PageResult findEntityPageResultByJPQL(String jpql, Integer page, Integer pageSize, Object... params) {
		PageResult pageResult = new PageResult();
		pageResult.setTotal(countEntityByJPQL(jpql, params));
		pageResult.setPage(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotalPage((int) Math.ceil((double) pageResult.getTotal() / pageSize));
		pageResult.setData(findPagedEntityListByJPQL(jpql, page, pageSize, params));
		return pageResult;
	}

	@Override
	public PageResult findEntityPageResultByJPQL_map(String jpql, Integer page, Integer pageSize, Map<String, Object> params) {
		PageResult pageResult = new PageResult();
		pageResult.setTotal(countEntityByJPQL_map(jpql, params));
		pageResult.setPage(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotalPage((int) Math.ceil((double) pageResult.getTotal() / pageSize));
		pageResult.setData(findPagedEntityListByJPQL_map(jpql, page, pageSize, params));
		return pageResult;
	}
	
	@Override
	public PageResult findEntityPageResultByNamedQuery(String namedQuery, Integer page, Integer pageSize, Object... params) {
		PageResult pageResult = new PageResult();
		pageResult.setTotal(countEntityByNamedQuery(namedQuery, params));
		pageResult.setPage(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotalPage((int) Math.ceil((double) pageResult.getTotal() / pageSize));
		pageResult.setData(findPagedEntityListByNamedQuery(namedQuery, page, pageSize, params));
		return pageResult;
	}

	@Override
	public PageResult findEntityPageResultByNamedQuery_map(String namedQuery, Integer page, Integer pageSize, Map<String, Object> params) {
		PageResult pageResult = new PageResult();
		pageResult.setTotal(countEntityByNamedQuery_map(namedQuery, params));
		pageResult.setPage(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotalPage((int) Math.ceil((double) pageResult.getTotal() / pageSize));
		pageResult.setData(findPagedEntityListByNamedQuery_map(namedQuery, page, pageSize, params));
		return pageResult;
	}
	
	@Override
	public PageResult findEntityPageResultBySQL(String sql, Integer page, Integer pageSize, Object... params) {
		PageResult pageResult = new PageResult();
		pageResult.setTotal(countBySQL(sql, params));
		pageResult.setPage(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotalPage((int) Math.ceil((double) pageResult.getTotal() / pageSize));
		pageResult.setData(findPagedEntityListBySQL(sql, page, pageSize, params));
		return pageResult;
	}
	
	@Override
	public PageResult findEntityPageResultBySQL_map(String sql, Integer page, Integer pageSize, Map<String, Object> params) {
		PageResult pageResult = new PageResult();
		pageResult.setTotal(countBySQL_map(sql, params));
		pageResult.setPage(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotalPage((int) Math.ceil((double) pageResult.getTotal() / pageSize));
		pageResult.setData(findPagedEntityListBySQL_map(sql, page, pageSize, params));
		return pageResult;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findListMapBySQL(String sql, Object... params) {
		SQLQuery query = getSession().createSQLQuery(sql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i+ 1, params[i]);
		}
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return query.list();  
	}
	
	protected Session getSession() {
		return entityManager.unwrap(Session.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findListMapBySQL_map(String sql, Map<String, Object> params) {
		SQLQuery query = getSession().createSQLQuery(sql);
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> entry = it.next();
	        Object value = entry.getValue();
	        if (value instanceof Collection) {
	        	query.setParameterList(entry.getKey(), (List<String>) value);
	        }
	        else {
	        	query.setParameter(entry.getKey(), value);
	        }
	    }
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return query.list();  
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findPagedListMapBySQL(String sql, Integer page, Integer pageSize, Object... params) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if ((pageSize != null) && (pageSize.intValue() > 0)) {
			if ((page != null) && (page.intValue() > 1)) {
				query.setFirstResult((page - 1) * pageSize);
			}
			query.setMaxResults(pageSize.intValue());
		}
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return query.list();  
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> findPagedListMapBySQL_map(String sql, Integer page, Integer pageSize, Map<String, Object> params) {
		SQLQuery query = getSession().createSQLQuery(sql);
		if ((pageSize != null) && (pageSize.intValue() > 0)) {
			if ((page != null) && (page.intValue() > 1)) {
				query.setFirstResult((page - 1) * pageSize);
			}
			query.setMaxResults(pageSize.intValue());
		}
		Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Object> entry = it.next();
	        Object value = entry.getValue();
	        if (value instanceof Collection) {
	        	query.setParameterList(entry.getKey(), (List<String>) value);
	        }
	        else {
	        	query.setParameter(entry.getKey(), value);
	        }
	    }
		query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
		return query.list();  
	}
	
	@Override
	public PageResult findListMapPageResultBySQL(String sql, Integer page, Integer pageSize, Object... params) {
		PageResult pageResult = new PageResult();
		pageResult.setTotal(countBySQL(sql, params));
		pageResult.setPage(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotalPage((int) Math.ceil((double) pageResult.getTotal() / pageSize));
		pageResult.setData(findPagedListMapBySQL(sql, page, pageSize, params));
		return pageResult;  
	}
	
	@Override
	public PageResult findListMapPageResultBySQL_map(String sql, Integer page, Integer pageSize, Map<String, Object> params) {
		PageResult pageResult = new PageResult();
		pageResult.setTotal(countBySQL_map(sql, params));
		pageResult.setPage(page);
		pageResult.setPageSize(pageSize);
		pageResult.setTotalPage((int) Math.ceil((double) pageResult.getTotal() / pageSize));
		pageResult.setData(findPagedListMapBySQL_map(sql, page, pageSize, params));
		return pageResult;  
	}
	
	@Override
	public CallableStatement createCallableStatement(String sql) throws HibernateException, SQLException {
		return ((SessionImpl) getSession()).connection().prepareCall(sql);
	}
	
	@Override
	public void flush() {
		entityManager.flush();
	}

	@Override
	public void clear() {
		entityManager.clear();
	}
	
	@Override
	public void flushAndClear() {
		flush();
		clear();
	}
}
