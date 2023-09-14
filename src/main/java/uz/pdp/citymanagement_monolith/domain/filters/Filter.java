package uz.pdp.citymanagement_monolith.domain.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import uz.pdp.citymanagement_monolith.domain.entity.BaseEntity;
import uz.pdp.citymanagement_monolith.domain.entity.apartment.FlatEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Filter<Object extends BaseEntity> {
    {
        this.page = 0;
        this.perPage = 10;
    }
    private String endDate;
    private String startDate;
    private Double minPrice;
    private Double maxPrice;
    private String type;
    private String status;
    private int page;
    private int perPage;
    public List<Object> doFilter(List<Object> getAll) {
        List<Object> newOne = new ArrayList<>();
        for (Object obj : getAll) {
            try {
                double minPrice;
                double maxPrice;
                boolean emptyType = false;
                boolean emptyStatus = false;
                Date date = Date.from(obj.getCreatedTime().atZone(ZoneId.systemDefault()).toInstant());
                Date startTime;
                Date endTime;
                if (this.minPrice == null) minPrice = 0D;
                else minPrice = this.minPrice;
                if (this.maxPrice == null) maxPrice = 0D;
                else maxPrice = this.maxPrice;
                if(type == null || type.isBlank()) emptyType = true;
                if(status == null || status.isBlank()) emptyStatus = true;

                if (startDate == null || startDate.isBlank()) startTime = new Date(1577836800000L);
                else startTime = new SimpleDateFormat("yyyy-MM-dd").parse(this.startDate);

                if (endDate == null || endDate.isBlank()) endTime = new Date();
                else endTime = new SimpleDateFormat("yyyy-MM-dd").parse(this.endDate);

                if (obj instanceof FlatEntity) {
                    FlatEntity map = new ModelMapper().map(obj, FlatEntity.class);
                    if (map.getPricePerMonth() >= minPrice
                            && map.getPricePerMonth() <= maxPrice
                            && (emptyType || Objects.equals(map.getFlatType().toString(), type))
                            && (emptyStatus || Objects.equals(map.getStatus().toString(), status))
                            && date.after(startTime) && date.before(endTime)) {
                        newOne.add((Object) map);
                    }
                    continue;
                }
                if (date.after(startTime) && date.before(endTime)) {
                    newOne.add(obj);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return newOne;
    }
}
