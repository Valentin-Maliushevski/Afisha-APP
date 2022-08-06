package com.itacademy.dao.api;

import com.itacademy.dao.entity.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {

  User findByUsername(String username);

  User findByUuid(UUID uuid);

}
