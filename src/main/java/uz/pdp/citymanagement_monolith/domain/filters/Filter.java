package uz.pdp.citymanagement_monolith.domain.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Filter {
    private String endDate;
    private String startDate;
    private Double minPrice;
    private Double maxPrice;
    private String type;
    private String status;
}
