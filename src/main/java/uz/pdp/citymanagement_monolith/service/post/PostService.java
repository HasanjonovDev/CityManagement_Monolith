package uz.pdp.citymanagement_monolith.service.post;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.post.PostCreateDto;
import uz.pdp.citymanagement_monolith.domain.dto.post.PostForUserDto;
import uz.pdp.citymanagement_monolith.domain.dto.response.ApiResponse;
import uz.pdp.citymanagement_monolith.domain.entity.post.PostEntity;
import uz.pdp.citymanagement_monolith.domain.entity.post.PostStatus;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.exception.DataNotFoundException;
import uz.pdp.citymanagement_monolith.repository.post.PostRepositoryImpl;
import uz.pdp.citymanagement_monolith.repository.user.UserRepositoryImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepositoryImpl postRepository;
    private final UserRepositoryImpl userRepository;
    private final ModelMapper modelMapper;
    public ApiResponse save(PostCreateDto postCreateDto, Principal principal) {
        PostEntity post = modelMapper.map(postCreateDto, PostEntity.class);
        UserEntity userEntity = userRepository.findUserEntityByEmail(principal.getName()).orElseThrow(() -> new DataNotFoundException("User not found!"));
        post.setOwner(userEntity);
        post.setStatus(PostStatus.CREATED);
        PostEntity savedPost = postRepository.save(post);
        return new ApiResponse(HttpStatus.OK,true,"Successfully saved", modelMapper.map(savedPost, PostForUserDto.class));
    }

    public ApiResponse getById(UUID postId) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new DataNotFoundException("Post not found!"));
        return new ApiResponse(HttpStatus.OK,true,"Success",modelMapper.map(postEntity,PostForUserDto.class));
    }

    public ApiResponse get(Principal principal) {
        UserEntity user = userRepository.findUserEntityByEmail(principal.getName()).orElseThrow(() -> new DataNotFoundException("User not found!"));
        PostEntity postEntity = postRepository.findPostEntityByOwnerId(user.getId()).orElseThrow(() -> new DataNotFoundException("Post not found!"));
        return new ApiResponse(HttpStatus.OK,true,"Success",modelMapper.map(postEntity,PostForUserDto.class));
    }

    public ApiResponse update(PostCreateDto postCreateDto, UUID id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Post not found!"));
        modelMapper.map(postCreateDto,postEntity);
        postRepository.save(postEntity);
        return new ApiResponse(HttpStatus.OK,true,"Successfully updated!");
    }

    public ApiResponse delete(UUID id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Post not found!"));
        postRepository.delete(postEntity);
        return new ApiResponse(HttpStatus.OK,true,"Successfully deleted!");
    }

    public ApiResponse getAll(Filter filter) {
        List<PostEntity> all = postRepository.findAll(filter);
        List<PostForUserDto> forUserDto = new ArrayList<>();
        all.forEach((post) -> forUserDto.add(modelMapper.map(post, PostForUserDto.class)));
        return new ApiResponse(HttpStatus.OK,true,"Success",forUserDto);
    }

    public ApiResponse search(String search, Filter filter) {
        String success = "Success";
        if(search.isBlank()) success = "Search text is blank!";
        List<PostEntity> posts = postRepository.findPostEntitiesByNameContainingIgnoreCase(search,filter);
        List<PostForUserDto> forUserDto = new ArrayList<>();
        posts.forEach((post) -> forUserDto.add(modelMapper.map(post,PostForUserDto.class)));
        return new ApiResponse(HttpStatus.OK,true,success,forUserDto);
    }
}
