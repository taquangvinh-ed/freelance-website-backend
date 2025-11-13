package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.exception.AdminException;
import com.freelancemarketplace.backend.mapper.AdminMapper;
import com.freelancemarketplace.backend.model.AdminModel;
import com.freelancemarketplace.backend.repository.AdminsRepository;
import com.freelancemarketplace.backend.service.AdminService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class AdminServiceImp implements AdminService {

    private AdminMapper adminMapper;
    private AdminsRepository adminsRepository;


    public AdminServiceImp(AdminsRepository adminsRepository,
                           AdminMapper adminMapper) {
        this.adminsRepository = adminsRepository;
        this.adminMapper = adminMapper;
    }

    Logger logger = LoggerFactory.getLogger(AdminServiceImp.class.getName());


//    @Override
//    @Transactional
//    public AdminModel createAdmin(AdminDTO adminDTO) {
//
//        if (adminsRepository.existsByEmail(adminDTO.getEmail())) {
//            throw new AdminException("Admin with email:  " + adminDTO.getEmail()
//                    + " already exist");
//        }
//
//        if (adminsRepository.existsByUsername(adminDTO.getUsername())) {
//            throw new AdminException("Admin with username:  " + adminDTO.getUsername()
//                    + " already exist");
//        }
//
//        if (adminsRepository.existsByPhoneNumber(adminDTO.getPhoneNumber())) {
//            throw new AdminException("Admin with email:  " + adminDTO.getPhoneNumber()
//                    + " already exist");
//        }
//
//
//        AdminModel adminEntity = adminMapper.toEntity(adminDTO);
//        adminEntity.setCreatedAt(Timestamp.from(Instant.now()));
//        AdminModel savedAdmin = adminsRepository.save(adminEntity);
//        logger.info("Successfully created admin with ID:" + savedAdmin.getAdminId());
//        return savedAdmin;
//
//
//    }

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
        logger.info("Admin account has been updated successfully");
        return adminMapper.toDTO(savedAdmin);


    }

    @Override
    @Transactional
    public Boolean delete(Long adminId) {
        if (adminsRepository.existsById(adminId)) {
            adminsRepository.deleteById(adminId);
            logger.info("admin account with id: {} is deleted successfully", adminId);
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
