package com.bodansky.videochat.repository;/*
 * Created by Adam Bodansky on 2017.05.26..
 */

import com.bodansky.videochat.domain.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatUserRepository extends JpaRepository<ChatUser,Long> {
    ChatUser findByEmail(String email);
}
