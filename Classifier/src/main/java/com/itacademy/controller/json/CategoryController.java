package com.itacademy.controller.json;

import com.itacademy.dto.CustomPage;
import com.itacademy.dao.entity.Category;
import com.itacademy.dto.CategoryCreate;
import com.itacademy.service.api.ICategoryService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  ICategoryService categoryService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void create(@RequestBody CategoryCreate categoryCreate) {
    categoryService.addCategory(categoryCreate);
  }

  @GetMapping
  public ResponseEntity<CustomPage<Category>>  findPaginated(@RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "20") int size) {
    return new ResponseEntity<>(categoryService.getCustomPage(page, size), HttpStatus.OK);
  }

  @GetMapping("/{uuid}")
  public ResponseEntity<Category> get(@PathVariable UUID uuid) {
    return new ResponseEntity<>(categoryService.getCategoryByUuid(uuid), HttpStatus.OK) ;
  }
}
