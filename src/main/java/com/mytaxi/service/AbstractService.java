package com.mytaxi.service;

import com.mytaxi.exception.ConstraintsViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
    protected T findById(ID id) throws EntityNotFoundException{
        return getJpaRepository()
                    .findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Could not find entity with id: " + id));
    }

    protected List<T> findAll() {
        return getJpaRepository().findAll();

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

    protected void deleteById(ID id) {
        try {
            getJpaRepository().deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Could not find entity with id: " + id);
        }
    }

    protected abstract JpaRepository<T, ID> getJpaRepository();
    protected abstract JpaSpecificationExecutor<T> getTJpaSpecificationExecutor(); //Use it for Pageables
}
