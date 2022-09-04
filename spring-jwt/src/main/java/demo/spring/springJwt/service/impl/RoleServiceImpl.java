package demo.spring.springJwt.service.impl;

import demo.spring.springJwt.model.entity.RoleEntity;
import demo.spring.springJwt.repository.RoleJpaRepository;
import demo.spring.springJwt.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleJpaRepository roleJpaRepository;

    @Override
    public RoleEntity save(RoleEntity roleEntity) {
        log.info("Saving role {} to the database", roleEntity.getName());
        return roleJpaRepository.save(roleEntity);
    }

}