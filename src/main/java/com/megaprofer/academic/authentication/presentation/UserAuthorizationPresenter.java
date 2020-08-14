package com.megaprofer.academic.authentication.presentation;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserAuthorizationPresenter {
    private UUID userAuthorizationId;
    private String authorization;
}
