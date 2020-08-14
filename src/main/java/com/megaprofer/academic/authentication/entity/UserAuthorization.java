package com.megaprofer.academic.authentication.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = "userAuthorizationId")
@ToString(of = "userAuthorizationId")
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="user_authorizations")
@Entity
@Builder
public class UserAuthorization {
    @Id
    @GeneratedValue
    private UUID userAuthorizationId;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @NotNull
    @Size(min = 1)
    private String authorization;
    @Builder.Default
    private boolean active = true;
}
