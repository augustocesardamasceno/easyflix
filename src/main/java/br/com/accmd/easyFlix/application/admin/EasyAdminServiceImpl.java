package br.com.accmd.easyFlix.application.admin;

import br.com.accmd.easyFlix.application.jwt.admin.JwtServiceAdmin;
import br.com.accmd.easyFlix.domain.AccessToken;
import br.com.accmd.easyFlix.domain.entities.EasyAdmin;
import br.com.accmd.easyFlix.domain.exception.DuplicatedTupleException;
import br.com.accmd.easyFlix.domain.service.EasyAdminService;
import br.com.accmd.easyFlix.infra.repository.EasyAdminRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EasyAdminServiceImpl implements EasyAdminService {

    private final EasyAdminRepo adminRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceAdmin jwtService;


    @Override
    public EasyAdmin getByEmail(String email) {
        return adminRepo.findByEmail(email);
    }

    @Override
    public Optional<EasyAdmin> getById(String id){
        return adminRepo.findById(id);
    }

    @Override
    public Page<EasyAdminDto> getAllAdmins(Pageable pageable) {
        Page<EasyAdmin> easyAdmins = adminRepo.findAll(pageable);
        return easyAdmins.map(easyAdmin -> new EasyAdminDto(easyAdmin.getName(), easyAdmin.getEmail(), easyAdmin.getPassword()));
    }

    @Override
    @Transactional
    public EasyAdmin save(EasyAdmin admin) {
        var possibleAdmin = getByEmail(admin.getEmail());

        if (possibleAdmin != null){
            throw new DuplicatedTupleException("Email de admin já cadastrado");
        }
        encodePassword(admin);
        return adminRepo.save(admin);
    }



    @Override
    public Optional<EasyAdmin> update(EasyAdmin easyAdmin) {

        var existingAdmin = getById(easyAdmin.getId());

        if (existingAdmin.isPresent()){
            EasyAdmin adminToUpdate = existingAdmin.get();
            if (easyAdmin.getName() != null){
                adminToUpdate.setName(easyAdmin.getName());
            }
            if (easyAdmin.getEmail() != null){
                adminToUpdate.setEmail(easyAdmin.getEmail());
            }
            if (easyAdmin.getPassword() != null){
                adminToUpdate.setPassword(easyAdmin.getPassword());
            }
            encodePassword(adminToUpdate);
            return Optional.of(adminRepo.save(adminToUpdate));
        }

        return Optional.empty();
    }

    @Override
    public boolean delete(String id) {
        if (adminRepo.existsById(id)) {
            adminRepo.deleteById(id);
            return true;
        }
        return false;
    }


    @Override
    public AccessToken authenticate(String email, String password) {
        var admin = getByEmail(email);
        if(admin == null){
            return null;
        }

        boolean matches = passwordEncoder.matches(password, admin.getPassword());

        if(matches){
            return jwtService.generateToken(admin);
        }

        return null;
    }

    private void encodePassword(EasyAdmin admin){
        String rawPassword = admin.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        admin.setPassword(encodedPassword);
    }


}
