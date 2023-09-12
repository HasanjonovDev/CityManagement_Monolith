package uz.pdp.citymanagement_monolith.domain.entity.apartment;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;

import java.util.UUID;


@Entity(name = "flat")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class FlatEntity extends BaseEntity {
    private Integer number;
    private Integer whichFloor;
    private FlatType flatType;
    private Integer rooms;
    private UUID newOwnerId;
    private UUID ownerId;
    private UUID cardId;
    @ManyToOne
    private CompanyEntity company;
    private FlatStatus status;
    private String about;
    private Double pricePerMonth;
    private Double FullPrice;
}
