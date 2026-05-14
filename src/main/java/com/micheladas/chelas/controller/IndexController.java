package com.micheladas.chelas.controller;

import com.micheladas.chelas.controllergenericview.ControllerGenericView;
import com.micheladas.chelas.entity.UserIp;
import com.micheladas.chelas.export.IpsPdfExporter;
import com.micheladas.chelas.repository.UserIpRepository;
import com.micheladas.chelas.service.UserIpService;
import com.micheladas.chelas.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
public class IndexController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserIpRepository userIpRepository;

	private final UserIpService userIpService;

	public IndexController(UserIpService userIpService) {
		this.userIpService = userIpService;
	}

	@GetMapping("/index")
	public String Index(Model model) {
		model.addAttribute("usuario", userService.UserLists());
		return "main/index";
	}

	/**
	 * TOGGLES THE ACCOUNT LOCK STATUS (ENABLED/DISABLED) FOR A SPECIFIC USER.
	 */

	@GetMapping("/usuarios/bloquear/{id}")
	public String toggleUser(@PathVariable Long id) {
		userService.toggleLockStatus(id);
		return "redirect:/index";
	}

	// AUDITORIA VIEW
	@GetMapping("/userips")
	public String listIps(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		return ControllerGenericView.processView(
				"userips",
				page,
				null,
				model,
				userIpService::findAll,
				null
		);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/IpsPdfExporter")
	public void exportIpsPdf(HttpServletResponse response) {

		ControllerGenericView.processPdfExport(response, "auditoria_ips",
				userIpService::findAllUserIps,
				(list, resp) -> {
					try {
						// 'LIST'
						new IpsPdfExporter(list).export(resp);
					} catch (Exception e) {
						log.error("Error al exportar PDF de IPs: {}", e.getMessage());
					}
				});
	}

	/* * NOTE: The manual logout mapping is commented out because Spring Security's
	 * LogoutFilter (defined in SecurityConfiguration) intercepts "/logout"
	 * before it ever reaches this controller.
	 *
	 * @GetMapping("/logout")
	 * public String logout(HttpServletRequest request) {
	 * request.getSession().invalidate();
	 * return "redirect:/login?logout";
	 * }
	 */

}
