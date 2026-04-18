package com.budgetmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.budgetmanager.controller.dto.BudgetResponse;
import com.budgetmanager.controller.dto.BudgetSetRequest;
import com.budgetmanager.model.Budget;
import com.budgetmanager.service.BudgetService;

import java.util.Optional;

/**
 * BudgetWebController
 * Public budget endpoints for frontend interaction.
 */
@RestController
@RequestMapping("/budget")
public class BudgetWebController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/set")
    public ResponseEntity<BudgetResponse> setBudget(@RequestBody BudgetSetRequest request) {
        if (request == null || request.getUserId() == null || request.getAmount() == null) {
            return ResponseEntity.badRequest().build();
        }

        String period = request.getPeriod();
        if (period == null || period.isBlank()) {
            period = "monthly";
        }

        Budget budget = budgetService.setBudget(request.getUserId(), request.getAmount(), period);
        return ResponseEntity.ok(new BudgetResponse(budget));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BudgetResponse> getBudget(@PathVariable Long userId) {
        Optional<Budget> budget = budgetService.getBudget(userId, "monthly");
        return budget.map(b -> ResponseEntity.ok(new BudgetResponse(b)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
