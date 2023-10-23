package uz.pdp.citymanagement_monolith.domain.dto.response;

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.util.Date;
import java.time.ZoneId;
import java.time.LocalDateTime;

public class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
    @Override
    public Date convert(MappingContext<LocalDateTime, Date> context) {
        LocalDateTime source = context.getSource();
        if (source == null) {
            return null;
        }
        return Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
    }
}

