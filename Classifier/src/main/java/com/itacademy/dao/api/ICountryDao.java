package com.itacademy.dao.api;

import com.itacademy.dao.entity.Country;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICountryDao extends JpaRepository<Country, UUID> {

  Country save(Country country);

  Page<Country> findAll(Pageable pageable);

  Country findByUuid(UUID uuid);

  Country findByTitleIgnoreCase(String title);

}
