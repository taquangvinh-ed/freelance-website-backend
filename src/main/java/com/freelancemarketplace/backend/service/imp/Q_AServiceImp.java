package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.Q_ADTO;
import com.freelancemarketplace.backend.exception.AdminException;
import com.freelancemarketplace.backend.exception.Q_AException;
import com.freelancemarketplace.backend.mapper.Q_AMapper;
import com.freelancemarketplace.backend.model.AdminModel;
import com.freelancemarketplace.backend.model.Q_AModel;
import com.freelancemarketplace.backend.repository.AdminsRepository;
import com.freelancemarketplace.backend.repository.Q_ARepository;
import com.freelancemarketplace.backend.service.Q_AService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class Q_AServiceImp implements Q_AService {

    Logger logger = LoggerFactory.getLogger(Q_AServiceImp.class);

    private final Q_ARepository qARepository;
    private final AdminsRepository adminsRepository;
    private final Q_AMapper qAMapper;

    public Q_AServiceImp(Q_ARepository qARepository,
                         Q_AMapper qAMapper,
                         AdminsRepository adminsRepository) {
        this.qARepository = qARepository;
        this.qAMapper = qAMapper;
        this.adminsRepository = adminsRepository;
    }

    @Override
    public Q_ADTO createQA(Q_ADTO qADTO) {
        try{


            Q_AModel qAModel = qAMapper.toEntity(qADTO);

            AdminModel adminModel = adminsRepository.findById(qADTO.getAdminId()).orElseThrow(
                    ()->new AdminException("Admin account with id: " + qADTO.getAdminId() + " not found"));
            qAModel.setAdmin(adminModel);
            Q_AModel savedQA = qARepository.save(qAModel);
            logger.info("create Q_A successfully");
            return qAMapper.toDTO(savedQA);
        } catch (RuntimeException e) {
            logger.error("Failed to create Q_A");
            throw new Q_AException("Message: " +e);
        }
    }

    @Override
    public Q_ADTO updateQA(Long qandAId, Q_ADTO qADTO) {
        return null;
    }

    @Override
    public Boolean deleteQA(Long qandAId) {
        return null;
    }

    @Override
    public List<Q_ADTO> getAll() {
        return List.of();
    }

    @Override
    public List<Q_ADTO> getAllByTag(String tag) {
        return List.of();
    }

    @Override
    public List<Q_ADTO> getAllByAdminId(Long adminId) {
        return List.of();
    }
}
