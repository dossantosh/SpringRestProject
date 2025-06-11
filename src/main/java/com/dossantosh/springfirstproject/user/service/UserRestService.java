package com.dossantosh.springfirstproject.user.service;

import com.dossantosh.springfirstproject.user.models.User;
import com.dossantosh.springfirstproject.user.models.permissions.Modules;
import com.dossantosh.springfirstproject.user.models.permissions.Roles;
import com.dossantosh.springfirstproject.user.models.permissions.Submodules;
import com.dossantosh.springfirstproject.user.repository.UserRepository;
import com.dossantosh.springfirstproject.user.service.permissions.ModuleService;
import com.dossantosh.springfirstproject.user.service.permissions.RoleService;
import com.dossantosh.springfirstproject.user.service.permissions.SubmoduleService;
import com.dossantosh.springfirstproject.user.utils.UserRestDTO;
import com.dossantosh.springfirstproject.user.utils.UserSpecifications;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserRestService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModuleService moduleService;
    private final SubmoduleService submoduleService;

    // --- Métodos REST/DTO ---

    public Page<UserRestDTO> findByFiltersDTO(Long id, String username, String email, int page, int size, String sortby, String direction) {
        Sort.Direction sortDirection = Sort.Direction.fromOptionalString(direction).orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortby));
        Specification<User> spec = UserSpecifications.filter(id, username, email);
        Page<User> users = userRepository.findAll(spec, pageable);
        return users.map(this::toDTO);
    }

    public UserRestDTO findDTOById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario con ID " + id + " no encontrado"));
        return toDTO(user);
    }

    public UserRestDTO saveUserRestDTO(UserRestDTO dto) {
        User user = dto.getId() != null
                ? userRepository.findById(dto.getId()).orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"))
                : new User();

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setEnabled(dto.getEnabled());

        // Mapear roles, módulos, submódulos
        Set<Roles> roles = dto.getRoles() != null
                ? dto.getRoles().stream().map(roleService::findById).collect(Collectors.toSet())
                : Collections.emptySet();
        Set<Modules> modules = dto.getModules() != null
                ? dto.getModules().stream().map(moduleService::findById).collect(Collectors.toSet())
                : Collections.emptySet();
        Set<Submodules> submodules = dto.getSubmodules() != null
                ? dto.getSubmodules().stream().map(submoduleService::findById).collect(Collectors.toSet())
                : Collections.emptySet();

        user.setRoles(roles);
        user.setModules(modules);
        user.setSubmodules(submodules);

        User saved = userRepository.save(user);
        return toDTO(saved);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // --- Métodos para listas de selección ---

    public Map<Long, String> getAllRoles() {
        return roleService.findAll().stream()
                .collect(Collectors.toMap(Roles::getId, Roles::getName, (a, b) -> a, LinkedHashMap::new));
    }

    public Map<Long, String> getAllModules() {
        return moduleService.findAll().stream()
                .collect(Collectors.toMap(Modules::getId, Modules::getName, (a, b) -> a, LinkedHashMap::new));
    }

    public Map<Long, String> getAllSubmodules() {
        return submoduleService.findAll().stream()
                .collect(Collectors.toMap(Submodules::getId, Submodules::getName, (a, b) -> a, LinkedHashMap::new));
    }

    // --- Conversión Entity <-> DTO ---

    public UserRestDTO toDTO(User user) {
        UserRestDTO dto = new UserRestDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setEnabled(user.getEnabled());
        dto.setRoles(user.getRoles() != null
                ? user.getRoles().stream().map(Roles::getId).collect(Collectors.toSet())
                : Collections.emptySet());
        dto.setModules(user.getModules() != null
                ? user.getModules().stream().map(Modules::getId).collect(Collectors.toSet())
                : Collections.emptySet());
        dto.setSubmodules(user.getSubmodules() != null
                ? user.getSubmodules().stream().map(Submodules::getId).collect(Collectors.toSet())
                : Collections.emptySet());
        return dto;
    }
}