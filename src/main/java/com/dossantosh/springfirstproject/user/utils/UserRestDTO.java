package com.dossantosh.springfirstproject.user.utils;

import java.io.Serializable;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRestDTO implements Serializable {

    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 50, message = "El nombre de usuario no debe superar 50 caracteres")
    private String username;

    @Email(message = "Email no válido")
    @Size(max = 50, message = "El email no debe superar 50 caracteres")
    private String email;

    private Boolean enabled;

    private Set<Long> roles;
    private Set<Long> modules;
    private Set<Long> submodules;
}
