package br.com.accmd.easyFlix.application.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EasyAdminDto {
    private String name;
    private String email;
    private String password;
}
