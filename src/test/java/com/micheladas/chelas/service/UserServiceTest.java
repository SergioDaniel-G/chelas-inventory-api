package com.micheladas.chelas.service;

import com.micheladas.chelas.entity.UserAccount;
import com.micheladas.chelas.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.time.LocalDateTime;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;



@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Debe actualizar exitosamente el LastLogin cuando el usuario existe")
    void shouldUpdateLastLogin_WhenUserExists() {

        // GIVEN

        String email = "test@miches.com";
        LocalDateTime fecha = LocalDateTime.now();
        UserAccount mockUser = new UserAccount();
        mockUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(mockUser);

        // WHEN
        userService.updateLastLoginDate(email, fecha);

        // THEN
        assertEquals(fecha, mockUser.getLastLogin(), "La fecha de login no se actualizó correctamente");
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException cuando el usuario no existe")
    void shouldThrowException_WhenUserDoesNotExist() {
        // GIVEN
        String emailInexistente = "no-existe@miches.com";
        LocalDateTime fecha = LocalDateTime.now();

        when(userRepository.findByEmail(emailInexistente)).thenReturn(null);

        // WHEN & THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateLastLoginDate(emailInexistente, fecha);
        });

        assertTrue(exception.getMessage().contains("Usuario no encontrado"), "El mensaje de error no es el correcto");

        verify(userRepository, never()).save(any(UserAccount.class));
    }

}
