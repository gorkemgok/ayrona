package com.ayronasystems.core.dao.mysql;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by gorkemgok on 22/02/15.
 */
public interface Dao<E> {

    Dao<E> setSession (Session session);

    Dao<E> beginTransaction ();

    void commit ();

    void commitAndClose ();

    void rollback ();

    void rollbackAndClose ();

    void close ();

    Transaction getTransaction ();


    Dao<E> save (E entity);

    Dao<E> delete (E entity);

    Dao<E> update (E entity);

    E findById (int id);

    List<E> findAll ();


    Dao<E> find ();

    Dao<E> findCount ();

    Dao<E> find (String... fields);

    Dao<E> eq (String field, Object value);

    Dao<E> lt (String field, Object value);

    Dao<E> gt (String field, Object value);

    Dao<E> orderBy (String field, boolean isAsc);

    Dao<E> orderBy (String field);

    List<E> list ();

    List<E> list (int limit);

    List<E> list (int limit, int offset);

    List<E> list (LimitOffset limitOffset);

    PaginatedResult<E> listPaginated (int limit, int offset);

    PaginatedResult<E> listPaginated (LimitOffset limitOffset);

    long count ();

    E get ();

    Session getSession ();
}
