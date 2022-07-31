package com.itacademy.service.api;

import com.itacademy.dto.CustomPage;
import com.itacademy.dao.entity.Category;
import com.itacademy.dto.CategoryCreate;
import java.util.UUID;
import javax.validation.Valid;

public interface ICategoryService {

  void addCategory(@Valid CategoryCreate categoryCreate);

  CustomPage<Category> getCustomPage(int page, int size);

  Category getCategoryByUuid(UUID uuid);

}
