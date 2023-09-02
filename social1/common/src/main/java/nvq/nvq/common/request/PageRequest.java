package nvq.nvq.common.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PageRequest {
    private int total;
    private int page = 1;
    private int limit = 10;
    private int offset = 0;
    public PageRequest offset() {
        this.offset = Math.max((page - 1) * limit, 0);
        return this;
    }
}
