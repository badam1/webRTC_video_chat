package com.bodansky.videochat.domain;/*
 * Created by Adam Bodansky on 2017.05.26..
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Role {
    USER("User"),
    ADMIN("Admin");
    private String roleName;
}
