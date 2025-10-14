package com.freelancemarketplace.backend.repository;

import com.freelancemarketplace.backend.model.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<MessageModel, Long> {
    List<MessageModel> findByRoomId(String roomId);

    List<MessageModel> findBySenderIdOrReceiverId(Long senderId, Long receiverId);

    List<MessageModel> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    @Query("SELECT m FROM MessageModel m WHERE " +
            "(m.senderId = :user1Id AND m.receiverId = :user2Id) OR " +
            "(m.senderId = :user2Id AND m.receiverId = :user1Id) " +
            "ORDER BY m.sentAt ASC")
    List<MessageModel> findChatHistory(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);


}