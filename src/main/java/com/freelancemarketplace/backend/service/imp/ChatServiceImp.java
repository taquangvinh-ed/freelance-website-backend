package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.ContactInfoDTO;
import com.freelancemarketplace.backend.dto.ConversationDTO;
import com.freelancemarketplace.backend.dto.MessageDTO;
import com.freelancemarketplace.backend.enums.UserRoles;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.MessageMapper;
import com.freelancemarketplace.backend.model.ClientModel;
import com.freelancemarketplace.backend.model.FreelancerModel;
import com.freelancemarketplace.backend.model.MessageModel;
import com.freelancemarketplace.backend.model.UserModel;
import com.freelancemarketplace.backend.repository.ClientsRepository;
import com.freelancemarketplace.backend.repository.FreelancersRepository;
import com.freelancemarketplace.backend.repository.MessagesRepository;
import com.freelancemarketplace.backend.repository.UserRepository;
import com.freelancemarketplace.backend.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ChatServiceImp implements ChatService {

    private MessagesRepository messagesRepository;
    private MessageMapper messageMapper;
    private UserRepository userModelRepository;
    private FreelancersRepository freelancersRepository;
    private ClientsRepository clientsRepository;


    @Override
    public MessageDTO saveMessageToDatabase(Long serderId, MessageDTO messageDTO) {

        if (messageDTO.getRoomId() == null && messageDTO.getTeamId() == null)
            throw new IllegalArgumentException("Message has not destination");

        if (messageDTO.getContent() == null)
            throw new IllegalArgumentException("Message has no content");

        MessageModel newMessage = messageMapper.toEntity(messageDTO);
        newMessage.setSenderId(serderId);
        newMessage.setCreatedAt(Timestamp.from(Instant.now()));
        MessageModel savedMessage = messagesRepository.save(newMessage);
        log.info("message is saved successfully");
        return messageMapper.toDto(savedMessage);


    }


    @Override
    public ContactInfoDTO getContactInfo(Long userId) {
        UserModel user = userModelRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + userId)
        );

        ContactInfoDTO contactInfoDTO = new ContactInfoDTO();

        if (user.getRole() == UserRoles.FREELANCER) {
            FreelancerModel freelancer = freelancersRepository.findById(userId).orElseThrow(
                    () -> new ResourceNotFoundException("freelancer not found with id: " + userId)
            );

            contactInfoDTO.setUserId(userId);
            contactInfoDTO.setFirstName(freelancer.getFirstName());
            contactInfoDTO.setLastName(freelancer.getLastName());
            contactInfoDTO.setAvatar(freelancer.getProfilePicture());
            contactInfoDTO.setRole(UserRoles.FREELANCER.toString());

        }

        if (user.getRole() == UserRoles.CLIENT) {
            ClientModel client = clientsRepository.findById(userId).orElseThrow(
                    () -> new ResourceNotFoundException("client not found with id: " + userId)
            );
            contactInfoDTO.setUserId(userId);
            contactInfoDTO.setFirstName(client.getFirstName());
            contactInfoDTO.setLastName(client.getLastName());
            contactInfoDTO.setAvatar(client.getProfilePicture());
            contactInfoDTO.setRole(UserRoles.CLIENT.toString());
        }

        return contactInfoDTO;
    }


    List<ConversationDTO> getRecentConversation(Long userId) {

        List<MessageModel> allRelevantMessages = messagesRepository.findBySenderIdOrReceiverId(userId, userId);

        Map<Long, List<MessageModel>> messagesGroupByPartner = allRelevantMessages.stream().collect(Collectors.groupingBy(message -> {
            return message.getSenderId().equals(userId) ? message.getReceiverId() : message.getSenderId();
        }));

        List<ConversationDTO> conversationList = messagesGroupByPartner.entrySet().stream().map(
                        entry -> {
                            Long partnerId = entry.getKey();
                            List<MessageModel> conversationMessages = entry.getValue();

                            //Find last Message
                            MessageModel lastMessage = conversationMessages.stream().max(Comparator.comparing(MessageModel::getSentAt)).orElse(null);
                            if (lastMessage == null)
                                return null;


//                            long unreadCount = conversationMessages.stream()
//                                    .filter(msg -> msg.getReceiverId().equals(userId)) // Chỉ xét tin nhắn mà MÌNH là người nhận
//                                    .filter(msg -> !msg.isRead()) // Giả định MessageModel có trường isRead()
//                                    .count();
//
//                            conversation.setUnreadCount((int) unreadCount);


                            ConversationDTO conversation = new ConversationDTO();

                            ContactInfoDTO contactInfo = getContactInfo(partnerId);

                            conversation.setPartnerFullName(contactInfo.getLastName() + " " + contactInfo.getFirstName());
                            conversation.setPartnerAvatar(contactInfo.getAvatar());
                            conversation.setPartnerRole(contactInfo.getRole());
                            conversation.setLastMessage(lastMessage.getContent());
                            conversation.setLastMessageTime(lastMessage.getSentAt());

                            return conversation;
                        }
                ).filter(Objects::nonNull)
                .sorted(Comparator.comparing(ConversationDTO::getLastMessageTime)
                        .reversed())
                .toList();

        return conversationList;

    }

}
