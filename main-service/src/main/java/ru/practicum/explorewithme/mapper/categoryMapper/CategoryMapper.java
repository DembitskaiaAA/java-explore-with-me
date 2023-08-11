package ru.practicum.explorewithme.mapper.categoryMapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.dto.categoryDto.CategoryDto;
import ru.practicum.explorewithme.dto.categoryDto.NewCategoryDto;
import ru.practicum.explorewithme.model.Category;

@Component
@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category transformNewCategoryDtoToCategory(NewCategoryDto newCategoryDto);

    CategoryDto transformCategoryToCategoryDto(Category category);
}