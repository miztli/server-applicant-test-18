package com.mytaxi.service;

import com.mytaxi.exception.ConstraintsViolationException;
import com.mytaxi.exception.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * This abstracts class helps us to implement all basic CRUD methods
 * for all entities
 * @param <T> The type of the entity object
 * @param <ID> The id of the entity
 */
public abstract class AbstractService<T, ID> {

    /**
     * Find an entoty by id.
     * @param id
     * @throws EntityNotFoundException if no entity found with the provided id
     * @return The entity found
     */
    protected T findById(ID id) throws EntityNotFoundException {
        return getJpaRepository()
                    .findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + id));
    }

    protected T findOne(Specification<T> spec) throws EntityNotFoundException {
        return getJpaSpecificationExecutor()
                .findOne(spec)
                .orElseThrow(() -> new EntityNotFoundException("Could not find entity with provided specification"));
    }

    /**
     * Find all T entities
     * @return The list of found T entities
     */
    protected List<T> findAll() throws EntityNotFoundException {
        return getJpaRepository().findAll();
    }

    /**
     * Find all T entities
     * @return The list of found T entities
     */
    protected List<T> findAll(Specification<T> spec) throws EntityNotFoundException {
        List<T> resultList = getJpaSpecificationExecutor().findAll(spec);
        if (resultList.isEmpty()) { throw new EntityNotFoundException("Could not find results with the provided specification"); }
        return resultList;
    }

    protected T save(T entity) throws ConstraintsViolationException {
        T savedEntity;
        try {
            savedEntity = getJpaRepository().save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new ConstraintsViolationException(e.getMessage());
        }
        return savedEntity;
    }

    protected void delete(T entity) {
        getJpaRepository().delete(entity);
    }

    protected void deleteById(ID id) throws EntityNotFoundException {
        try {
            getJpaRepository().deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Could not find entity with id: " + id);
        }
    }

    protected abstract JpaRepository<T, ID> getJpaRepository();
    protected abstract JpaSpecificationExecutor<T> getJpaSpecificationExecutor(); //Use it for Pageables
}
