package ru.practicum.explorewithme.shared;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.pagination.Pagination;
import ru.practicum.explorewithme.dto.categoryDto.CategoryDto;
import ru.practicum.explorewithme.service.categoryService.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/categories")
public class SharedControllerCategories {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getCategories(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("GET /categories PARAMS from {}, size {}", from, size);
        return categoryService.getCategories(Pagination.splitByPages(from, size));
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable Integer catId) {
        log.info("GET /categories/{}", catId);
        return categoryService.getCategoryById(catId);
    }

}
