package com.micheladas.chelas.controller.auth;

import com.micheladas.chelas.authservice.RecaptchaService;
import com.micheladas.chelas.entity.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.micheladas.chelas.controller.DTO.UserRegistrationDto;
import com.micheladas.chelas.service.UserService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/registro")
public class UserRegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationController.class);

    private final UserService userService;
    private final RecaptchaService recaptchaService;
    private final JavaMailSender mailSender;

    public UserRegistrationController(UserService userService,
                                      RecaptchaService recaptchaService,JavaMailSender mailSender) {
        this.userService = userService;
        this.recaptchaService = recaptchaService;
        this.mailSender = mailSender;
    }

    @ModelAttribute("usuario")
    public UserRegistrationDto returnNewUserRegistrationDto() {

        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegisterForm() {

        return "auth/registro";
    }

    /**
     * PROCESSES THE REGISTRATION REQUEST, VALIDATING THE reCAPTCHA
     * CHECKING FOR EMAIL UNIQUENESS AND PERSISTING THE NEW USER ACCOUNT
     */

    @PostMapping
    public String userAccountRegister(@Valid @ModelAttribute("usuario") UserRegistrationDto userRegistrationDto,
                                      BindingResult result,
                                      @RequestParam(name = "g-recaptcha-response") String captchaResponse,
                                      Model model) {

        // VALIDATE THE reCAPTCHA IS ENABLED
        if (!recaptchaService.validate(captchaResponse)) {
            model.addAttribute("error", "Verifica el recatpha porfavor.");
            return "auth/registro";
        }

        // EMAIL NORMALIZATION TO CONVERT TO LOWER CASE
        if (userRegistrationDto.getEmail() != null) {
            userRegistrationDto.setEmail(userRegistrationDto.getEmail().toLowerCase().trim());
        }

        UserAccount accountExist = userService.findByEmail(userRegistrationDto.getEmail());

        if (accountExist != null) {
            sendAccountAlreadyExistsAlert(userRegistrationDto.getEmail());

            model.addAttribute("usuario", new UserRegistrationDto());
            model.addAttribute("error", "Te enviamos un correo con las instrucciones");
            return "auth/registro";
        }

        if (result.hasErrors()) {
            return "auth/registro";
        }

        userService.save(userRegistrationDto);
        return "redirect:/registro?exito";
    }

    public void sendAccountAlreadyExistsAlert(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Aviso informativo: Intento de registro en Micheladas App");

        String contenido = "Hola,\n\n" +
                "Te enviamos este correo porque alguien intentó crear una cuenta nueva usando tu dirección de email.\n\n" +
                "¿NO FUISTE TÚ?\n" +
                "No te preocupes, NO TIENES QUE HACER NADA. Nuestro sistema detectó que ya tienes una cuenta y bloqueó el intento automáticamente para proteger tu información.\n\n" +
                "Tu cuenta sigue segura y no se ha realizado ningún cambio en tu contraseña ni en tus datos personales.\n\n" +
                "¿FUISTE TÚ?\n" +
                "Si estabas intentando registrarte y olvidaste que ya tenías una cuenta, simplemente entra a la app e inicia sesión con tus datos de siempre.\n\n" +
                "¡Gracias por usar Micheladas App!\n\n" +
                "Atentamente,\n" +
                "El equipo de soporte.";

        message.setText(contenido);

        try {
            mailSender.send(message);
            logger.info("Security alert email sent successfully to: {}", email);
        } catch (org.springframework.mail.MailException e) {

            logger.error("Failed to send security alert to {}. Mail Service Error: {}", email, e.getMessage());
        } catch (Exception e) {

            logger.error("Unexpected error while attempting to send alert email to {}: ", email, e);
        }
    }

}