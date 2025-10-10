package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<MessageModel, Long> {
    List<MessageModel> findByRoomId(Long roomId);

    List<MessageModel> findByTeamId(Long teamId);

    List<MessageModel> findBySenderIdOrReceiverId(Long senderId, Long receiverId);
}