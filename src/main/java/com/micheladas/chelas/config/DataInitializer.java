package com.micheladas.chelas.config;

import com.micheladas.chelas.entity.UserAccount;
import com.micheladas.chelas.entity.Role;
import com.micheladas.chelas.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Arrays;

/**Data seeding component to initialize the database on startup.
 * This class ensures that a default administrative user exists,
 * leveraging externalized configuration for credentials.
 **/
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${app.admin.email:reclutador@ejemplo.com}")
    private String adminEmail;

    @Value("${app.admin.password:password123}")
    private String adminPassword;

    @Value("${app.admin.name:Reclutador}")
    private String adminName;

    @Value("${app.admin.surname:Prueba}")
    private String adminSurname;

    @Value("${app.admin.mobile:1234567890}")
    private String adminMobile;

    public DataInitializer(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (userRepository.findByEmail(adminEmail) == null) {

            UserAccount admin = UserAccount.builder()
                    .name(adminName)
                    .surname(adminSurname)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .mobileNumber(adminMobile)
                    .accountNonLocked(true)
                    .failedAttempts(0)
                    .roles(Arrays.asList(new Role("ROLE_ADMIN")))
                    .build();

            userRepository.save(admin);
            System.out.println("ADMIN CONFIGURADO CORRECTAMENTE");
        }
    }
}
