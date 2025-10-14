package com.freelancemarketplace.backend.service.imp;

import ch.qos.logback.core.net.server.Client;
import com.freelancemarketplace.backend.dto.ContactInfoDTO;
import com.freelancemarketplace.backend.dto.ConversationDTO;
import com.freelancemarketplace.backend.dto.CurrentUserProfileDTO;
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
    private UserRepository userRepository;


    @Override
    public MessageDTO saveMessageToDatabase(String roomId, MessageDTO messageDTO) {


        if (messageDTO.getContent() == null)
            throw new IllegalArgumentException("Message has no content");

        MessageModel newMessage = messageMapper.toEntity(messageDTO);
        newMessage.setRoomId(roomId);
        newMessage.setSentAt(Timestamp.from(Instant.now()));
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


    @Override
    public List<ConversationDTO> getRecentConversations(Long userId) {

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


                            String roomId = createRoomId(userId, partnerId);

                            ConversationDTO conversation = new ConversationDTO();

                            ContactInfoDTO contactInfo = getContactInfo(partnerId);

                            conversation.setPartnerId(contactInfo.getUserId());
                            conversation.setPartnerFullName(contactInfo.getLastName() + " " + contactInfo.getFirstName());
                            conversation.setPartnerAvatar(contactInfo.getAvatar());
                            conversation.setPartnerRole(contactInfo.getRole());
                            conversation.setLastMessage(lastMessage.getContent());
                            conversation.setLastMessageTime(lastMessage.getSentAt());
                            conversation.setRoomId(roomId);

                            return conversation;
                        }
                ).filter(Objects::nonNull)
                .sorted(Comparator.comparing(ConversationDTO::getLastMessageTime)
                        .reversed())
                .toList();

        return conversationList;

    }

    @Override
    public String createRoomId(Long userId1, Long userId2) {
        List<Long> ids = Arrays.asList(userId1, userId2);

        String sortedRoomId = ids.stream()
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining("_"));

        return sortedRoomId;
    }


    @Override
    public List<MessageDTO> fetchMessageHistory(Long senderId, Long receiverId){
        List<MessageModel> allRelevantMessages = messagesRepository.findChatHistory(senderId, receiverId);
        return messageMapper.toDTOs(allRelevantMessages);
    }


    @Override
    public CurrentUserProfileDTO getCurrentUserProfile(Long userId){

        CurrentUserProfileDTO userProfile = new CurrentUserProfileDTO();

        UserModel user = userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User not found with id: " + userId)
        );

        String role = user.getRole().toString();
        userProfile.setRole(role);


        if(role.equalsIgnoreCase("FREELANCER")){
            FreelancerModel freelancer = freelancersRepository.findById(userId).orElseThrow(
                    ()-> new ResourceNotFoundException("Freelancer not found with id " + userId)
            );
            userProfile.setFirstName(freelancer.getFirstName());
            userProfile.setLastName(freelancer.getLastName());
            userProfile.setAvatar(freelancer.getAvatar());
        }

       if(role.equalsIgnoreCase("CLIENT")){
           ClientModel client  = clientsRepository.findById(userId).orElseThrow(
                   ()-> new ResourceNotFoundException("Client not found with id " + userId));
           userProfile.setFirstName(client.getFirstName());
           userProfile.setLastName(client.getLastName());
           userProfile.setAvatar(client.getAvatar());
       }

        return  userProfile;
    }

}
