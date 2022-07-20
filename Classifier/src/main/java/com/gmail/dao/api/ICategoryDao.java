package com.gmail.dao.api;

import com.gmail.dao.entity.Category;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryDao extends JpaRepository<Category, Long> {

  Category save(Category category);

  Page<Category> findAll(Pageable pageable);

  Category findByUuid(UUID uuid);

  Category findByTitleIgnoreCase(String title);

}
