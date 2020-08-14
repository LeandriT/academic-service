package com.megaprofer.academic.authentication.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@EqualsAndHashCode(of = "userId")
@ToString(of = "userId")
@Entity
@NoArgsConstructor
@Table(name ="users")
public class User {

    @Id
    @GeneratedValue
    @Column(name="user_id")
    private UUID userId;
    @NotNull
    @Column(unique=true)
    private String username;
    @NotNull
    private String password;
    @Size(min = 1)
    private String fullName;
    @NotNull
    @Size(min = 1)
    private String dni;
    @NotNull
    @Column(name="reference_id")
    private String referenceId;
    @ManyToMany()
    @JoinTable(name = "users_roles",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles= new HashSet<>();

}
