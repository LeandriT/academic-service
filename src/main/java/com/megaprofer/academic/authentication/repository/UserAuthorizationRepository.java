package com.megaprofer.academic.authentication.repository;

import com.megaprofer.academic.authentication.entity.UserAuthorization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAuthorizationRepository extends CrudRepository<UserAuthorization, UUID> {
}
