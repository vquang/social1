package nvq.nvq.core.util;

import nvq.nvq.core.config.exception.ApiException;

import static nvq.nvq.common.constant.StatusRp.RESOURCE_NOT_FOUND;

public class ReturnUtil {
    public static String statusDB(Integer e) throws ApiException {
        if (e == 1) return "SUCCESS";
        throw new ApiException(RESOURCE_NOT_FOUND);
    }
}
