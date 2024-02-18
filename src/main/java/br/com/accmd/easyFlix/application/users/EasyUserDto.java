package br.com.accmd.easyFlix.application.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EasyUserDto {
    private String name;
    private String email;
    private String password;


}
