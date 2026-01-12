package com.example.pharmacy.controller;

import com.example.pharmacy.models.Medicine;
import com.example.pharmacy.models.User;
import com.example.pharmacy.service.MedicineService;
import com.example.pharmacy.service.StockNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/medicines")
public class MedicineController {
    @Autowired
    private MedicineService medicineService;
    @Autowired
    private StockNotificationService stockNotificationService;

    private boolean canManage(User user) {
        return user != null && user.getRole() != null &&
                (Objects.equals(user.getRole().getName(), "ADMIN")
                        || Objects.equals(user.getRole().getName(), "PHARMACIST"));
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        model.addAttribute("medicines", medicineService.findAll());
        User user = (User) session.getAttribute("user");
        model.addAttribute("canManage", canManage(user));
        return "medicines/list";
    }

    @GetMapping("/add")
    public String addForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManage(user))
            return "redirect:/medicines";
        model.addAttribute("medicine", new Medicine());
        return "medicines/form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute Medicine medicine,
            @RequestParam("imageFile") MultipartFile imageFile,
            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManage(user))
            return "redirect:/medicines";
        if (!imageFile.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + "/uploads/obat/";
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                File dest = new File(uploadDir + fileName);
                dest.getParentFile().mkdirs();
                imageFile.transferTo(dest);
                medicine.setImage("/uploads/obat/" + fileName);
            } catch (IOException e) {
                model.addAttribute("errorMessage", "Gagal upload gambar: " + e.getMessage());
                model.addAttribute("medicine", medicine);
                return "medicines/form";
            }
        }
        if (!medicineService.add(medicine)) {
            model.addAttribute("errorMessage", "Stok tidak boleh negatif!");
            model.addAttribute("medicine", medicine);
            return "medicines/form";
        }
        return "redirect:/medicines";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManage(user))
            return "redirect:/medicines";
        Medicine medicine = medicineService.getById(id);
        if (medicine == null)
            return "redirect:/medicines";
        model.addAttribute("medicine", medicine);
        return "medicines/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, @ModelAttribute Medicine medicine,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManage(user))
            return "redirect:/medicines";
        medicine.setId(id);
        Medicine oldMedicine = medicineService.getById(id);
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + "/uploads/obat/";
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                File dest = new File(uploadDir + fileName);
                dest.getParentFile().mkdirs();
                imageFile.transferTo(dest);
                medicine.setImage("/uploads/obat/" + fileName);
            } catch (IOException e) {
                model.addAttribute("errorMessage", "Gagal upload gambar: " + e.getMessage());
                model.addAttribute("medicine", medicine);
                return "medicines/form";
            }
        } else if (oldMedicine != null) {
            medicine.setImage(oldMedicine.getImage()); // pertahankan gambar lama
        }
        if (!medicineService.update(medicine)) {
            model.addAttribute("errorMessage", "Stok tidak boleh negatif!");
            model.addAttribute("medicine", medicine);
            return "medicines/form";
        }
        return "redirect:/medicines";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (!canManage(user))
            return "redirect:/medicines";
        medicineService.delete(id);
        return "redirect:/medicines";
    }
}