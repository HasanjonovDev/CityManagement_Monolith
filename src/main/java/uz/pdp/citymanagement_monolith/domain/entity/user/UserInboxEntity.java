package uz.pdp.citymanagement_monolith.domain.entity.user;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;

@Entity(name = "user_inbox")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserInboxEntity extends BaseEntity {
    @ManyToOne(cascade = CascadeType.DETACH)
    private UserEntity toWhom;
    @ManyToOne(cascade = CascadeType.DETACH)
    private UserEntity fromWhom;
    private String message;
    @Enumerated(EnumType.STRING)
    private MessageState state;
    private String type;
}
