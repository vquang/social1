package nvq.nvq.core.util;

import com.google.gson.Gson;

import java.util.List;

public class JsonUtil {
    private static final Gson gson = new Gson();
    public static String objectToJson(Object o) {
        return gson.toJson(o);
    }
    public static <T> T jsonToObject(String json, Class<T> tClass) {
        return gson.fromJson(json, tClass);
    }
    public static List jsonToList(String json) {
        return gson.fromJson(json, List.class);
    }
}
