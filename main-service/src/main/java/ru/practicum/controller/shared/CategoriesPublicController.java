package ru.practicum.controller.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.service.category.CategoryService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoriesPublicController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(
            @RequestParam(required = false, defaultValue = "0") @Min(value = 0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 10) Integer size
    ) {
        List<CategoryDto> categoryDtos = categoryService.getCategories(from, size);
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long catId) {
        CategoryDto categoryDto = categoryService.getCategory(catId);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

}
