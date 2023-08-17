package ru.practicum.explorewithme.mapper.commentMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.commentDto.CommentDto;
import ru.practicum.explorewithme.model.Comment;

@Component
@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "commentator", source = "commentator.name")
    CommentDto transformCommentToCommentDto(Comment comment);
}
