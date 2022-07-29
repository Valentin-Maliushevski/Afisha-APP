package com.gmail.service;

import com.gmail.dto.CustomPage;
import com.gmail.dao.api.ICategoryDao;
import com.gmail.dao.entity.Category;
import com.gmail.dto.CategoryCreate;
import com.gmail.service.api.ICategoryService;
import com.gmail.service.converters.CategoryCreateToCategoryConverter;
import com.gmail.service.converters.CategoryPageToCustomPageConverter;
import com.gmail.service.custom_exception.multiple.EachErrorDefinition;
import com.gmail.service.custom_exception.multiple.ErrorsDefinition;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CategoryService implements ICategoryService {

  private final ICategoryDao categoryDao;
  private final CategoryCreateToCategoryConverter categoryCreateToCategoryConverter;
  private final CategoryPageToCustomPageConverter categoryPageToCustomPageConverter;

  public CategoryService(ICategoryDao categoryDao,
      CategoryCreateToCategoryConverter categoryCreateToCategoryConverter,
      CategoryPageToCustomPageConverter categoryPageToCustomPageConverter) {
    this.categoryDao = categoryDao;
    this.categoryCreateToCategoryConverter = categoryCreateToCategoryConverter;
    this.categoryPageToCustomPageConverter = categoryPageToCustomPageConverter;
  }

  @Override
  @Transactional
  public void addCategory(CategoryCreate categoryCreate) throws Multiple400Exception, SingleException {
    this.categoryDao.save(categoryCreateToCategoryConverter.convert(categoryCreate));
  }

  @Override
  public CustomPage<Category> getCustomPage(int page, int size) throws SingleException {
    Pageable pageable = PageRequest.of(page, size, Sort.by("title"));

    Page page1 = this.categoryDao.findAll(pageable);

    if (page + 1 > page1.getTotalPages()) {
      throw new SingleException();
    }

    return categoryPageToCustomPageConverter.convert(page1);
  }

  @Override
  public Category getCategoryByUuid(UUID uuid) throws Multiple400Exception {
    Category category = this.categoryDao.findByUuid(uuid);
    final String uuidError = "There are no category with such uuid";
    List<EachErrorDefinition> eachErrorDefinitions = new ArrayList<>();
    if(category == null) {
      EachErrorDefinition errorDefinition = new EachErrorDefinition("uuid", uuidError);
      eachErrorDefinitions.add(errorDefinition);
    }
    if(!eachErrorDefinitions.isEmpty()) {
      ErrorsDefinition errorsDefinition = new ErrorsDefinition(eachErrorDefinitions);
      throw new Multiple400Exception(errorsDefinition);
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
