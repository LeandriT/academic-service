package com.megaprofer.academic.authentication.repository;

import com.megaprofer.academic.authentication.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

	User findByUsername(String username);

	@Query("select u from User u left join fetch u.roles r " +
			"left join fetch r.permissions p where u.username =:username")
	User findByUsernameWithRoles(@Param("username") String username);

	@Query("select u from User u left join fetch u.roles r " +
			"left join fetch r.permissions p where u.referenceId =:referenceId")
	Optional<User> findByReferenceId(@Param("referenceId") String referenceId);

	@Query( "SELECT u " +
			"FROM User u " +
			"WHERE LOWER(userName) like LOWER(CONCAT('%',:searchValue,'%')) " +
			"OR LOWER(fullName) like LOWER(CONCAT('%',:searchValue,'%')) " )
    Page<User> findUser(@Param("searchValue") String searchValue, Pageable pageable);

	@Query("SELECT u " +
			"FROM UserAuthorization uauth " +
			"JOIN uauth.user u " +
			"WHERE uauth.authorization = :authorizationToken ")
	Optional<User> findUserFromAuthorizationToken(@Param("authorizationToken") String authorizationToken);
}
