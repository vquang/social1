package nvq.nvq.common.mapper.vote;

import nvq.nvq.common.request.vote.VoteRequest;
import nvq.nvq.mysql.tables.pojos.Vote;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class VoteMapper {
    public abstract Vote toPojo(VoteRequest voteRequest);
}
