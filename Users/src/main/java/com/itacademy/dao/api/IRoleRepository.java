package com.itacademy.dao.api;

import com.itacademy.dao.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {

}
