package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;
import javax.servlet.http.HttpServletRequest;
@Controller
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public String showUsers(Model model) {
		model.addAttribute("users", userService.getAllUsers());
		return "users";
	}
	@GetMapping("/test-encoding")
	@ResponseBody
	public String testEncoding(HttpServletRequest request) {
		String testText = "Тест кириллицы: Привет!";
		return "DB: " + userService.getAllUsers() + "<br>" +
				"HTTP Request: " + request.getCharacterEncoding() + "<br>" +
				"System: " + System.getProperty("file.encoding") + "<br>" +
				"Static: " + testText;
	}

	@PostMapping("/save")
	public String saveUser(@RequestParam String name,
						   @RequestParam String email) {
		User user = new User(name, email);
		userService.saveUser(user);
		return "redirect:/users";
	}

	@PostMapping("/update")
	public String updateUser(@RequestParam Long id,
							 @RequestParam String name,
							 @RequestParam String email) {
		User user = userService.getUserById(id);
		user.setName(name);
		user.setEmail(email);
		userService.saveUser(user);
		return "redirect:/users";
	}

	@PostMapping("/delete")
	public String deleteUser(@RequestParam Long id) {
		userService.deleteUser(id);
		return "redirect:/users";
	}
}