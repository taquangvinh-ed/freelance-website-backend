package com.freelancemarketplace.backend.service;

import com.freelancemarketplace.backend.dto.Q_ADTO;

import java.util.List;

public interface Q_AService {

    Q_ADTO createQA(Q_ADTO qADTO);

    Q_ADTO updateQA(Long qandAId, Q_ADTO qADTO);

    void deleteQA(Long qandAId);

    List<Q_ADTO> getAll();

    List<Q_ADTO>getAllByTag(String tag);

    List<Q_ADTO>getAllByAdminId(Long adminId);

}
