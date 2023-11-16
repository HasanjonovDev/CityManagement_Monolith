package uz.pdp.city_management_monolith.domain.entity.apartment;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.city_management_monolith.domain.entity.BaseEntity;

@Entity(name = "accommodations")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccommodationEntity extends BaseEntity {
    private String name;
    private Long number;
    @OneToOne(cascade = CascadeType.ALL)
    private LocationEntity locationEntity;
    private Integer numberOfFlats;
    private Integer floors;
//    private String imgPath;
    @ManyToOne(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;
}
