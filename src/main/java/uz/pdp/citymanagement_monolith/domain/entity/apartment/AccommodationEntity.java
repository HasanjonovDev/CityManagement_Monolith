package uz.pdp.citymanagement_monolith.domain.entity.apartment;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;

@Entity(name = "accommodations")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccommodationEntity extends BaseEntity {
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    private LocationEntity locationEntity;
    private Integer numberOfFlats;
    private Integer floors;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyEntity company;
}
