package demo.spring.springJwt.repository;

import demo.spring.springJwt.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);
}
