package nvq.nvq.api.service.handle.file;

import io.reactivex.rxjava3.core.Single;
import lombok.SneakyThrows;
import nvq.nvq.common.constant.Media;
import nvq.nvq.common.response.file.FileResponse;
import nvq.nvq.core.config.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static nvq.nvq.common.constant.StatusRp.BAD_REQUEST;
import static nvq.nvq.core.util.FileUtil.generateName;
import static nvq.nvq.core.util.RxUtil.rxSchedulerIo;

@Service
public class FileService implements IFileService {
    @Override
    @SneakyThrows
    public Single<FileResponse> upload(MultipartFile multipartFile, Media media) {
        if (multipartFile == null) return Single.error(new ApiException(BAD_REQUEST));
        File file = new File(media.path() + generateName(multipartFile.getOriginalFilename()));
        multipartFile.transferTo(file);
        return rxSchedulerIo(() -> FileResponse
                .builder()
                .url(file.getName())
                .type(media.data())
                .build());
    }
}
