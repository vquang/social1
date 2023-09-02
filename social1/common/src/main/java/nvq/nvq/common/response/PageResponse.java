package nvq.nvq.common.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import nvq.nvq.common.request.PageRequest;

import java.util.List;

@Data
@Accessors(chain = true)
public class PageResponse<T> {
    private int total;
    private int page;
    private int limit;
    private int offset;
    private boolean preLoadAble;
    private boolean loadMoreAble;
    private List<T> items;

    public PageResponse() {

    }

    public static <T> PageResponse<T> config(PageRequest pageRequest, List<T> items) {
        PageResponse<T> pageResponse = new PageResponse<T>()
                .setTotal(pageRequest.getTotal())
                .setPage(pageRequest.getPage())
                .setLimit(pageRequest.getLimit())
                .setOffset(pageRequest.getOffset())
                .setPreLoadAble(pageRequest.getPage() > 1)
                .setLoadMoreAble(items.size() > pageRequest.getLimit())
                .setItems(items);
        pageResponse.reSize();
        return pageResponse;
    }

    private void reSize() {
        if (this.loadMoreAble) this.items.remove(this.items.size() - 1);
    }
}
