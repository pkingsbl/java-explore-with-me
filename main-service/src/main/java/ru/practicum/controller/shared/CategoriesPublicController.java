package ru.practicum.controller.shared;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.service.StatsClientServiceImpl;
import ru.practicum.service.category.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoriesPublicController {

    private final CategoryService categoryService;
    private final StatsClientServiceImpl statsClientService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(
            @RequestParam(required = false, defaultValue = "0") @Min(value = 0) Integer from,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 10) Integer size,
            HttpServletRequest request
    ) {
        List<CategoryDto> categoryDtos = categoryService.getCategories(from, size);
        statsClientService.post(request);
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long catId, HttpServletRequest request) {
        CategoryDto categoryDto = categoryService.getCategory(catId);
        statsClientService.post(request);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

}
