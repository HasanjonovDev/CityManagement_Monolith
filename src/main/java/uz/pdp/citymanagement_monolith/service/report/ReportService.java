package uz.pdp.citymanagement_monolith.service.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.citymanagement_monolith.domain.dto.report.WeekReport;
import uz.pdp.citymanagement_monolith.domain.entity.booking.BookingEntity;
import uz.pdp.citymanagement_monolith.repository.apartment.FlatRepository;
import uz.pdp.citymanagement_monolith.repository.booking.BookingRepository;

import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final FlatRepository flatRepository;
    private final BookingRepository bookingRepository;

    public WeekReport reportPerWeek() {
        List<BookingEntity> weeklyBooked = bookingRepository.findAllByCreatedTimeAfter(
                new Date(System.currentTimeMillis() - (86400000 * 7))
                        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        Map<String, Integer> soldGoods = new HashMap<>();
        AtomicInteger flats = new AtomicInteger();
        AtomicInteger products = new AtomicInteger();
        AtomicInteger doctors = new AtomicInteger();
        AtomicReference<Double> totalProfit = new AtomicReference<>(0D);
        weeklyBooked.forEach((order) -> {
            switch (order.getType()) {
                case FLAT -> flats.getAndIncrement();
                case PRODUCT -> doctors.getAndIncrement();
                case DOCTOR -> products.getAndIncrement();
            }
            totalProfit.updateAndGet(v -> v + order.getTotalPrice());
        });
        soldGoods.put("FLAT",flats.get());
        soldGoods.put("PRODUCTS",products.get());
        soldGoods.put("DOCTORS",doctors.get());
        return WeekReport.builder()
                .totalSell(weeklyBooked.size())
                .totalProfit(totalProfit.get())
                .soldGoods(soldGoods)
                .build();
    }
}
