package com.micheladas.chelas.config;

import com.micheladas.chelas.entidad.Usuario;
import com.micheladas.chelas.entidad.Rol;
import com.micheladas.chelas.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepositorio usuarioRepositorio;
    private final BCryptPasswordEncoder passwordEncoder;

    // Inyectamos los valores desde el entorno
    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    public DataInitializer(UsuarioRepositorio usuarioRepositorio, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Verificamos si el admin ya existe para no duplicarlo
        if (usuarioRepositorio.findByEmail(adminEmail) == null) {

            // 2. Creamos el usuario con todos los datos
            Usuario admin = Usuario.builder()
                    .nombre("Administrador")
                    .apellido("Sistema")
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .mobileNumber("1299997836")
                    .roles(Arrays.asList(new Rol("ROLE_ADMIN")))
                    .build();// <--- AQUÍ LE DAMOS EL PODER

            usuarioRepositorio.save(admin);
            System.out.println("ADMIN CONFIGURADO CORRECTAMENTE");
        }
    }
}
