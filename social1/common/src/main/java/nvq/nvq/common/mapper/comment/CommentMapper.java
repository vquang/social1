package nvq.nvq.common.mapper.comment;

import nvq.nvq.common.request.comment.CommentRequest;
import nvq.nvq.common.request.comment.ShortCommentRequest;
import nvq.nvq.common.response.comment.DetailCommentResponse;
import nvq.nvq.mysql.tables.pojos.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {
    public abstract Comment toPojo(CommentRequest commentRequest);

    public abstract Comment toPojo(ShortCommentRequest shortCommentRequest);

    public abstract DetailCommentResponse toResponse(Comment comment);
}
