package br.com.accmd.easyFlix.application.admin;
import br.com.accmd.easyFlix.domain.entities.EasyAdmin;
import br.com.accmd.easyFlix.domain.exception.DuplicatedTupleException;
import br.com.accmd.easyFlix.domain.service.EasyAdminService;
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
@RequestMapping("/v1/admins")
@RequiredArgsConstructor
public class EasyAdminController {

    private final EasyAdminService adminService;
    private final EasyAdminMapper adminMapper;

    @GetMapping
    public ResponseEntity<Page<EasyAdminDto>> listAdmins(@PageableDefault(size = 10, sort = {"name"}) Pageable pageableAdmin) {
        var page = adminService.getAllAdmins(pageableAdmin);
        if (page.hasContent()) {
            return ResponseEntity.status(HttpStatus.OK).body(page);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    public ResponseEntity save(@RequestBody EasyAdminDto easyAdminDto) {
        try {
            EasyAdmin easyAdmin = adminMapper.mapToAdmin(easyAdminDto);
            adminService.save(easyAdmin);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicatedTupleException e) {
            Map<String, String> jsonResult = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResult);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable String id, @RequestBody EasyAdminDto dto) {
        try {
            Optional<EasyAdmin> existingAdmin = adminService.getById(id);

            if (existingAdmin.isPresent()) {
                EasyAdmin updatedAdmin = adminMapper.mapToAdmin(dto);
                updatedAdmin.setId(id);
                adminService.update(updatedAdmin);

                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (DuplicatedTupleException e) {
            Map<String, String> jsonResult = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResult);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        boolean deleted = adminService.delete(id);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping("/auth")
    public ResponseEntity authenticate(@RequestBody CredentialsAdminDto credentials){
        var token = adminService.authenticate(credentials.getEmail(), credentials.getPassword());

        if(token == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(token);
    }

}
