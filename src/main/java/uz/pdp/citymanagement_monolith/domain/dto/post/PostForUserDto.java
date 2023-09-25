package uz.pdp.citymanagement_monolith.domain.dto.post;

import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.post.PostStatus;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PostForUserDto {
    private UUID id;
    private String name;
    private String description;
    private UserEntity owner;
    private Double price;
    private String telephoneNumber;
    private PostStatus status;
}
