package com.megaprofer.academic.authentication.presentation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = "permissionId")
@ToString(of = "permissionId")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PermissionPresenter {
    private UUID permissionId;
    private String name;
    private String domainAction;
    private boolean active;
}
