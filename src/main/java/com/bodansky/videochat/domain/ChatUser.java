package com.bodansky.videochat.domain;/*
 * Created by Adam Bodansky on 2017.05.26..
 */

import lombok.*;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;

import static javax.persistence.EnumType.STRING;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"userName"})
@EqualsAndHashCode(of = {"email","id"})
@Entity
@Table(name = "chat_user")
public class ChatUser {
    @Id
    @GeneratedValue
    private Long id;

    @Email
    private String email;

    private String userName;
    private String password;

    @Enumerated(STRING)
    private Role role;
}
