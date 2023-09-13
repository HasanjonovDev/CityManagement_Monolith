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
public class Filter<T extends BaseEntity> {
    private String endDate;
    private String startDate;
    private Double minPrice;
    private Double maxPrice;
    private String type;
    private String status;
    public List<T> doFilter(List<T> getAll) {
        List<T> newOne = new ArrayList<>();
        getAll.forEach((obj) -> {
            try {
                Date date = Date.from(obj.getCreatedTime().atZone(ZoneId.systemDefault()).toInstant());
                Date startTime;
                Date endTime;
                if(startDate == null || startDate.isBlank()) startTime = new Date(1577836800000L);
                else startTime = new SimpleDateFormat("yyyy-MM-dd").parse(this.startDate);

                if(endDate == null || endDate.isBlank()) endTime = new Date();
                else endTime = new SimpleDateFormat("yyyy-MM-dd").parse(this.endDate);

                if(date.after(startTime) && date.before(endTime)) newOne.add(obj);
                if(obj instanceof FlatEntity) {
                    FlatEntity map = new ModelMapper().map(obj, FlatEntity.class);
                    if (map.getPricePerMonth()>=minPrice || map.getPricePerMonth()<=maxPrice) newOne.add((T) map);
                    if (Objects.equals(map.getFlatType().toString(),type)) newOne.add((T) map);
                    if (Objects.equals(map.getStatus().toString(),status)) newOne.add((T) map);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        return newOne;
    }
}
