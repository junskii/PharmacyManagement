package com.example.pharmacy.controller;

import com.example.pharmacy.models.Sale;
import com.example.pharmacy.models.SaleItem;
import com.example.pharmacy.models.User;
import com.example.pharmacy.service.SaleService;
import com.example.pharmacy.service.MedicineService;
import com.example.pharmacy.service.StockNotificationService;
import com.example.pharmacy.designpatterns.command.ProcessSaleCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/sales-transaction")
public class SalesController {

    @Autowired
    private SaleService saleService;
    @Autowired
    private MedicineService medicineService;
    @Autowired
    private StockNotificationService stockNotificationService;

    private boolean canProcessSales(User user) {
        return user != null && user.getRole() != null &&
                ("ADMIN".equalsIgnoreCase(user.getRole().getName())
                        || "CASHIER".equalsIgnoreCase(user.getRole().getName()));
    }

    @GetMapping
    public String showSalesTransactionPage(HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login?unauthorized";
        }

        if (!canProcessSales(currentUser)) {
            model.addAttribute("errorMessage", "Anda tidak memiliki akses untuk membuat transaksi baru.");
            return "redirect:/dashboard";
        }

        model.addAttribute("sale", new Sale());
        model.addAttribute("allMedicines", medicineService.findAll());
        model.addAttribute("medicineCategories", List.of("Medicine", "Vitamin", "Body care"));

        return "sales-transaction"; // Correctly return the new sales-transaction.html
    }

    @PostMapping("/process")
    public String processSale(@ModelAttribute Sale sale, HttpSession session, Model model) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login?unauthorized";
        }

        if (!canProcessSales(currentUser)) {
            model.addAttribute("errorMessage", "Anda tidak memiliki akses untuk memproses transaksi.");
            return "redirect:/dashboard";
        }

        sale.setUserId(currentUser.getId());
        sale.setSaleDate(LocalDateTime.now());
        System.out.println("Received Sale Object in SalesController: ");
        System.out.println("  Payment Method: " + sale.getPaymentMethod());
        System.out.println("  Frontend Promo Percentage: " + sale.getFrontendPromoPercentage());
        System.out.println("  Discount Details: " + sale.getDiscountDetails());
        try {
            ProcessSaleCommand command = new ProcessSaleCommand(saleService, sale);
            command.execute();
            return "redirect:/sales-transaction?success";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("sale", sale);
            model.addAttribute("allMedicines", medicineService.findAll());
            model.addAttribute("medicineCategories", List.of("Medicine", "Vitamin", "Body care"));
            return "sales-transaction"; // Return the cashier app on error too
        }
    }
}