package com.rak.budget.controller.budget;

import com.rak.budget.controller.budget.dto.BudgetDto;
import com.rak.budget.controller.budget.dto.BudgetViewDto;
import com.rak.budget.dao.model.budget.Budget;
import com.rak.budget.service.budget.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService service;

    @GetMapping
    public ResponseEntity<Page<BudgetViewDto>> getAll(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String categoryId,
            Pageable pageable) {
        Page<BudgetViewDto> budgets = service.getAll(userId, categoryId, pageable);
        return new ResponseEntity<>(budgets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetViewDto> getById(@PathVariable String id) {
        return service.getById(id)
                .map(entity -> new ResponseEntity<>(entity, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/category-id/{categoryId}/userId/{userId}")
    public ResponseEntity<Budget> getBudgetByCategoryIdAndUserId(@PathVariable String categoryId, @PathVariable String userId) {
        Optional<Budget> budget = service.getBudgetByCategoryIdAndUserId(categoryId, userId);
        return budget.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BudgetDto> create(@RequestBody BudgetDto dto) {
        try {
            return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetDto> update(@PathVariable String id, @RequestBody BudgetDto dto) {
        try {
            return service.update(id, dto)
                    .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (service.delete(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
