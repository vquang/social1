package nvq.nvq.common.request.post;

import io.micrometer.common.util.StringUtils;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShortPostRequest {
    private String image;
    private String content;

    public ShortPostRequest dfImage(String image) {
        return StringUtils.isBlank(this.image)
                ? this.setImage(image)
                : this;
    }
}
