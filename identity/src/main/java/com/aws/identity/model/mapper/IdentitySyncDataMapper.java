package com.aws.identity.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.aws.identity.model.IdentityModel;
import com.aws.identity.view.consumer.AccountSync;

@Mapper(componentModel = "spring")
public interface IdentitySyncDataMapper {

  @Mapping(target = "account_id", source = "id")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "password", source = "password")
  @Mapping(target = "jti", source = "jti")
  IdentityModel toIdentityModellFromIdentitySyncData(AccountSync accountSync);
}
