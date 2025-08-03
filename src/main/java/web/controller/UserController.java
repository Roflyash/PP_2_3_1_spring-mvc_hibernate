package web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;
import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public String showUsers(Model model) {
		model.addAttribute("users", userService.getAllUsers());
		model.addAttribute("newUser", new User());
		model.addAttribute("editUser", new User());
		return "users";
	}

	@PostMapping("/save")
	public String saveUser(@Valid @ModelAttribute("newUser") User user,
						   BindingResult bindingResult,
						   Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("users", userService.getAllUsers());
			return "users";
		}
		userService.registerUser(user);
		return "redirect:/users";
	}

	@GetMapping("/edit")
	public String showEditForm(@RequestParam Long id, Model model) {
		model.addAttribute("users", userService.getAllUsers());
		model.addAttribute("editUser", userService.getUserById(id));
		return "users";
	}

	@PostMapping("/update")
	public String updateUser(@Valid @ModelAttribute("editUser") User user,
							 BindingResult bindingResult,
							 Model model) {
		if (bindingResult.hasErrors()) {
			model.addAttribute("users", userService.getAllUsers());
			return "users";
		}

		try {
			userService.updateUser(user);
			return "redirect:/users";
		} catch (IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("users", userService.getAllUsers());
			return "users";
		}
	}

	@PostMapping("/delete")
	public String deleteUser(@RequestParam Long id) {
		userService.deleteUser(id);
		return "redirect:/users";
	}
}