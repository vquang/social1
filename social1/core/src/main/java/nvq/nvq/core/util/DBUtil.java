package nvq.nvq.core.util;

import org.jooq.Field;
import org.jooq.impl.TableRecordImpl;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class DBUtil {
    public static String generateID() {
        return UUID
                .randomUUID()
                .toString()
                .replace("-", "");
    }

    public static <T extends TableRecordImpl<T>> Map<Field<?>, Object> toFieldQueries(T record, Object o) {
        record.from(o);
        return Arrays
                .stream(record.fields())
                .filter(f -> record.getValue(f) != null)
                .collect(Collectors.toMap(f -> f, record::getValue));
    }
}
