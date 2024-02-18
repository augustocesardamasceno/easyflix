package br.com.accmd.easyFlix.infra.repository;

import br.com.accmd.easyFlix.domain.entities.EasyAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EasyAdminRepo extends JpaRepository<EasyAdmin, String>, JpaSpecificationExecutor<EasyAdmin> {
    Optional<EasyAdmin> findById(String id);
    EasyAdmin findByEmail(String email);

}
