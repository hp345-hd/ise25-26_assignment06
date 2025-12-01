package de.seuhd.campuscoffee.api.mapper;

import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.domain.model.User;
import org.mapstruct.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

/**
 * MapStruct mapper for converting between domain model objects and DTOs.
 * This mapper handles the translation between the {@link User} domain model and the
 * {@link UserDto}.
 * <p>
 * This is part of the API layer adapter in the hexagonal architecture, enabling the
 * domain layer to remain independent of API concerns.
 */
@Mapper(componentModel = "spring")
@ConditionalOnMissingBean // prevent IntelliJ warning about duplicate beans
public interface UserDtoMapper {
    UserDto fromDomain(User source);
    User toDomain(UserDto source);
}