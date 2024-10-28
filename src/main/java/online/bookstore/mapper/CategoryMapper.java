package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.book.CreateBookRequestDto;
import online.bookstore.dto.category.CategoryDto;
import online.bookstore.dto.category.CreateCategoryRequestDto;
import online.bookstore.model.Book;
import online.bookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto categoryDTO);

    void updateCategoryFromDto(CreateCategoryRequestDto categoryRequestDto, @MappingTarget Category category);
}
