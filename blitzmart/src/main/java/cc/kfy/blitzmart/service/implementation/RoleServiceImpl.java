package cc.kfy.blitzmart.service.implementation;

import cc.kfy.blitzmart.domain.Role;
import cc.kfy.blitzmart.repository.RoleRepository;
import cc.kfy.blitzmart.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository<Role> roleRepository;
    @Override
    public Role getRoleByUserId(Long userId) {
        return roleRepository.getRoleByUserId(userId);
    }
}
