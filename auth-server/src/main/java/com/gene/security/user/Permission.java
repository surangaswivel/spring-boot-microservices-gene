package com.gene.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    DOCUMETN_ADD("addDocument"),
    DOCUMETN_DELETE("deleteDocument"),
    DOCUMETN_UPDATE("updateDocument")

    ;

    @Getter
    private final String permission;
}
