package nvq.nvq.common.response.file;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class FileResponse {
    private String url;
    private String type;
}
