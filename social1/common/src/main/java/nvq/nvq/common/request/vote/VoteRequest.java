package nvq.nvq.common.request.vote;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class VoteRequest {
    private String objectId;
    private String objectType;
}
