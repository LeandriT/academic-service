package com.megaprofer.academic.authentication.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "permissions")
@Getter
@Setter
@EqualsAndHashCode(of = "permissionId")
@ToString(of = "permissionId")
@Builder
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue
    private UUID permissionId;
    @Column(unique = true)
    @NotNull
    private String name;
    @NotNull
    private String domainAction;
    @Builder.Default
    @ManyToMany(mappedBy = "permissions", cascade = {CascadeType.ALL})
    private Set<Role> roles = new HashSet<>();
}
