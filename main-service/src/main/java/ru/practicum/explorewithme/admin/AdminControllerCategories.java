package ru.practicum.explorewithme.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.categoryDto.CategoryDto;
import ru.practicum.explorewithme.dto.categoryDto.NewCategoryDto;
import ru.practicum.explorewithme.service.categoryService.CategoryService;

import javax.validation.Valid;

@RestController
@Slf4j
@Validated
@RequestMapping("/admin/categories")
public class AdminControllerCategories {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto postCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("POST /admin/categories BODY NewCategoryDto {}", newCategoryDto);
        return categoryService.postCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer catId) {
        log.info("DELETE /admin/categories/{} ", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto updateCategory(@PathVariable Integer catId,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        log.info("PATCH /admin/categories/{} BODY CategoryDto {}", catId, categoryDto);
        return categoryService.updateCategory(catId, categoryDto);
    }

}
