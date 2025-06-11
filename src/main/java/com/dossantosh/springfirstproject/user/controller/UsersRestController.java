package com.dossantosh.springfirstproject.user.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.dossantosh.springfirstproject.user.service.UserRestService;
import com.dossantosh.springfirstproject.user.utils.UserRestDTO;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UsersRestController {

    private final UserRestService userRestService;

    @GetMapping
    public Page<UserRestDTO> getUsers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return userRestService.findByFiltersDTO(id, username, email, page, size, "id", "ASC");
    }

    @GetMapping("/{id}")
    public UserRestDTO getUser(@PathVariable Long id) {
        return userRestService.findDTOById(id);
    }

    @PostMapping
    public UserRestDTO createUser(@RequestBody UserRestDTO userRestDTO) {
        return userRestService.saveUserRestDTO(userRestDTO);
    }

    @PutMapping("/{id}")
    public UserRestDTO updateUser(@PathVariable Long id, @RequestBody UserRestDTO userRestDTO) {
        userRestDTO.setId(id);
        return userRestService.saveUserRestDTO(userRestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userRestService.deleteUser(id);
    }

    // Métodos para obtener roles, módulos y submódulos
    @GetMapping("/roles")
    public Map<Long, String> getAllRoles() {
        return userRestService.getAllRoles();
    }

    @GetMapping("/modules")
    public Map<Long, String> getAllModules() {
        return userRestService.getAllModules();
    }

    @GetMapping("/submodules")
    public Map<Long, String> getAllSubmodules() {
        return userRestService.getAllSubmodules();
    }
}