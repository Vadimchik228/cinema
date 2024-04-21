package com.vasche.repository;

import com.vasche.exception.RepositoryException;

import java.util.List;
import java.util.Optional;

public interface Repository<K, E> {

    E save(E entity) throws RepositoryException;

    Optional<E> findById(K id) throws RepositoryException;

    List<E> findAll() throws RepositoryException;

    void update(E entity) throws RepositoryException;

    boolean delete(K id) throws RepositoryException;
}
