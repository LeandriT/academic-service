package com.megaprofer.academic.authentication.repository;

import com.megaprofer.academic.authentication.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends CrudRepository<Role, UUID> {

    @Query("select r from Role r left join fetch r.permissions where r.name=:name")
    Optional<Role> findByName(@Param("name") String name);
}
