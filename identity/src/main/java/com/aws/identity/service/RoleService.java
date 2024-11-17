package com.aws.identity.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.aws.identity.model.IdentityRoleModel;
import com.aws.identity.model.RoleModel;
import com.aws.identity.repository.RoleRepository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Service
public class RoleService {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final RoleRepository roleRepositroy;

  public RoleService(NamedParameterJdbcTemplate jdbcTemplate, RoleRepository roleRepositroy) {
    this.jdbcTemplate = jdbcTemplate;
    this.roleRepositroy = roleRepositroy;
  }

  public boolean InsertRoleAccount(IdentityRoleModel identityRoleModel) {
    String sql = "INSERT INTO table_identity_role (id_role, id_identity) VALUES (:id_role, :id_identity)";
    int rowsAffected = jdbcTemplate.update(sql, Map.of(
        "id_role", identityRoleModel.getId_role(),
        "id_identity", identityRoleModel.getId_identity()));
    return rowsAffected > 0;
  }

  // public RoleModel GetRoleByName(String name) {
  // String sql = "SELECT * FROM role WHERE name = :name";
  // return jdbcTemplate.queryForObject(sql, Map.of("name", name), (rs, rowNum) ->
  // {
  // RoleModel roleModel = new RoleModel();
  // roleModel.setId(rs.getLong("id"));
  // roleModel.setName(rs.getString("name"));
  // roleModel.setDescription(rs.getString("description"));
  // return roleModel;
  // });
  // }

  public RoleModel GetRoleByName(String name) {
    return roleRepositroy.findByName(name);
  }
}
