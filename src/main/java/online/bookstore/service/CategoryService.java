package online.bookstore.service;

import java.util.List;
import online.bookstore.dto.category.CategoryDto;
import online.bookstore.dto.category.CreateCategoryRequestDto;

public interface CategoryService {
    List<CategoryDto> findAll();

    CategoryDto getById(Long id);

    CategoryDto save(CreateCategoryRequestDto categoryDto);

    CategoryDto update(Long id, CreateCategoryRequestDto categoryDto);

    void deleteById(Long id);
}
