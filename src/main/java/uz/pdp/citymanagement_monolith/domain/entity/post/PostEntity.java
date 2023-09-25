package uz.pdp.citymanagement_monolith.domain.entity.post;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;

@Entity(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PostEntity extends BaseEntity {
    private String name;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity owner;
    private Double price;
    private String telephoneNumber;
    @Enumerated(EnumType.STRING)
    private PostStatus status;
}
