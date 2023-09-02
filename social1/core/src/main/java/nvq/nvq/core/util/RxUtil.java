package nvq.nvq.core.util;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nvq.nvq.core.config.exception.ApiException;

import static nvq.nvq.common.constant.StatusRp.RESOURCE_NOT_FOUND;

public class RxUtil {
    @FunctionalInterface
    public interface SupplierThrowable<T> {
        T get() throws Exception;
    }

    public static <T> Single<T> rxSchedulerIo(SupplierThrowable<T> supplier) {
        return Single.just("io")
                .subscribeOn(Schedulers.io())
                .flatMap(s -> Single.create(emitter -> {
                    try {
                        final T value = supplier.get();
                        emitter.onSuccess(value);
                    } catch (Exception exception) {
                        emitter.onError(exception);
                    }
                }));
    }
}
