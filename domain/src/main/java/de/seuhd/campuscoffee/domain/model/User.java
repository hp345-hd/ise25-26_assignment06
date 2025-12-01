package de.seuhd.campuscoffee.domain.model;

import lombok.Builder;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain record that stores the User metadata.
 * This is an immutable value object - use the builder or toBuilder() to create modified copies.
 * Records provide automatic implementations of equals(), hashCode(), toString(), and accessors.
 * <p>
 * We validate the fields in the API layer based on the DTOs, so no validation annotations are needed here.
 *
 * @param id           the unique identifier; null when the User has not been created yet
 * @param createdAt    creation timestamp (LocalDateTime, automatically set upon creation)
 * @param updatedAt    update timestamp (LocalDateTime, automatically set upon creation and update)
 * @param loginName    login name (String, must only contain valid word characters, see Pattern class)
 * @param emailAddress email address (String, must be a valid email address)
 * @param firstName    first name (String, at least one and at most 255 characters)
 * @param lastName     last name (String, at least one and at most 255 characters)
 */
@Builder(toBuilder = true)
public record User(
        @Nullable Long id,
        @Nullable LocalDateTime createdAt,
        @Nullable LocalDateTime updatedAt,
        @NonNull String loginName,
        @NonNull String emailAddress,
        @NonNull String firstName,
        @NonNull String lastName
) implements Serializable { // serializable to allow cloning (see TestFixtures class).
    @Serial
    private static final long serialVersionUID = 1L;
}
