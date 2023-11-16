package uz.pdp.city_management_monolith.domain.dto.post;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PostCreateDto {
    private String name;
    private String description;
    private Double price;
    private String telephoneNumber;
}
