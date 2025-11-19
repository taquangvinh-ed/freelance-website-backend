package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.dto.UserDTO;
import com.freelancemarketplace.backend.enums.AccountStatus;
import com.freelancemarketplace.backend.exception.AdminException;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
import com.freelancemarketplace.backend.mapper.AdminMapper;
import com.freelancemarketplace.backend.mapper.UserMapper;
import com.freelancemarketplace.backend.model.AdminModel;
import com.freelancemarketplace.backend.model.UserModel;
import com.freelancemarketplace.backend.repository.AdminsRepository;
import com.freelancemarketplace.backend.repository.UserRepository;
import com.freelancemarketplace.backend.request.DisableUserRequest;
import com.freelancemarketplace.backend.request.UserRequest;
import com.freelancemarketplace.backend.service.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImp implements AdminService {

    private final AdminMapper adminMapper;
    private final AdminsRepository adminsRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public Page<UserDTO> getAllUsers(UserRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by(Sort.Direction.DESC, "userId"));

        Specification<UserModel> spec = Specification.allOf();

        if (StringUtils.hasText(request.getQuery())) {
            String likePattern = "%" + request.getQuery().toLowerCase() + "%";
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder
                    .or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), likePattern),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), likePattern),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), likePattern)

                    ));

        }

        if (request.getStatus() != null) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("accountStatus"), request.getStatus())));
        }

        if (request.getRole() != null) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("role"), request.getRole())));
        }

        Page<UserModel> users = userRepository.findAll(spec, pageable);
        return userMapper.toDtoPage(users);
    }


    @Override
    public UserDTO activateUser(Long userId, Long currentAdminId) throws BadRequestException {
        UserModel user = getUserAndCheckPermission(userId, currentAdminId);
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setDisableReason(null);
        user.setDisableAt(null);
        user.setDisableBy(null);
        UserModel savedUser = userRepository.save(user);
        log.info("Admin {} đã kích hoạt tài khoản userId={}", currentAdminId, userId);
        return userMapper.toDto(user);
    }


    @Override
    public UserDTO updateUserStatus(Long userId, Long currentAdminId, AccountStatus status, String reason) throws BadRequestException {
        if (!StringUtils.hasText(reason) || reason.trim().length() < 10) {
            throw new BadRequestException("Lý do khóa tài khoản phải từ 10 ký tự trở lên");
        }

        UserModel user = getUserAndCheckPermission(userId, currentAdminId);
        AdminModel admin = getCurrentAdmin(currentAdminId);

        user.setAccountStatus(status);
        user.setDisableReason(reason);
        user.setDisableAt(Timestamp.from(Instant.now()));
        user.setDisableBy(admin);
        UserModel savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDTO disableUser(Long userId, Long currentAdminId, DisableUserRequest request) throws BadRequestException {
        return updateUserStatus(userId, currentAdminId, AccountStatus.DISABLED, request.getReason());
    }

    // 4. Cấm vĩnh viễn (Ban)
    @Override
    public UserDTO banUser(Long userId, Long currentAdminId, DisableUserRequest request) throws BadRequestException {
        return updateUserStatus(userId, currentAdminId, AccountStatus.BANNED, request.getReason());
    }


    private UserModel getUserAndCheckPermission(Long userId, Long currentAdminId) throws BadRequestException {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy userId: " + userId));

        if (user.getUserId().equals(currentAdminId)) {
            throw new BadRequestException("Bạn không thể thực hiện hành động này lên chính mình");
        }
        return user;
    }


    private AdminModel getCurrentAdmin(Long currentAdminId) {
        AdminModel admin = adminsRepository.findById(currentAdminId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy admin với userId: " + currentAdminId));
        return admin;
    }


    @Transactional
    @Override
    public AdminDTO update(Long adminId, AdminDTO adminDTO) {

        AdminModel existedAdmin = adminsRepository.findById(adminId).orElseThrow(() -> new AdminException("Admin account with id: " + adminId + " not found"));

        if (adminDTO.getPhoneNumber() != null && !adminDTO.getPhoneNumber().equals(existedAdmin.getPhoneNumber())
                && adminsRepository.existsByPhoneNumber(adminDTO.getPhoneNumber())) {
            throw new AdminException("Phone number " + adminDTO.getPhoneNumber() + " has adready exited");
        }


        if (adminDTO.getFirstName() != null) {
            existedAdmin.setFirstName(adminDTO.getFirstName());
        }
        if (adminDTO.getLastName() != null) {
            existedAdmin.setLastName(adminDTO.getLastName());
        }

        if (adminDTO.getPhoneNumber() != null) {
            existedAdmin.setPhoneNumber(adminDTO.getPhoneNumber());
        }
        if (adminDTO.getTitle() != null) {
            existedAdmin.setTitle(adminDTO.getTitle());
        }


        AdminModel savedAdmin = adminsRepository.save(existedAdmin);
        log.info("Admin account has been updated successfully");
        return adminMapper.toDTO(savedAdmin);

    }

    @Override
    @Transactional
    public Boolean delete(Long adminId) {
        if (adminsRepository.existsById(adminId)) {
            adminsRepository.deleteById(adminId);
            log.info("admin account with id: {} is deleted successfully", adminId);
            return true;
        }
        return false;

    }

    @Override
    public AdminDTO getAdminProfile(Long adminId) {
        AdminModel adminModel = adminsRepository.findById(adminId).orElseThrow(() -> new AdminException("Admin account with id: " + adminId + " not found"));
        return adminMapper.toDTO(adminModel);
    }

    @Override
    public List<AdminDTO> getAllAdmins() {
        List<AdminModel> adminModelList = adminsRepository.findAll();
        List<AdminDTO> adminDTOList = adminMapper.toDTOs(adminModelList);
        return adminDTOList;
    }


}
