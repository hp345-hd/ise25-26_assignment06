package de.seuhd.campuscoffee.api.controller;

import de.seuhd.campuscoffee.api.exceptions.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import de.seuhd.campuscoffee.api.dtos.UserDto;
import de.seuhd.campuscoffee.api.mapper.UserDtoMapper;
import de.seuhd.campuscoffee.domain.ports.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

import static de.seuhd.campuscoffee.api.util.ControllerUtils.getLocation;

@Tag(name = "Users", description = "Operations related to user management.")
@Controller
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    @Operation(
            summary = "Get all USER.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(type = "array", implementation = UserDto.class)
                            ),
                            description = "All USER as a JSON array."
                    )
            }
    )
    @GetMapping("")
    public ResponseEntity<List<UserDto>> getAll() {

        return ResponseEntity.ok(
                userService.getAll().stream()
                        .map(userDtoMapper::fromDomain)
                        .toList()
        );
    }

    @Operation(
            summary = "Get USER by ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            ),
                            description = "The USER with the provided ID as a JSON object."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            ),
                            description = "No USER with the provided ID could be found."
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                userDtoMapper.fromDomain(userService.getById(id))
        );
    }

    @Operation(
            summary = "Get USER by name.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            ),
                            description = "The USER with the provided name as a JSON object."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            ),
                            description = "No USER with the provided name could be found."
                    )
            }
    )
    @GetMapping("/filter")
    public ResponseEntity<UserDto> filter(
            @RequestParam("name") String name) {

        return ResponseEntity.ok(
                userDtoMapper.fromDomain(userService.getByName(name))
        );
    }

    @Operation(
            summary = "Create a new USER.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            ),
                            description = "The new USER as a JSON object."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            ),
                            description = "Validation failed (e.g., invalid email address, login name, or bean validation errors)."
                    )
            }
    )
    @PostMapping("")
    public ResponseEntity<UserDto> create(
            @RequestBody @Valid UserDto userDto) {

        UserDto created = upsert(userDto);
        return ResponseEntity
                .created(getLocation(created.id()))
                .body(created);
    }

    @Operation(
            summary = "Update an existing USER by ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class)
                            ),
                            description = "The updated USER as a JSON object."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            ),
                            description = "Validation failed: IDs in path and body do not match, invalid email address, login name, or bean validation errors."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            ),
                            description = "No USER with the provided ID could be found."
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UserDto userDto) {

        if (!id.equals(userDto.id())) {
            throw new IllegalArgumentException("USER ID in path and body do not match.");
        }
        return ResponseEntity.ok(
                upsert(userDto)
        );
    }

    @Operation(
            summary = "Delete a USER by ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "The USER was successfully deleted."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            ),
                            description = "No USER with the provided ID could be found."
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {
        userService.delete(id); // throws NotFoundException if no USER with the provided ID exists
        return ResponseEntity.noContent().build();
    }

    /**
     * Common upsert logic for create and update.
     *
     * @param userDto the USER DTO to map and upsert
     * @return the upserted USER mapped back to the DTO format.
     */
    private UserDto upsert(UserDto userDto) {
        return userDtoMapper.fromDomain(
                userService.upsert(
                        userDtoMapper.toDomain(userDto)
                )
        );
    }
}
