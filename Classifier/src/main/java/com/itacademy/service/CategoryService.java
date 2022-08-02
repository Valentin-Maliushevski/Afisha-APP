package com.itacademy.service;

import com.itacademy.service.converters.CategoryCreateToCategoryConverter;
import com.itacademy.service.converters.CategoryPageToCustomPageConverter;
import com.itacademy.dto.CustomPage;
import com.itacademy.dao.api.ICategoryDao;
import com.itacademy.dao.entity.Category;
import com.itacademy.dto.CategoryCreate;
import com.itacademy.service.api.ICategoryService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryService implements ICategoryService {

  @Autowired
  ICategoryDao categoryDao;
  @Autowired
  CategoryCreateToCategoryConverter categoryCreateToCategoryConverter;
  @Autowired
  CategoryPageToCustomPageConverter categoryPageToCustomPageConverter;

  @Override
  @Transactional
  public void addCategory(CategoryCreate categoryCreate) {
    categoryDao.save(categoryCreateToCategoryConverter.convert(categoryCreate));
  }

  @Override
  public CustomPage<Category> getCustomPage(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));

    Page page1 = categoryDao.findAll(pageable);

    if (page + 1 > page1.getTotalPages()) {
      throw new IllegalArgumentException("There are less pages than you want");
    }

    return categoryPageToCustomPageConverter.convert(page1);
  }

  @Override
  public Category getCategoryByUuid(UUID uuid) {
    Category category = categoryDao.findByUuid(uuid);
    if(category == null) {
      throw new IllegalArgumentException("Category with such uuid is not found");
    }
    return category;
  }
}
