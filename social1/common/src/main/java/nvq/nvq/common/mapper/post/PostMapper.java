package nvq.nvq.common.mapper.post;

import nvq.nvq.common.request.post.ShortPostRequest;
import nvq.nvq.common.response.post.DetailPostResponse;
import nvq.nvq.mysql.tables.pojos.Post;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PostMapper {
    public abstract Post toPojo(ShortPostRequest shortPostRequest);

    public abstract DetailPostResponse toResponse(Post post);
}
