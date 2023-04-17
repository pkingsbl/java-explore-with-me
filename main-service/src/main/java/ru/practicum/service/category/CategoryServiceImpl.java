package ru.practicum.service.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.entity.Category;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.CategoryMapper.toCategory;
import static ru.practicum.mapper.CategoryMapper.toCategoryDto;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        log.info("Add category name: {}", newCategoryDto.getName());

        Category category = toCategory(newCategoryDto);
        Category saveAndFlush = categoryRepository.saveAndFlush(category);

        return toCategoryDto(saveAndFlush);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        log.info("Update category id: {}, name: {}", categoryDto.getId(), categoryDto.getName());

        Category category = findCategory(catId);
        if (categoryDto.getId() != null && !categoryDto.getId().equals(catId)) {
            throw new ValidationException("Параметр id категории не соответствует телу запроса");
        }
        if (categoryDto.getName() == null) {
            throw new ValidationException("Параметр name не может быть пустым");
        }
        category.setName(categoryDto.getName());
        category = categoryRepository.saveAndFlush(category);

        return toCategoryDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        log.info("Delete category id: {}", catId);
        Category category = findCategory(catId);

        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        log.info("Get categories");
        return categoryRepository.findAll(PageRequest.of(from / size, size))
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        log.info("Get category by id: {}", catId);
        Category category = findCategory(catId);
        return toCategoryDto(category);
    }

    private Category findCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
    }

}
