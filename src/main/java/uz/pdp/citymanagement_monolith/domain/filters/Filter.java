package uz.pdp.citymanagement_monolith.domain.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Filter {
    {
        if (this.perPage == 0) this.perPage = 10;
        if (this.page == 0) this.page = 1;
    }
    private Date endDate;
    private Date startDate;
    private double minPrice;
    private double maxPrice;
    private String type;
    private String status;
    private int page;
    private int perPage;
    private int numberOfFlats;
    private int floor;
}
