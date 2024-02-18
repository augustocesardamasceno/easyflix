package br.com.accmd.easyFlix.application.users;

import br.com.accmd.easyFlix.domain.entities.EasyUser;
import org.springframework.stereotype.Component;

@Component
public class EasyUserMapper {
    public EasyUser mapToUser(EasyUserDto dto){
        return EasyUser.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(dto.getPassword())
                .build();
    }



}
