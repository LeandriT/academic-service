package com.megaprofer.academic.authentication.service.impl;

import com.megaprofer.academic.authentication.entity.Permission;
import com.megaprofer.academic.authentication.entity.Role;
import com.megaprofer.academic.authentication.entity.User;
import com.megaprofer.academic.authentication.entity.UserAuthorization;
import com.megaprofer.academic.authentication.presentation.PermissionPresenter;
import com.megaprofer.academic.authentication.presentation.RolePresenter;
import com.megaprofer.academic.authentication.presentation.UserAuthorizationPresenter;
import com.megaprofer.academic.authentication.presentation.UserPresenter;
import com.megaprofer.academic.authentication.repository.PermissionRepository;
import com.megaprofer.academic.authentication.repository.RoleRepository;
import com.megaprofer.academic.authentication.repository.UserAuthorizationRepository;
import com.megaprofer.academic.authentication.repository.UserRepository;
import com.megaprofer.academic.authentication.service.UserService;
import com.megaprofer.academic.exception.ValidationException;
import com.megaprofer.academic.authentication.presentation.Paginator;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private UserAuthorizationRepository userAuthorizationRepository;

    public User saveUser(UserPresenter userPresenter){
        User user = new User();
        if(userPresenter.getUserId() != null){
            user = userRepository
                    .findById(userPresenter.getUserId())
                    .orElse(user);
        }else if (userPresenter.getReferenceId() != null){
            user = userRepository
                    .findByReferenceId(userPresenter.getReferenceId())
                    .orElse(user);
        }
        user.setPassword(passwordEncoder.encode(userPresenter.getPassword()));
        user.setUsername(userPresenter.getUserName());
        user.setReferenceId(userPresenter.getReferenceId());
        user.setFullName(userPresenter.getFullName());
        user.setDni(userPresenter.getDni());
        for (RolePresenter rolePresenter : userPresenter.getRolePresenters()) {
            Role role = roleRepository.findByName(rolePresenter.getName()).orElse(new Role());
            role.setName(rolePresenter.getName());
            user.getRoles().add(role);
            role.getPermissions().clear();
            for (PermissionPresenter permissionPresenter : rolePresenter.getPermissionPresenters()) {
                if(permissionPresenter.isActive()){
                    Permission permission = permissionRepository.findByName(permissionPresenter.getName())
                            .orElse(new Permission());
                    permission.setDomainAction(permissionPresenter.getDomainAction());
                    permission.setName(permissionPresenter.getName());
                    permissionRepository.save(permission);
                    role.getPermissions().add(permission);
                }
            }
            roleRepository.save(role);
        }

        userRepository.save(user);
        return user;
    }

    @Override
    public User findCurrentUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return userRepository.findByUsername(userName);
    }

    @Override
    public Paginator getPaginatedUsers(String searchValue, Integer page, Integer size) {
        searchValue = searchValue.replace(' ', '%');
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPages = userRepository.findUser(searchValue, pageable);
        Paginator paginator = new Paginator();
        paginator.setTotalElements(userPages.getTotalElements());
        paginator.setTotalPages(userPages.getTotalPages());
        paginator.setData(new HashSet(userPages.getContent()));
        return paginator;
    }

    @Override
    public UserAuthorizationPresenter saveUserAuthorization(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ValidationException("El usuario no existe"));
        UserAuthorization userAuthorization = new UserAuthorization();
        userAuthorization.setUser(user);
        userAuthorization.setActive(true);
        userAuthorization.setAuthorization(String.valueOf(RandomUtils.nextInt(100_000, 999_999)));
        userAuthorizationRepository.save(userAuthorization);
        UserAuthorizationPresenter userAuthorizationPresenter = new UserAuthorizationPresenter();
        userAuthorizationPresenter.setUserAuthorizationId(userAuthorization.getUserAuthorizationId());
        userAuthorizationPresenter.setAuthorization(userAuthorization.getAuthorization());
        return userAuthorizationPresenter;
    }

    @Override
    public User findUserFromAuthorizationToken(String authorizationToken) {
        return userRepository.findUserFromAuthorizationToken(authorizationToken).orElse(null);
    }
}
