package com.gmail.service;

import com.gmail.dto.CustomPage;
import com.gmail.dao.api.ICategoryDao;
import com.gmail.dao.entity.Category;
import com.gmail.dto.CategoryCreate;
import com.gmail.service.api.ICategoryService;
import com.gmail.service.custom_exception.multiple.EachErrorDefinition;
import com.gmail.service.custom_exception.multiple.ErrorsDefinition;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CategoryService implements ICategoryService {

  private final ICategoryDao categoryDao;

  public CategoryService(ICategoryDao categoryDao) {
    this.categoryDao = categoryDao;
  }

  @Override
  public void check(CategoryCreate categoryCreate) throws Multiple400Exception, SingleException {

    List<EachErrorDefinition> eachErrorDefinitions = new ArrayList<>();

    final String titleFieldError = "Category with such title is already exist";

    if(getCategoryByTitle(categoryCreate.getTitle()) != null) {
      EachErrorDefinition errorDefinition = new EachErrorDefinition("title", titleFieldError);
      eachErrorDefinitions.add(errorDefinition);
    }

    if(!eachErrorDefinitions.isEmpty()) {
      ErrorsDefinition errorsDefinition = new ErrorsDefinition(eachErrorDefinitions);
      throw new Multiple400Exception(errorsDefinition);
    }
  }

  @Override
  public void addCategory(CategoryCreate categoryCreate) throws Multiple400Exception, SingleException {

    check(categoryCreate);

    Category category = new Category();
    category.setUuid(UUID.randomUUID());
    category.setDtCreate(OffsetDateTime.now());
    category.setDtUpdate(category.getDtCreate());
    category.setTitle(categoryCreate.getTitle());

    this.categoryDao.save(category);
  }

  @Override
  public CustomPage<Category> getCustomPage(int page, int size) throws SingleException {
    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));

    Page page1 = this.categoryDao.findAll(pageable);

    if (page + 1 > page1.getTotalPages()) {
      throw new SingleException();
    }

    CustomPage<Category> categoriesPage = new CustomPage<>();
    categoriesPage.setNumber(page);
    categoriesPage.setSize(size);
    categoriesPage.setTotalPages(page1.getTotalPages());
    categoriesPage.setTotalElements(page1.getTotalElements());
    categoriesPage.setNumberOfElements(page1.getContent().size());
    categoriesPage.setFirstPage(page1.isFirst());
    categoriesPage.setLastPage(page1.isLast());
    categoriesPage.setContent(page1.getContent());

    return categoriesPage;
  }

  @Override
  public Category getCategoryByUuid(UUID uuid) throws SingleException {
    Category category = this.categoryDao.findByUuid(uuid);
    if(category == null) {
      throw new SingleException();
    }
    return category;
  }

  @Override
  public Category getCategoryByTitle(String title) throws SingleException {
    if(title.isBlank()) {
      throw new SingleException();
    }
    return this.categoryDao.findByTitleIgnoreCase(title);
  }
}
