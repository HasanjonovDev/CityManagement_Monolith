package uz.pdp.citymanagement_monolith.service.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.report.WeekReport;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingEntity;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingType;
import uz.pdp.citymanagement_monolith.domain.filters.Filter;
import uz.pdp.citymanagement_monolith.repository.booking.BookingRepositoryImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final BookingRepositoryImpl bookingRepository;

    public WeekReport reportPerWeek(Filter filter) {
        LocalDateTime weekAgo = new Date(System.currentTimeMillis() - (86400000 * 7))
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        List<BookingEntity> weeklyBooked = bookingRepository.findAllByCreatedTimeAfter(weekAgo, filter);
        Map<String, Integer> soldGoods = new HashMap<>();
        if(filter.getType() == null) {
            for (BookingType type : BookingType.values()) {
                soldGoods.put(type.name(), bookingRepository.getCount(type, filter, weekAgo));
            }
        } else
            soldGoods.put(filter.getType(),bookingRepository.getCount(BookingType.valueOf(filter.getType()),filter,weekAgo));
        Double totalProfit = bookingRepository.getWeeklyProfit(weekAgo,filter).get();
        return WeekReport.builder()
                .totalSell(weeklyBooked.size())
                .totalProfit(totalProfit)
                .soldGoods(soldGoods)
                .build();
    }
}
