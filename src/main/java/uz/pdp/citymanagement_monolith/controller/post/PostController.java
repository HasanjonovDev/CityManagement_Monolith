package uz.pdp.citymanagement_monolith.controller.post;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.citymanagement_monolith.domain.dto.post.PostCreateDto;
import uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.service.post.PostService;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post/api/v1/")
public class PostController {
    private final PostService postService;

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Search post"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(postService.search(search, filter));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get all posts"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAll(
            @RequestBody(required = false) Filter filter
    ) {
        return ResponseEntity.ok(postService.getAll(filter));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Add new post"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> savePost(
            @RequestBody PostCreateDto postCreateDto,
            Principal principal
    ) {
        return ResponseEntity.ok(postService.save(postCreateDto, principal));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get post by its id"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/get")
    public ResponseEntity<ApiResponse> get(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Get user's post"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get")
    public ResponseEntity<ApiResponse> get(
            Principal principal
    ) {
        return ResponseEntity.ok(postService.get(principal));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Update post"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> update(
            @RequestBody PostCreateDto postCreateDto,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(postService.update(postCreateDto, id));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            headers = @Header(
                    name = "authorization",
                    required = true,
                    description = "Jwt token is required to check if the user has role or permission to access this api"
            ),
            responseCode = "200",
            description = "Delete post"
    )
    @Operation(security = @SecurityRequirement(name = "jwtBearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(postService.delete(id));
    }
}

