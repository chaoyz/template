package cn.idocode.template.singleserver.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 常用时间处理类
 *
 * @author levicyang
 * 2020-08-20 14:42
 */
@Slf4j
public class TimeUtil {
    public static final SimpleDateFormat SDF_YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DTF_YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DTF_2_YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyy-MM-dd_HH:mm:ss");
    public static final DateTimeFormatter DTF_YYYYMM = DateTimeFormatter.ofPattern("yyyMM");
    public static final DateTimeFormatter DTF_YYYYMMDD = DateTimeFormatter.ofPattern("yyyMMdd");

    private TimeUtil() {

    }

    public static long parseFormatStrDateSecondTime(String time) {
        long result = 0;
        if (StringUtils.isNotEmpty(time)) {
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(time, DTF_YYYY_MM_DD_HH_MM_SS);
                result = localDateTime.toEpochSecond(ZoneOffset.ofHours(8));
            } catch (Exception e) {
                log.error("TimeUtil parseFormatStrDateSecondTime parse time str error. time:" + time, e);
            }
        }
        return result;
    }
}
