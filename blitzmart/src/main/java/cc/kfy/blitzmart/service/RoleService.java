package cc.kfy.blitzmart.service;

import cc.kfy.blitzmart.domain.Role;

public interface RoleService {
    Role getRoleByUserId(Long userId);
}
