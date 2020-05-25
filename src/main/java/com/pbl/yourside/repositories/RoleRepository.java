package com.pbl.yourside.repositories;

import com.pbl.yourside.entities.Role;
import com.pbl.yourside.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
