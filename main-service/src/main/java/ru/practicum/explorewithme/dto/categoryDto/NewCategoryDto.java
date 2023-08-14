package ru.practicum.explorewithme.dto.categoryDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCategoryDto {
    @NotBlank(message = "Category name cannot be empty")
    @Size(min = 1, max = 50, message = "Category name length must be between 1 and 50")
    String name;
}
