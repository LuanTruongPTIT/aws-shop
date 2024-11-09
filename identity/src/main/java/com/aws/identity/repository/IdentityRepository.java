package com.aws.identity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aws.identity.model.IdentityModel;

@Repository
public interface IdentityRepository extends JpaRepository<IdentityModel, Long> {

  Optional<IdentityModel> findByJti(String jti);

  IdentityModel update(IdentityModel identityModel);
}
