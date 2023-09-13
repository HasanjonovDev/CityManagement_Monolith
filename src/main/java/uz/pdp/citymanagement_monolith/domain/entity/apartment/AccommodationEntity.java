package uz.pdp.citymanagement_monolith.domain.entity.apartment;

import jakarta.persistence.*;
import lombok.*;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;


import java.util.List;

@Entity(name = "accommodation")
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
    private CompanyEntity company;
    @OneToMany
    private List<FlatEntity> flats;
}