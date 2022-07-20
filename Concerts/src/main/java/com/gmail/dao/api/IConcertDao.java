package com.gmail.dao.api;

import com.gmail.dao.entity.Concert;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IConcertDao extends JpaRepository<Concert, Long> {

  Concert save(Concert concert);

  Concert findByUuid(UUID uuid);

  Page<Concert> findAll(Pageable pageable);

  //  Concert findByTitle(String title);
}
