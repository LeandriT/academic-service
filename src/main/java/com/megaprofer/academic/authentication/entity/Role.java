package com.megaprofer.academic.authentication.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="roles")
@Getter
@Setter
@EqualsAndHashCode(of = "roleId")
@ToString(of = "roleId")
@Builder
public class Role {
    @Id
    @GeneratedValue
    private UUID roleId;
    @Column(unique=true)
    @NotNull
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable()
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();
    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.ALL})
    @Builder.Default
    private Set<User> users = new HashSet<>();
}
