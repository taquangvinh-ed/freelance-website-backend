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
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

@Service
@Slf4j
public class AdminServiceImp implements AdminService {

    private AdminMapper adminMapper;
    private AdminsRepository adminsRepository;



    public AdminServiceImp(AdminsRepository adminsRepository,
                           AdminMapper adminMapper) {
        this.adminsRepository = adminsRepository;
        this.adminMapper =adminMapper;
    }

    Logger logger = LoggerFactory.getLogger(AdminServiceImp.class.getName());


    @Override
    @Transactional
    public AdminModel createAdmin(AdminDTO adminDTO) {

        if(adminsRepository.existsByEmail(adminDTO.getEmail())){
            throw new AdminException("Admin with email:  " + adminDTO.getEmail()
            + " already exist");
        }

        if(adminsRepository.existsByUsername(adminDTO.getUsername())){
            throw new AdminException("Admin with username:  " + adminDTO.getUsername()
                    + " already exist");
        }

        if(adminsRepository.existsByPhoneNumber(adminDTO.getPhoneNumber())){
            throw new AdminException("Admin with email:  " + adminDTO.getPhoneNumber()
                    + " already exist");
        }

        try{
            AdminModel adminEntity = adminMapper.toEntity(adminDTO);
            adminEntity.setCreatedAt(Timestamp.from(Instant.now()));
            AdminModel savedAdmin = adminsRepository.save(adminEntity);
            logger.info("Successfully created admin with ID:" + savedAdmin.getAdminId());
            return savedAdmin;
        } catch (DataAccessException e) {
            logger.error("Failed to create admin: " + e.getMessage());
            throw new AdminException("Can not create admin");
        }

    }

    @Transactional
    @Override
    public AdminDTO update(Long adminId, AdminDTO adminDTO) {
            try {
                AdminModel existedAdmin = adminsRepository.findById(adminId).orElseThrow(()->new AdminException("Admin account with id: " + adminId + " not found"));
                System.out.println("Before update");
                System.out.println(existedAdmin.getUsername());
                System.out.println(existedAdmin.getLastName());
                System.out.println(existedAdmin.getEmail());


                if(adminDTO.getFirstName() != null){
                    existedAdmin.setFirstName( adminDTO.getFirstName() );
                }
                if(adminDTO.getLastName() != null){
                    existedAdmin.setLastName( adminDTO.getLastName() );
                }
                if(adminDTO.getUsername() != null){
                    existedAdmin.setUsername( adminDTO.getUsername() );
                }
                if(adminDTO.getEmail() != null){
                    existedAdmin.setEmail( adminDTO.getEmail() );
                }
                if(adminDTO.getPhoneNumber() != null){
                    existedAdmin.setPhoneNumber( adminDTO.getPhoneNumber() );
                }
                if(adminDTO.getTitle() != null){
                    existedAdmin.setTitle( adminDTO.getTitle() );
                }

                System.out.println("After update");
                System.out.println(existedAdmin.getUsername());
                System.out.println(existedAdmin.getLastName());
                System.out.println(existedAdmin.getEmail());

                AdminModel savedAdmin = adminsRepository.save(existedAdmin);
                    logger.info("Admin account has been updated successfully");
                    return adminMapper.toDTO(savedAdmin);

            }catch (DataAccessException e){
                logger.error("Failed to update admin account: " + e.getMessage());
                throw new RuntimeException("Cannot update admin account");
            }


    }

    @Override
    @Transactional
    public Boolean delete(Long adminId) {
        if(adminsRepository.existsById(adminId)) {
            try {
                adminsRepository.deleteById(adminId);
                if (!adminsRepository.existsById(adminId)) {
                    logger.info("Admin account with id: " + adminId + "was deleted successfully");
                    return true;
                } else {
                    logger.error("Cannot delete admin account with id: " + adminId);
                    return false;
                }
            } catch (RuntimeException e) {
                throw new AdminException("Has error when trying to delete this admin account" + e);
            }
        }else{
            logger.info("Admin account with id: " + adminId + " has not found");
            return false;
        }
    }

    @Override
    public AdminDTO getProfileAdmin(Long adminId) {
        try{
            AdminModel adminModel = adminsRepository.findById(adminId).orElseThrow(()->new AdminException("Admin account with id: " + adminId + " not found"));
            return adminMapper.toDTO(adminModel);
        }catch (DataAccessException e) {
            logger.error("Database error access admin ID {}: {}", adminId, e.getMessage());
            throw new AdminException("Cannot access admin account: " + e.getMessage());
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<AdminDTO> getAllAdmins() {
        return Set.of();
    }

}
