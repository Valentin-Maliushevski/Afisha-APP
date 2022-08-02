package com.itacademy.service.converters;

import com.itacademy.dao.entity.Category;
import com.itacademy.dto.CategoryCreate;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryCreateToCategoryConverter implements Converter<CategoryCreate, Category> {

  @Override
  public Category convert(CategoryCreate categoryCreate) {
    Category category = new Category();
    category.setUuid(UUID.randomUUID());
    category.setDtCreate(OffsetDateTime.now());
    category.setDtUpdate(category.getDtCreate());
    category.setTitle(categoryCreate.getTitle());

    return category;
  }
}
