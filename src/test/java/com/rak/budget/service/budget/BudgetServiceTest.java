package com.rak.budget.service.budget;

import com.rak.budget.controller.budget.dto.BudgetCategoryDto;
import com.rak.budget.controller.budget.dto.BudgetDto;
import com.rak.budget.controller.budget.dto.BudgetViewDto;
import com.rak.budget.dao.model.budget.Budget;
import com.rak.budget.dao.model.budget.BudgetCategory;
import com.rak.budget.dao.repository.budget.BudgetRepository;
import com.rak.budget.service.budget.mapper.BudgetDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BudgetServiceTest {

    @Mock
    private BudgetRepository repository;

    @Mock
    private BudgetCategoryService budgetCategoryService;

    @Mock
    private BudgetDtoMapper mapper;

    @InjectMocks
    private BudgetService service;

    private Budget budget;
    private BudgetDto budgetDto;
    private BudgetViewDto budgetViewDto;
    private BudgetCategory budgetCategory;
    private BudgetCategoryDto budgetCategoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        budgetCategory = new BudgetCategory();
        budgetCategory.setId("1");
        budgetCategory.setName("Utilities");

        budget = new Budget();
        budget.setId("1");
        budget.setUserId("user1");
        budget.setAmount(BigDecimal.valueOf(2000));
        budget.setCategory(budgetCategory);

        budgetDto = BudgetDto.builder()
                .id("1")
                .userId("user1")
                .amount(BigDecimal.valueOf(2000))
                .categoryId("1")
                .build();

        budgetCategoryDto = BudgetCategoryDto.builder()
                .id("1")
                .name("Utilities")
                .build();

        budgetViewDto = BudgetViewDto.builder()
                .id("1")
                .userId("user1")
                .amount(BigDecimal.valueOf(2000))
                .category(budgetCategoryDto)
                .build();
    }

    @Test
    void getAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Budget> page = new PageImpl<>(Collections.singletonList(budget), pageable, 1);

        when(repository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(mapper.mapView(budget)).thenReturn(budgetViewDto);

        Page<BudgetViewDto> result = service.getAll("user1", "1", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Utilities", result.getContent().get(0).getCategory().getName());

        verify(repository, times(1)).findAll(any(Specification.class), eq(pageable));
        verify(mapper, times(1)).mapView(budget);
    }


    @Test
    void getById() {
        when(repository.findById("1")).thenReturn(Optional.of(budget));
        when(mapper.mapView(budget)).thenReturn(budgetViewDto);

        Optional<BudgetViewDto> result = service.getById("1");

        assertTrue(result.isPresent());
        assertEquals("Utilities", result.get().getCategory().getName());

        verify(repository, times(1)).findById("1");
        verify(mapper, times(1)).mapView(budget);
    }

    @Test
    void getBudgetByCategoryIdAndUserId() {
        when(repository.findByCategoryIdAndUserId("1", "user1")).thenReturn(Optional.of(budget));
        when(mapper.mapView(budget)).thenReturn(budgetViewDto);

        Optional<BudgetViewDto> result = service.getBudgetByCategoryIdAndUserId("1", "user1");

        assertTrue(result.isPresent());
        assertEquals("Utilities", result.get().getCategory().getName());

        verify(repository, times(1)).findByCategoryIdAndUserId("1", "user1");
        verify(mapper, times(1)).mapView(budget);
    }

    @Test
    void create() {
        when(repository.existsByUserIdAndCategoryId("user1", "1")).thenReturn(false);
        when(budgetCategoryService.findById("1")).thenReturn(budgetCategory);
        when(repository.save(any(Budget.class))).thenReturn(budget);
        when(mapper.map(budget)).thenReturn(budgetDto);

        BudgetDto result = service.create(budgetDto);

        assertNotNull(result);
        assertEquals("1", result.getCategoryId());

        verify(repository, times(1)).existsByUserIdAndCategoryId("user1", "1");
        verify(budgetCategoryService, times(1)).findById("1");
        verify(repository, times(1)).save(any(Budget.class));
        verify(mapper, times(1)).map(budget);
    }

    @Test
    void create_ExistingBudget() {
        when(repository.existsByUserIdAndCategoryId("user1", "1")).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> service.create(budgetDto));

        assertEquals("User already has a budget for category id 1", exception.getMessage());

        verify(repository, times(1)).existsByUserIdAndCategoryId("user1", "1");
        verify(budgetCategoryService, never()).findById(anyString());
        verify(repository, never()).save(any(Budget.class));
    }

    @Test
    void update() {
        when(repository.findById("1")).thenReturn(Optional.of(budget));
        when(budgetCategoryService.findById("1")).thenReturn(budgetCategory);
        when(repository.save(any(Budget.class))).thenReturn(budget);
        when(mapper.map(budget)).thenReturn(budgetDto);

        Optional<BudgetDto> result = service.update("1", budgetDto);

        assertTrue(result.isPresent());
        assertEquals("1", result.get().getCategoryId());

        verify(repository, times(1)).findById("1");
        verify(budgetCategoryService, times(1)).findById("1");
        verify(repository, times(1)).save(any(Budget.class));
        verify(mapper, times(1)).map(budget);
    }

    @Test
    void delete() {
        when(repository.findById("1")).thenReturn(Optional.of(budget));

        boolean result = service.delete("1");

        assertTrue(result);

        verify(repository, times(1)).findById("1");
        verify(repository, times(1)).deleteById("1");
    }

    @Test
    void delete_NotFound() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        boolean result = service.delete("1");

        assertFalse(result);

        verify(repository, times(1)).findById("1");
        verify(repository, never()).deleteById("1");
    }
}
