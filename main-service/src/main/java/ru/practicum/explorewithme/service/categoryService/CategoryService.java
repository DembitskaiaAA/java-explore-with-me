package ru.practicum.explorewithme.service.categoryService;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.categoryDto.CategoryDto;
import ru.practicum.explorewithme.dto.categoryDto.NewCategoryDto;

import java.util.List;

@Service
public interface CategoryService {
    CategoryDto postCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Integer catId);

    CategoryDto updateCategory(Integer catId, CategoryDto categoryDto);

    List<CategoryDto> getCategories(Pageable pageable);

    CategoryDto getCategoryById(Integer catId);
}
