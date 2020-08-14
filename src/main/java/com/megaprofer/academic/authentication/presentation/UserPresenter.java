package com.megaprofer.academic.authentication.presentation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = "userId")
@ToString(of = "userId")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPresenter implements Comparable<UserPresenter> {
    private UUID userId;
    @NotNull
    private String userName;
    @NotNull
    private String password;
    @NotNull
    private String fullName;
    @NotNull
    private String dni;
    @NotNull
    private String referenceId;
    @Builder.Default
    private List<RolePresenter> rolePresenters = new ArrayList<>();

    @Override
    public int compareTo(UserPresenter userPresenter) {
        return this.userName.compareTo(userPresenter.userName);
    }
}
