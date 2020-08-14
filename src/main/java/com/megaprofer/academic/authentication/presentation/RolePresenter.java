package com.megaprofer.academic.authentication.presentation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "roleId")
@ToString(of = "roleId")
@JsonIgnoreProperties(ignoreUnknown = true)
public class RolePresenter {
    private UUID roleId;
    private String name;
    @Builder.Default
    private List<PermissionPresenter> permissionPresenters = new ArrayList<>();
}
