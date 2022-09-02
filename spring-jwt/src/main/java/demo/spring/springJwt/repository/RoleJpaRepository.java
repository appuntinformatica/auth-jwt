package demo.spring.springJwt.repository;

import demo.spring.springJwt.model.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);
}
