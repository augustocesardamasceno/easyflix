package br.com.accmd.easyFlix.application.users;

import br.com.accmd.easyFlix.application.jwt.user.JwtServiceUser;
import br.com.accmd.easyFlix.domain.AccessToken;
import br.com.accmd.easyFlix.domain.entities.EasyAdmin;
import br.com.accmd.easyFlix.domain.entities.EasyUser;
import br.com.accmd.easyFlix.domain.exception.DuplicatedTupleException;
import br.com.accmd.easyFlix.domain.service.EasyUserService;
import br.com.accmd.easyFlix.infra.repository.EasyUserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EasyUserServiceImpl implements EasyUserService {

    private final EasyUserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceUser jwtService;


    @Override
    public EasyUser getByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public Optional<EasyUser> getById(String id){
        return userRepo.findById(id);
    }

    @Override
    public Page<EasyUserDto> getAllUsers(Pageable pageableEasyUser){
        Page<EasyUser> easyUsers = userRepo.findAll(pageableEasyUser);
        return easyUsers.map(easyUser -> new EasyUserDto(easyUser.getName(), easyUser.getEmail(), easyUser.getPassword()));
    }

    @Override
    @Transactional
    public EasyUser save(EasyUser user) {
        var possibleUser = getByEmail(user.getEmail());

        if (possibleUser != null){
            throw new DuplicatedTupleException("Email já cadastrado!");
        }
        encodePassword(user);
        return userRepo.save(user);
    }

    @Override
    public AccessToken authenticate(String email, String password) {
        var user = getByEmail(email);
        if (user == null){
            return null;
        }

        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if (matches){
            return jwtService.generateToken(user);
        }

        return null;

    }

    @Override
    @Transactional
    public Optional<EasyUser> updateUser(EasyUser easyUser){
        var existingUser = getById(easyUser.getId());

        if (existingUser.isPresent()){
            EasyUser userToUpdate = existingUser.get();
            if (easyUser.getName() != null){
                userToUpdate.setName(easyUser.getName());
            }
            if (easyUser.getEmail() != null){
                userToUpdate.setEmail(easyUser.getEmail());
            }
            if (easyUser.getPassword() != null){
                userToUpdate.setPassword(easyUser.getPassword());
            }
            encodePassword(easyUser);
            return Optional.of(userRepo.save(userToUpdate));
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(String id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

    private void encodePassword(EasyUser user){
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
    }

}
