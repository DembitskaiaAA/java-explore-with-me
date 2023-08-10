package ru.practicum.explorewithme.service.categoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.common.checks.CheckExist;
import ru.practicum.explorewithme.dto.categoryDto.CategoryDto;
import ru.practicum.explorewithme.dto.categoryDto.NewCategoryDto;
import ru.practicum.explorewithme.exceptions.ConditionException;
import ru.practicum.explorewithme.mapper.categoryMapper.CategoryMapper;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CheckExist checkExist;

    @Override
    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.transformNewCategoryDtoToCategory(newCategoryDto);
        return categoryMapper.transformCategoryToCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Integer catId) {
        checkExist.checkCategoryOnExist(catId, String.format("Error when deleting: category with id: %s is missing", catId));
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategory(Integer catId, CategoryDto categoryDto) {
        Category category = checkExist.checkCategoryOnExist(catId, String.format("Error when updating: category with id: %s is missing", catId));
        Category checkNameCategory = categoryRepository.findByName(categoryDto.getName());
        if (checkNameCategory != null) {
            if (checkNameCategory.getId() != catId) {
                throw new ConditionException(String.format("Category name %s already exists", categoryDto.getName()));
            }
        }
        category.setName(categoryDto.getName());
        return categoryMapper.transformCategoryToCategoryDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> getCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<Category> categories = categoryPage.getContent();
        return categories.stream().map(x -> categoryMapper.transformCategoryToCategoryDto(x)).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Integer catId) {
        Category category = checkExist.checkCategoryOnExist(catId, String.format("Error when getting category: category with id: %s is missing", catId));
        return categoryMapper.transformCategoryToCategoryDto(category);
    }
}
