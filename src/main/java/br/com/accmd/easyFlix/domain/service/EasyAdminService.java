package br.com.accmd.easyFlix.domain.service;

import br.com.accmd.easyFlix.application.admin.EasyAdminDto;
import br.com.accmd.easyFlix.domain.AccessToken;
import br.com.accmd.easyFlix.domain.entities.EasyAdmin;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EasyAdminService {

    EasyAdmin getByEmail(String email);
    Optional<EasyAdmin> getById(String id);

    Page<EasyAdminDto> getAllAdmins(Pageable pageable);

    EasyAdmin save (EasyAdmin admin);

    AccessToken authenticate(String email, String password);
    Optional<EasyAdmin> update(EasyAdmin easyAdmin);

    boolean delete(String id);



}
