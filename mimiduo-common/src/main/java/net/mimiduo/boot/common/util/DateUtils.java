package net.mimiduo.boot.common.util;

import java.time.*;
import java.util.Date;

public class DateUtils {

    public static LocalDateTime DateToLocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }

    public static Date LocalDateTimeToDate(LocalDateTime localDateTime){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }


    public static Date LocalDateToDate(LocalDate localDate){
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt =localDate.atStartOfDay().atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }


}







