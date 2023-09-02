package nvq.nvq.core.util;

import java.time.LocalDateTime;

public class TimeUtil {
    public static long currentMillis() {
        return System.currentTimeMillis();
    }

    public static long currentNano() {
        return System.nanoTime();
    }

    public static LocalDateTime currentLocal() {
        return LocalDateTime.now();
    }
}
