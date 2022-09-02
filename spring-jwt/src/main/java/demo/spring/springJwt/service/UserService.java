package demo.spring.springJwt.service;

import java.util.List;

import demo.spring.springJwt.model.entity.UserEntity;

public interface UserService {

    UserEntity save(UserEntity user);
    UserEntity addRoleToUser(String username, String roleName);
    UserEntity findByUsername(String username);
    List<UserEntity> findAll();
  //  Map<String,String> refreshToken(String authorizationHeader, String issuer) throws BadJOSEException, ParseException, JOSEException;
}
