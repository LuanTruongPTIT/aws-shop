package com.aws.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aws.identity.model.IdentityModel;

import jakarta.transaction.Transactional;

@Repository
public interface IdentityRepository extends JpaRepository<IdentityModel, Long> {

  IdentityModel findByJti(String jti);

  @Transactional
  @Modifying
  @Query(value = "UPDATE identity SET is_active = :is_active, jti = :jti, is_verify = :is_verify WHERE jti = :jti", nativeQuery = true)
  void updateIdentityModel(
      @Param("is_active") Boolean is_active,
      @Param("jti") String jti,
      @Param("is_verify") Boolean is_verify);
}
