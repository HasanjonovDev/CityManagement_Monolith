package uz.pdp.citymanagement_monolith.domain.entity.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
    private UserEntity owner;
    private String message;
    private MessageState state;
    private String type;
}
