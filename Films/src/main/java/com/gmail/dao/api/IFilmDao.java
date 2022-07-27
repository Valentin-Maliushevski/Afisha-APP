package com.gmail.dao.api;

import com.gmail.dao.entity.EventStatus;
import com.gmail.dao.entity.Film;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFilmDao extends JpaRepository<Film, Long> {

  Film save(Film film);

  Film findByUuid(UUID uuid);

  Page<Film> findAll(Pageable pageable);

  Page<Film> findByEventStatus(EventStatus eventStatus, Pageable pageable);

  Page<Film> findByEventStatusOrAuthorUuid(EventStatus status, UUID authorUuid, Pageable pageable);

}
