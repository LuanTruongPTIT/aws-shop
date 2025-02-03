package com.aws.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aws.identity.model.RoleModel;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Long> {

  @Query(value = "SELECT * FROM table_role WHERE name = :name", nativeQuery = true)
  RoleModel findByName(String name);
}
