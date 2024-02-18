package br.com.accmd.easyFlix.application.admin;

import br.com.accmd.easyFlix.domain.entities.EasyAdmin;
import org.springframework.stereotype.Component;

@Component
public class EasyAdminMapper {
    public EasyAdmin mapToAdmin(EasyAdminDto dto){
        return EasyAdmin.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(dto.getPassword())
                .build();
    }
}
