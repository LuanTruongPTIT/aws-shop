package com.aws.identity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.aws.identity.model.IdentityModel;
import com.aws.identity.view.IdentityRoleView;
import jakarta.transaction.Transactional;
import java.util.List;

@Repository
public interface IdentityRepository extends JpaRepository<IdentityModel, Long> {

  @Query(value = "SELECT * FROM table_identity WHERE jti = :jti", nativeQuery = true)
  Optional<IdentityModel> findByJti(String jti);

  @Transactional
  @Modifying
  @Query(value = "UPDATE table_identity SET is_active = :is_active, jti = :jti, is_verify = :is_verify WHERE jti = :jti", nativeQuery = true)
  void updateIdentityModel(
      @Param("is_active") Boolean is_active,
      @Param("jti") String jti,
      @Param("is_verify") Boolean is_verify);

  @Query(value = "SELECT * FROM table_identity WHERE email = :email", nativeQuery = true)
  Optional<IdentityModel> findAccountByEmail(String email);

}
