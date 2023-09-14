package uz.pdp.citymanagement_monolith.domain.dto.report;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class WeekReport {
    private Integer totalSell;
    private Double totalProfit;
    private Map<String,Integer> soldGoods;
}
