package nvq.nvq.api.controller.file;

import io.reactivex.rxjava3.core.Single;
import nvq.nvq.api.service.handle.file.IFileService;
import nvq.nvq.common.response.DfResponse;
import nvq.nvq.common.response.file.FileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static nvq.nvq.common.constant.Media.IMAGE;

@RestController
@RequestMapping("/api/nvq/file")
public class FileController {
    @Autowired
    private IFileService fileService;

    @PostMapping("/upload/image")
    public Single<ResponseEntity<DfResponse<FileResponse>>> uploadImage(
            @RequestParam("file") MultipartFile multipartFile
    ) {
        return fileService.upload(multipartFile, IMAGE)
                .map(DfResponse::ok);
    }
}
