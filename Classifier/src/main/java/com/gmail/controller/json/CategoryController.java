package com.gmail.controller.json;

import com.gmail.dto.CustomPage;
import com.gmail.dao.entity.Category;
import com.gmail.dto.CategoryCreate;
import com.gmail.service.api.ICategoryService;
import com.gmail.service.custom_exception.multiple.Multiple400Exception;
import com.gmail.service.custom_exception.single.SingleException;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/classifier/concert/category")
public class CategoryController {

  private final ICategoryService categoryService;

  public CategoryController(ICategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody CategoryCreate categoryCreate)
      throws Multiple400Exception, SingleException {
    this.categoryService.addCategory(categoryCreate);
  }

  @GetMapping
  public ResponseEntity<CustomPage<Category>>  findPaginated(@RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "20") int size) throws SingleException {
    return new ResponseEntity<>(this.categoryService.getCustomPage(page, size), HttpStatus.OK);
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<Category> get(@PathVariable UUID uuid) throws SingleException {
    return new ResponseEntity<>(this.categoryService.getCategoryByUuid(uuid), HttpStatus.OK) ;
  }
}
