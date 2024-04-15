package com.vasche.dao;

import com.vasche.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {

    E save(E entity) throws DaoException;

    Optional<E> findById(K id) throws DaoException;

    List<E> findAll() throws DaoException;

    void update(E entity) throws DaoException;

    boolean delete(K id) throws DaoException;
}
