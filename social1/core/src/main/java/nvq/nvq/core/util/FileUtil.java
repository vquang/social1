package nvq.nvq.core.util;

import static nvq.nvq.core.util.TimeUtil.currentNano;

public class FileUtil {
    public static String generateName(String name) {
        return currentNano() + "-" + name;
    }
}
