package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.AdminDTO;
import com.freelancemarketplace.backend.exception.AdminException;
import com.freelancemarketplace.backend.mapper.AdminMapper;
import com.freelancemarketplace.backend.model.AdminsModel;
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
    public AdminsModel createAdmin(AdminDTO adminDTO) {

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
            AdminsModel adminEntity = adminMapper.toEntity(adminDTO);
            adminEntity.setCreated_at(Timestamp.from(Instant.now()));
            AdminsModel savedAdmin = adminsRepository.save(adminEntity);
            logger.info("Successfully created admin with ID:" + savedAdmin.getId());
            return savedAdmin;
        } catch (DataAccessException e) {
            logger.error("Failed to create admin: " + e.getMessage());
            throw new AdminException("Can not create admin");
        }

    }

    @Override
    public Boolean update(AdminDTO adminDTO) {
        return null;
    }

    @Override
    public Boolean delete(Long adminId) {
        return null;
    }
}
