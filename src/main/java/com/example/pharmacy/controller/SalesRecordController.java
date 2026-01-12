package com.example.pharmacy.controller;

import com.example.pharmacy.models.Sale;
import com.example.pharmacy.service.SaleService;
import com.example.pharmacy.service.UserService;
import com.example.pharmacy.models.User;
import com.example.pharmacy.designpatterns.strategy.SaleFilterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sales-record")
public class SalesRecordController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private UserService userService;

    private final List<SaleFilterStrategy> filterStrategies;

    @Autowired
    public SalesRecordController(List<SaleFilterStrategy> filterStrategies) {
        this.filterStrategies = filterStrategies;
    }

    private static final int PAGE_SIZE = 10;

    private boolean canAccessSalesRecord(User user) {
        return user != null && user.getRole() != null &&
                (Objects.equals(user.getRole().getName(), "ADMIN")
                        || Objects.equals(user.getRole().getName(), "CASHIER"));
    }

    @GetMapping
    public String salesRecordPage(
            Model model,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "startDate", required = false) String startDateStr,
            @RequestParam(value = "endDate", required = false) String endDateStr,
            @RequestParam(value = "cashierName", required = false) String cashierName,
            @RequestParam(value = "minAmount", required = false) Double minAmount,
            @RequestParam(value = "maxAmount", required = false) Double maxAmount,
            @RequestParam(value = "hasDiscount", required = false) Boolean hasDiscount,
            @RequestParam(value = "searchKeyword", required = false) String searchKeyword,
            @RequestParam(value = "orderBy", defaultValue = "sale_date") String orderBy,
            @RequestParam(value = "orderDirection", defaultValue = "desc") String orderDirection,
            HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (!canAccessSalesRecord(currentUser)) {
            return "redirect:/dashboard?error=unauthorized";
        }

        Map<String, Object> filterParams = new HashMap<>();

        // Parse date strings and add to filterParams as LocalDate
        // Do NOT put them in filterParams if parsing fails, but proceed with other
        // filters
        try {
            if (startDateStr != null && !startDateStr.isEmpty()) {
                filterParams.put("startDate", LocalDate.parse(startDateStr));
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                filterParams.put("endDate", LocalDate.parse(endDateStr));
            }
        } catch (DateTimeParseException e) {
            model.addAttribute("dateError", "Invalid date format. Please use YYYY-MM-DD.");
            // Do not return here; continue to apply other filters
        }

        // Apply other filter parameters to the map
        if (cashierName != null && !cashierName.isEmpty()) {
            filterParams.put("cashierName", cashierName);
        }
        if (minAmount != null) {
            filterParams.put("minAmount", minAmount);
        }
        if (maxAmount != null) {
            filterParams.put("maxAmount", maxAmount);
        }
        if (hasDiscount != null) {
            filterParams.put("hasDiscount", hasDiscount);
        }
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            filterParams.put("searchKeyword", searchKeyword);
        }

        // List<Sale> allSales = saleService.getAllSales(); // Get all sales from DB
        // List<Sale> filteredSales = new ArrayList<>(allSales);

        // Apply each filter strategy
        // for (SaleFilterStrategy strategy : filterStrategies) {
        // filteredSales = strategy.filter(filteredSales, filterParams);
        // }

        List<Sale> filteredSales = saleService.getFilteredSales(filterParams);

        // Apply sorting
        Comparator<Sale> comparator = switch (orderBy) {
            case "total_amount" -> Comparator.comparing(Sale::getTotalAmount);
            case "cashier_name" ->
                Comparator.comparing(Sale::getCashierName, Comparator.nullsLast(String::compareToIgnoreCase));
            default -> Comparator.comparing(Sale::getSaleDate, Comparator.nullsLast(LocalDateTime::compareTo));
        };

        if ("desc".equals(orderDirection)) {
            comparator = comparator.reversed();
        }
        filteredSales.sort(comparator);

        int totalSales = filteredSales.size();
        int totalPages = (int) Math.ceil((double) totalSales / PAGE_SIZE);

        // Apply pagination
        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, totalSales);
        List<Sale> salesForPage = new ArrayList<>();
        if (startIndex < endIndex) {
            salesForPage = filteredSales.subList(startIndex, endIndex);
        }

        model.addAttribute("sales", salesForPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("pageSize", PAGE_SIZE);

        // Add filter parameters back to model for form pre-population
        model.addAttribute("startDate", startDateStr);
        model.addAttribute("endDate", endDateStr);
        model.addAttribute("cashierName", cashierName);
        model.addAttribute("minAmount", minAmount);
        model.addAttribute("maxAmount", maxAmount);
        model.addAttribute("hasDiscount", hasDiscount);
        model.addAttribute("searchKeyword", searchKeyword);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("orderDirection", orderDirection);

        List<User> cashiers = userService.findAllCashiers();
        model.addAttribute("cashiers", cashiers);

        return "sales-record";
    }

    @GetMapping("/{id}")
    public String saleDetail(@PathVariable("id") Integer id, Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (!canAccessSalesRecord(currentUser)) {
            return "redirect:/dashboard?error=unauthorized";
        }

        Sale sale = saleService.getSaleById(id);
        if (sale == null) {
            return "redirect:/sales-record?error=SaleNotFound";
        }

        BigDecimal totalBeforeDiscount = sale.getSaleItems().stream()
                .map(item -> BigDecimal.valueOf(item.getSubtotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("sale", sale);
        model.addAttribute("totalBeforeDiscount", totalBeforeDiscount);
        return "sale-detail";
    }
}