package br.com.accmd.easyFlix.infra.repository;

import br.com.accmd.easyFlix.domain.entities.EasyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EasyUserRepo extends JpaRepository<EasyUser, String>, JpaSpecificationExecutor<EasyUser> {
    EasyUser findByEmail(String email);
    Optional<EasyUser> findById(String id);

}
