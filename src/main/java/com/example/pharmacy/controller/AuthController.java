package com.example.pharmacy.controller;

import com.example.pharmacy.models.User;
import com.example.pharmacy.models.Role;
import com.example.pharmacy.mapper.UserMapper;
import com.example.pharmacy.mapper.RoleMapper;
import com.example.pharmacy.designpatterns.factory.UserFactory;
import com.example.pharmacy.designpatterns.factory.UserFactoryProvider;
import com.example.pharmacy.service.DashboardService;
import com.example.pharmacy.service.MedicineService;
import com.example.pharmacy.service.StockNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class AuthController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private StockNotificationService stockNotificationService;
    @Autowired
    private MedicineService medicineService;
    @Autowired
    private UserFactoryProvider factoryProvider;

    public AuthController(MedicineService medicineService, StockNotificationService stockNotificationService) {
        this.medicineService = medicineService;
        this.stockNotificationService = stockNotificationService;
        this.medicineService.addObserver(this.stockNotificationService);
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        User foundUser = userMapper.findByUsername(username);
        if (foundUser != null && foundUser.getPassword().equals(password)) {
            session.setAttribute("user", foundUser);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("errorMessage", "Username atau password salah!");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleMapper.findAll());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (userMapper.findByUsername(user.getUsername()) != null) {
            // Ask the provider for the correct factory based on the role
            model.addAttribute("errorMessage", "Username already exists!");
            model.addAttribute("user", user);
            model.addAttribute("roles", roleMapper.findAll());
            return "register";
        }
        // Find the selected role from the database
        Role selectedRole = roleMapper.findById(user.getRole().getId());
        if (selectedRole == null) {
            model.addAttribute("errorMessage", "Role tidak valid");
            model.addAttribute("user", user);
            model.addAttribute("roles", roleMapper.findAll());
            return "register";
        }

        // Use factory provider to get the appropriate factory
        User newUser = factoryProvider.getFactory(selectedRole.getName())
                .createUser(user.getUsername(), user.getPassword(), selectedRole);
        // Save the new user to the database
        userMapper.insert(newUser);
        return "redirect:/login?registered";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login?unauthorized";
        }
        model.addAttribute("username", currentUser.getUsername());
        model.addAttribute("role", currentUser.getRole().getName());

        Map<String, Object> summaryData = dashboardService.getDashboardSummary();
        model.addAllAttributes(summaryData);

        model.addAttribute("stockNotifications", stockNotificationService.getUnreadNotifications());

        return "dashboard";
    }

    @GetMapping("/list")
    public String showListPage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || currentUser.getRole() == null
                || !"ADMIN".equalsIgnoreCase(currentUser.getRole().getName())) {
            return "redirect:/login";
        }
        List<User> users = userMapper.findAll();
        model.addAttribute("users", users);
        return "list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}