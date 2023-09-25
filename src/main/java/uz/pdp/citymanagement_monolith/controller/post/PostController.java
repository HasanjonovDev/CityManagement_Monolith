package uz.pdp.citymanagement_monolith.controller.post;

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
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search(
            @RequestParam(required = false,defaultValue = "") String search,
            @RequestBody Filter filter
    ) {
        return ResponseEntity.ok(postService.search(search,filter));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAll(
            @RequestBody Filter filter
    ) {
        return ResponseEntity.ok(postService.getAll(filter));
    }
    @PreAuthorize("hasAnyAuthority('PERMISSION_POST_CRUD','PERMISSION_ALL_CRUD','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> savePost(
            @RequestBody PostCreateDto postCreateDto,
            Principal principal
    ) {
        return ResponseEntity.ok(postService.save(postCreateDto,principal));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/get")
    public ResponseEntity<ApiResponse> get(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(postService.getById(id));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get")
    public ResponseEntity<ApiResponse> get(
            Principal principal
    ) {
        return ResponseEntity.ok(postService.get(principal));
    }
    @PreAuthorize("hasAnyAuthority('PERMISSION_POST_CRUD','PERMISSION_ALL_CRUD','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse> update(
            @RequestBody PostCreateDto postCreateDto,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(postService.update(postCreateDto,id));
    }
    @PreAuthorize("hasAnyAuthority('PERMISSION_POST_CRUD','PERMISSION_ALL_CRUD','ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse> delete(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(postService.delete(id));
    }
}

