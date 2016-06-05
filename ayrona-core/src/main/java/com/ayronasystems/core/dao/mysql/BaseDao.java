package com.ayronasystems.core.dao.mysql;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gorkemgok on 22/02/15.
 */
public class BaseDao<E> extends TimestampEntity implements Dao<E>, Serializable {

    private Map<String, Object> dynamicQueryParameters = new HashMap<String, Object>();

    private StringBuilder dynamicWhereStatementBuilder = new StringBuilder();

    private final String fromString;

    private Session session;

    private Transaction transaction;

    private final Class<E> classOfEntity;

    private String selectString = "";

    private String orderByString = "";

    public static <T> BaseDao<T> getInstance(Class<T> entityClass){
        return new BaseDao<T> (entityClass);
    }

    public BaseDao (Class<E> classOfEntity) {
        this.classOfEntity = classOfEntity;
        this.fromString = new StringBuilder().append("from ").append(this.classOfEntity.getName()).append (" as entity").toString();
    }

    public BaseDao<E> setSession (Session session) {
        this.session = session;
        return this;
    }

    public BaseDao<E> beginTransaction(){
        transaction = session.beginTransaction();
        return this;
    }

    public void commit(){
        getTransaction ().commit();
    }

    public void commitAndClose() {
        getTransaction().commit();
        session.close();
    }

    public void rollback (){
        getTransaction().rollback();
    }

    public void rollbackAndClose (){
        getTransaction().rollback();
        session.close();
    }

    public void close(){
        session.close();
    }

    public Transaction getTransaction(){
        return transaction;
    }

    
    public BaseDao<E> save(E entity) {
        session.save(entity);
        return this;
    }

    
    public BaseDao<E> delete(E entity) {
        session.delete(entity);
        return this;
    }

    
    public BaseDao<E> update(E entity) {
        session.update(entity);
        return this;
    }

    
    public E findById(int id) {
        String queryString = new StringBuilder().append(fromString).append(" where id = :id").toString();
        Query query = session.createQuery(queryString);
        query.setParameter("id", id);
        List<E> results = query.list();
        if (results.size() > 0){
            return results.get(0);
        }
        return null;
    }

    
    public List<E> findAll() {
        String queryString = new StringBuilder().append(fromString).toString();
        return session.createQuery(queryString).list();
    }

    
    public Dao<E> find () {
        selectString = "";
        dynamicQueryParameters.clear ();
        dynamicWhereStatementBuilder = new StringBuilder ();
        return this;
    }

    public Dao<E> findCount () {
        return find ("count(id)");
    }


    public Dao<E> find (String... fields) {
        StringBuilder selectFields = new StringBuilder ();
        for (String field : fields){
            if (selectFields.length () > 0){
                selectFields.append (",");
            }
            selectFields.append (field);
        }
        selectString = "select "+selectFields.append (" ").toString ();
        dynamicQueryParameters.clear ();
        dynamicWhereStatementBuilder = new StringBuilder ();
        return this;
    }

    public Dao<E> eq(String field, Object value) {
        String _field = field.trim();
        setParameterString ();
        dynamicWhereStatementBuilder.append(_field).append(" = :").append(_field+dynamicQueryParameters.size ());
        dynamicQueryParameters.put(_field+dynamicQueryParameters.size (), value);
        return this;
    }

    public Dao<E> lt(String field, Object value) {
        String _field = field.trim();
        setParameterString ();
        dynamicWhereStatementBuilder.append(_field).append(" < :").append(_field+dynamicQueryParameters.size ());
        dynamicQueryParameters.put(_field+dynamicQueryParameters.size (), value);
        return this;
    }

    public Dao<E> gt(String field, Object value) {
        String _field = field.trim();
        setParameterString ();
        dynamicWhereStatementBuilder.append(_field).append(" > :").append(_field+dynamicQueryParameters.size ());
        dynamicQueryParameters.put(_field+dynamicQueryParameters.size (), value);
        return this;
    }

    private void setParameterString () {
        if ( dynamicQueryParameters.size() == 0){
            dynamicWhereStatementBuilder.append(" where ");
        }else{
            dynamicWhereStatementBuilder.append(" and ");
        }
    }

    
    public Dao<E> orderBy(String field, boolean isAsc) {
        String _field = field.trim();
        this.orderByString = new StringBuilder (" order by ").append(_field).append(isAsc?" ASC":" DESC").toString ();
        return this;
    }

    
    public Dao<E> orderBy(String field) {
        return orderBy(field ,true);
    }

    
    public List<E> list() {
        StringBuilder queryBuilder = new StringBuilder ();
        queryBuilder.append (selectString).append(fromString).append (dynamicWhereStatementBuilder.toString ()).append (orderByString);
        Query query = session.createQuery(queryBuilder.toString());
        for (Map.Entry<String, Object> entry : dynamicQueryParameters.entrySet ()){
            query.setParameter(entry.getKey (), entry.getValue ());
        }
        return query.list();
    }

    
    public List<E> list (int limit) {
        StringBuilder queryBuilder = new StringBuilder ();
        queryBuilder.append (selectString).append(fromString).append (dynamicWhereStatementBuilder.toString ()).append (orderByString);
        Query query = session.createQuery(queryBuilder.toString());
        for (String key : dynamicQueryParameters.keySet()){
            query.setParameter(key, dynamicQueryParameters.get(key));
        }
        query.setMaxResults (limit);
        return query.list();
    }

    
    public List<E> list (int limit, int offset) {
        StringBuilder queryBuilder = new StringBuilder ();
        queryBuilder.append (selectString).append(fromString).append (dynamicWhereStatementBuilder.toString ()).append (orderByString);
        Query query = session.createQuery(queryBuilder.toString());
        for (String key : dynamicQueryParameters.keySet()){
            query.setParameter(key, dynamicQueryParameters.get(key));
        }
        query.setFirstResult (offset);
        query.setMaxResults (limit);
        return query.list();
    }

    public List<E> list (LimitOffset limitOffset) {
        return list (limitOffset.getLimit (), limitOffset.getOffset ());
    }

    public PaginatedResult<E> listPaginated (int limit, int offset) {
        StringBuilder countQueryBuilder = new StringBuilder ();
        countQueryBuilder.append ("select count(id) ").append(fromString).append (dynamicWhereStatementBuilder.toString ());
        Query countQuery = session.createQuery(countQueryBuilder.toString());

        StringBuilder queryBuilder = new StringBuilder ();
        queryBuilder.append (selectString).append(fromString).append (dynamicWhereStatementBuilder.toString ()).append (orderByString);
        Query query = session.createQuery(queryBuilder.toString());

        for (String key : dynamicQueryParameters.keySet()){
            query.setParameter(key, dynamicQueryParameters.get(key));
            countQuery.setParameter(key, dynamicQueryParameters.get(key));
        }

        query.setFirstResult (offset);
        query.setMaxResults (limit);
        List<E> list = query.list();

        long totalCount = 0;
        List cList = countQuery.list ();
        if (cList.size () > 0){
            totalCount = (Long)cList.get (0);
        }

        return new PaginatedResult<E> (totalCount, list);
    }

    public PaginatedResult<E> listPaginated (LimitOffset limitOffset) {
        return listPaginated (limitOffset.getLimit (), limitOffset.getOffset ());
    }

    public long count () {
        List cList = list ();
        if (cList.size () > 0){
            return (Long)cList.get (0);
        }
        return 0;
    }


    public E get() {
        StringBuilder queryBuilder = new StringBuilder ();
        queryBuilder.append (selectString).append(fromString).append (dynamicWhereStatementBuilder.toString ()).append (orderByString);
        Query query = session.createQuery(queryBuilder.toString());
        for (String key : dynamicQueryParameters.keySet()){
            query.setParameter(key, dynamicQueryParameters.get(key));
        }
        List<E> entities = query.list ();
        if (entities.size () > 0){
            return entities.get (0);
        }
        return null;
    }

    
    public Session getSession() {
        return session;
    }

}
