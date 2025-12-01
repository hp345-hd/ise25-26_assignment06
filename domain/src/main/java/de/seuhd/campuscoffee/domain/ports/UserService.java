package de.seuhd.campuscoffee.domain.ports;

import de.seuhd.campuscoffee.domain.exceptions.DuplicationException;
import de.seuhd.campuscoffee.domain.exceptions.NotFoundException;
import de.seuhd.campuscoffee.domain.model.User;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Service interface for USER operations.
 * This interface defines the core business logic operations for managing USER.
 * This is a port in the hexagonal architecture pattern, implemented by the domain layer
 * and consumed by the API layer. It encapsulates business rules and orchestrates
 * data operations through the {@link UserDataService} port.
 */
public interface UserService {
    /**
     * Clears all USER data.
     * This operation removes all USER from the system.
     * Warning: This is a destructive operation typically used only for testing
     * or administrative purposes. Use with caution in production environments.
     */
    void clear();

    /**
     * Retrieves all USER in the system.
     *
     * @return a list of all USER entities; never null, but may be empty if no USERs exist
     */
    @NonNull List<User> getAll();

    /**
     * Retrieves a specific User by its unique identifier.
     *
     * @param id the unique identifier of the USER to retrieve; must not be null
     * @return the USER entity with the specified ID; never null
     * @throws NotFoundException if no USER exists with the given ID
     */
    @NonNull User getById(@NonNull Long id);

    /**
     * Retrieves a specific USER by its unique name.
     *
     * @param name the unique name of the USER to retrieve; must not be null
     * @return the USER entity with the specified name; never null
     * @throws NotFoundException if no USER exists with the given name
     */
    @NonNull User getByName(@NonNull String name);

    /**
     * Creates a new USER or updates an existing one.
     * This method performs an "upsert" operation:
     * <ul>
     *   <li>If the USER has no ID (null), a new USER is created</li>
     *   <li>If the USER has an ID, and it exists, the existing USER is updated</li>
     * </ul>
     * <p>
     * Business rules enforced:
     * <ul>
     *   <li>USER names must be unique (enforced by database constraint)</li>
     *   <li>All required fields must be present and valid</li>
     *   <li>Timestamps (createdAt, updatedAt) are managed by the {@link UserDataService}.</li>
     * </ul>
     *
     * @param user the USER entity to create or update; must not be null
     * @return the persisted USER entity with populated ID and timestamps; never null
     * @throws NotFoundException if attempting to update a USER that does not exist
     * @throws DuplicationException if a USER with the same name already exists
     */
    @NonNull User upsert(@NonNull User user);

    /**
     * Deletes a USER by its unique identifier.
     *
     * @param id the unique identifier of the USER to delete; must not be null
     * @throws NotFoundException if no USER exists with the given ID
     */
    void delete(@NonNull Long id);
}
