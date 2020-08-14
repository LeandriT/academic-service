package com.megaprofer.academic.authentication.service;

import com.megaprofer.academic.authentication.entity.User;
import com.megaprofer.academic.authentication.presentation.UserAuthorizationPresenter;
import com.megaprofer.academic.authentication.presentation.UserPresenter;
import com.megaprofer.academic.authentication.presentation.Paginator;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public interface UserService {

    User saveUser(@NotNull UserPresenter userPresenter);

    User findCurrentUser();

    Paginator getPaginatedUsers(@NotNull @Size(min = 3) String searchValue,
                                @Min(0) Integer page, @NotNull @Min(1) @Max(50)  Integer size);

    UserAuthorizationPresenter saveUserAuthorization(UUID userId);

    User findUserFromAuthorizationToken(String authorizationToken);
}
