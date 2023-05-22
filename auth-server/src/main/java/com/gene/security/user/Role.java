package com.gene.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gene.security.user.Permission.*;


@RequiredArgsConstructor
public enum Role {

  USER(Collections.emptySet()),
  ADMIN(
          Set.of(
                  DOCUMETN_ADD,
                  DOCUMETN_UPDATE,
                  DOCUMETN_DELETE
          )
  ),
  MANAGER(
          Set.of(
                  DOCUMETN_ADD,
                  DOCUMETN_UPDATE
          )
  ),


  EMPLOY(
          Set.of(
          DOCUMETN_UPDATE
          )
  )

  ;

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }
}
