package de.seuhd.campuscoffee.domain.impl;

import de.seuhd.campuscoffee.domain.model.User;
import de.seuhd.campuscoffee.domain.ports.UserService;
import de.seuhd.campuscoffee.domain.ports.UserDataService;
import de.seuhd.campuscoffee.domain.exceptions.DuplicationException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of the USER service that handles business logic related to USER entities.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDataService userDataService;

    @Override
    public void clear() {
        log.warn("Clearing all USER data");
        userDataService.clear();
    }

    @Override
    public @NonNull List<User> getAll() {
        log.debug("Retrieving all USER");
        return userDataService.getAll();
    }

    @Override
    public @NonNull User getById(@NonNull Long id) {
        log.debug("Retrieving USER with ID: {}", id);
        return userDataService.getById(id);
    }

    @Override
    public @NonNull User getByName(@NonNull String name) {
        log.debug("Retrieving USER with name: {}", name);
        return userDataService.getByLoginName(name);
    }

    @Override
    public @NonNull User upsert(@NonNull User user) {
        if (user.id() == null) {
            // create a new USER
            log.info("Creating new USER: {}", user.loginName());
        } else {
            // update an existing USER
            log.info("Updating USER with ID: {}", user.id());
            // USER ID must be set
            Objects.requireNonNull(user.id());
            // USER must exist in the database before the update
            userDataService.getById(user.id());
        }
        return performUpsert(user);
    }

    @Override
    public void delete(@NonNull Long id) {
        log.info("Trying to delete USER with ID: {}", id);
        userDataService.delete(id);
        log.info("Deleted USER with ID: {}", id);
    }

    /**
     * Performs the actual upsert operation with consistent error handling and logging.
     * Database constraint enforces name uniqueness - data layer will throw DuplicateEntityException if violated.
     * JPA lifecycle callbacks (@PrePersist/@PreUpdate) set timestamps automatically.
     *
     * @param user the USER to upsert
     * @return the persisted USER with updated ID and timestamps
     * @throws DuplicationException if a USER with the same name already exists
     */
    private @NonNull User performUpsert(@NonNull User user) {
        try {
            User upsertedUser = userDataService.upsert(user);
            log.info("Successfully upserted USER with ID: {}", upsertedUser.id());
            return upsertedUser;
        } catch (DuplicationException e) {
            log.error("Error upserting USER '{}': {}", user.loginName(), e.getMessage());
            throw e;
        }
    }
}
