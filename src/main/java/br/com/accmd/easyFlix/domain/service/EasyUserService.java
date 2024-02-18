package br.com.accmd.easyFlix.domain.service;

import br.com.accmd.easyFlix.application.users.EasyUserDto;
import br.com.accmd.easyFlix.domain.AccessToken;
import br.com.accmd.easyFlix.domain.entities.EasyUser;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EasyUserService {
    EasyUser getByEmail(String email);

    Optional<EasyUser> getById(String id);

    Page<EasyUserDto> getAllUsers(Pageable pageableEasyUser);


    EasyUser save(EasyUser user);

    AccessToken authenticate(String email, String password);

    Optional<EasyUser> updateUser(EasyUser easyUser);


    boolean delete(String id);
}
