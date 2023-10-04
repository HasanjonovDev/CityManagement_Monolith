package uz.pdp.citymanagement_monolith.domain.entity.apartment;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.user.UserEntity;


@Entity(name = "flat")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FlatEntity extends BaseEntity {
    private Integer number;
    private Integer whichFloor;
    @Enumerated(EnumType.STRING)
    private FlatType flatType;
    private Integer rooms;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    private AccommodationEntity accommodation;
    @Enumerated(EnumType.STRING)
    private FlatStatus status;
    private String about;
    private Double pricePerMonth;
    private Double fullPrice;
}
