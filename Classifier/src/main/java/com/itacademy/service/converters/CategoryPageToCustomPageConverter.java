package com.itacademy.service.converters;

import com.itacademy.dao.entity.Category;
import com.itacademy.dto.CustomPage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CategoryPageToCustomPageConverter implements Converter<Page<Category>, CustomPage<Category>> {

  @Override
  public CustomPage<Category> convert(Page<Category> page) {
    CustomPage<Category> categoriesPage = new CustomPage<>();
    categoriesPage.setNumber(page.getNumber());
    categoriesPage.setSize(page.getSize());
    categoriesPage.setTotalPages(page.getTotalPages());
    categoriesPage.setTotalElements(page.getTotalElements());
    categoriesPage.setNumberOfElements(page.getContent().size());
    categoriesPage.setFirstPage(page.isFirst());
    categoriesPage.setLastPage(page.isLast());
    categoriesPage.setContent(page.getContent());

    return categoriesPage;
  }
}
