package com.aws.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.aws.account.model.AccountModel;

import jakarta.transaction.Transactional;

@Repository
public interface AccountRepository extends JpaRepository<AccountModel, Long> {
  @Query(value = "Insert INTO account ( " +
      "first_name, " +
      "last_name, " +
      "email, " +
      "password, " +
      "phone, avatar) VALUES (:first_name, :last_name, :email, :password, :phone, :avatar)"
      + "RETURNING id", nativeQuery = true)
  // @Modifying
  int RegisterAccount(
      @Param("first_name") String first_name,
      @Param("last_name") String last_name,
      @Param("email") String email,
      @Param("password") String password,
      @Param("phone") String phone,
      @Param("avatar") String avatar);

  @Query(value = "SELECT exists(select 1 FROM account WHERE email = :email )", nativeQuery = true)
  boolean existsByEmail(String email);

  @Query(value = "SELECT * FROM account WHERE id = :id", nativeQuery = true)
  AccountModel GetAccountById(int id);

  @Transactional
  @Modifying
  @Query(value = "UPDATE account SET is_active = :is_active, id_identity = :id_identity WHERE id = :id AND jti = :jti", nativeQuery = true)
  void updateAccountSyncStatus(
      @Param("id") Long id,
      @Param("is_active") boolean is_active,
      @Param("id_identity") Long id_identity,
      @Param("jti") String jti);

  @Query(value = "SELECT * FROM account WHERE jti = :jti AND is_deleted = false", nativeQuery = true)
  AccountModel findByJti(String jti);
}