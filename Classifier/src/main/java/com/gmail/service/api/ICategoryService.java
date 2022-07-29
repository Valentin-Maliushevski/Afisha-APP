package com.gmail.service.api;

import com.gmail.dto.CustomPage;
import com.gmail.dao.entity.Category;
import com.gmail.dto.CategoryCreate;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.UUID;
import javax.validation.Valid;

public interface ICategoryService {

  void addCategory(@Valid CategoryCreate categoryCreate) throws Multiple400Exception, SingleException;

  CustomPage<Category> getCustomPage(int page, int size) throws SingleException;

  Category getCategoryByUuid(UUID uuid) throws Multiple400Exception;

  Category getCategoryByTitle(String title) throws SingleException;
}
