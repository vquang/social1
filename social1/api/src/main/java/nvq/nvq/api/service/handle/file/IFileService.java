package nvq.nvq.api.service.handle.file;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.common.constant.Media;
import nvq.nvq.common.response.file.FileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {
    Single<FileResponse> upload(MultipartFile multipartFile, Media media);
}
