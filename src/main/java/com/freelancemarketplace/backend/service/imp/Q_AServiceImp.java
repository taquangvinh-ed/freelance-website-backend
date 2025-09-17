package com.freelancemarketplace.backend.service.imp;

import com.freelancemarketplace.backend.dto.Q_ADTO;
import com.freelancemarketplace.backend.exception.AdminException;
import com.freelancemarketplace.backend.exception.Q_AException;
import com.freelancemarketplace.backend.exception.ResourceNotFoundException;
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
            Q_AModel qAModel = qAMapper.toEntity(qADTO);
            AdminModel adminModel = adminsRepository.findById(qADTO.getAdminId()).orElseThrow(
                    ()->new AdminException("Admin account with id: " + qADTO.getAdminId() + " not found"));
            qAModel.setAdmin(adminModel);
            Q_AModel savedQA = qARepository.save(qAModel);
            logger.info("create Q_A successfully");
            return qAMapper.toDTO(savedQA);
    }

    @Override
    public Q_ADTO updateQA(Long qandAId, Q_ADTO qADTO) {
        Q_AModel qAModel = qARepository.findById(qandAId).orElseThrow(
                ()-> new ResourceNotFoundException("Q_A with id: " + qandAId +" not found")
        );

        if(qADTO.getQuestion() != null)
            qAModel.setQuestion(qADTO.getQuestion());

        if(qADTO.getAnswer() != null)
            qAModel.setAnswer(qADTO.getAnswer());

        if(qADTO.getTag() != null)
            qAModel.setTag(qADTO.getTag());

        Q_AModel savdeQA = qARepository.save(qAModel);


        return qAMapper.toDTO(qAModel);
    }

    @Override
    public void deleteQA(Long qandAId) {
        Q_AModel qa = qARepository.findById(qandAId).orElseThrow(
                () -> new ResourceNotFoundException("QA with id " + qandAId + " not found")
        );
        qARepository.deleteById(qandAId);

    }

    @Override
    public List<Q_ADTO> getAll() {
        List<Q_AModel> Q_As = qARepository.findAll();
        return qAMapper.toDTOs(Q_As);
    }

    @Override
    public List<Q_ADTO> getAllByTag(String tag) {
        List<Q_AModel> Q_As = qARepository.findAllByTag(tag);
        return qAMapper.toDTOs(Q_As);
    }

    @Override
    public List<Q_ADTO> getAllByAdminId(Long adminId) {
        AdminModel admin = adminsRepository.findById(adminId).orElseThrow(
                ()-> new ResourceNotFoundException("Admin with id: " + adminId + " not found"));

        List<Q_AModel> QAs = qARepository.findAllByAdmin(admin);
        return qAMapper.toDTOs(QAs);
    }
}
