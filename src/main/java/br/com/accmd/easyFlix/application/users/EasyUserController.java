package br.com.accmd.easyFlix.application.users;
import br.com.accmd.easyFlix.domain.entities.EasyUser;
import br.com.accmd.easyFlix.domain.exception.DuplicatedTupleException;
import br.com.accmd.easyFlix.domain.service.EasyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class EasyUserController {

    private final EasyUserService userService;
    private final EasyUserMapper userMapper;



    @GetMapping
    public ResponseEntity<Page<EasyUserDto>> listUsers(@PageableDefault(size = 10, sort = {"name"}) Pageable pageableUser){
        var page = userService.getAllUsers(pageableUser);
        if (page.hasContent()){
            return ResponseEntity.status(HttpStatus.OK).body(page);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity save (@RequestBody EasyUserDto easyUserDto){
        try{
            EasyUser easyUser = userMapper.mapToUser(easyUserDto);
            userService.save(easyUser);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicatedTupleException e){
            Map<String, String> jsonResultado = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResultado);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable String id, @RequestBody EasyUserDto dto){
        try {
            Optional<EasyUser> existingUser = userService.getById(id);

            if (existingUser.isPresent()) {
                EasyUser updatedUser = userMapper.mapToUser(dto);
                updatedUser.setId(id);
                userService.updateUser(updatedUser);

                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (DuplicatedTupleException e){
            Map<String, String> jsonResultado = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResultado);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        boolean deleted = userService.delete(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("auth")
    public ResponseEntity authenticate(@RequestBody CredentialsDto credentialsDto){
        var token = userService.authenticate(credentialsDto.getEmail(), credentialsDto.getPassword());
        if (token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(token);
    }

}
