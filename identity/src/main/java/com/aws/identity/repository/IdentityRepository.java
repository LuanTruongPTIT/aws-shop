package com.aws.identity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aws.identity.model.IdentityModel;

@Repository
public interface IdentityRepository extends JpaRepository<IdentityModel, Long> {

  IdentityModel findByJti(String jti);

  IdentityModel update(IdentityModel identityModel);
}
