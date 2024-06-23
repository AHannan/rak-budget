package com.rak.budget.service.budget;

import com.rak.budget.controller.budget.dto.BudgetCategoryDto;
import com.rak.budget.dao.model.budget.BudgetCategory;
import com.rak.budget.dao.repository.budget.BudgetCategoryRepository;
import com.rak.budget.service.budget.mapper.BudgetCategoryDtoMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BudgetCategoryServiceTest {

    @Mock
    private BudgetCategoryRepository repository;

    @Mock
    private BudgetCategoryDtoMapper mapper;

    @InjectMocks
    private BudgetCategoryService service;

    private BudgetCategory budgetCategory;
    private BudgetCategoryDto budgetCategoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        budgetCategory = new BudgetCategory();
        budgetCategory.setId("1");
        budgetCategory.setName("Utilities");

        budgetCategoryDto = BudgetCategoryDto.builder().id("1").name("Utilities").build();
    }

    @Test
    void getAll() {
        when(repository.findAll()).thenReturn(Collections.singletonList(budgetCategory));
        when(mapper.map(budgetCategory)).thenReturn(budgetCategoryDto);

        List<BudgetCategoryDto> result = service.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Utilities", result.get(0).getName());

        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).map(budgetCategory);
    }

    @Test
    void getById() {
        when(repository.findById("1")).thenReturn(Optional.of(budgetCategory));
        when(mapper.map(budgetCategory)).thenReturn(budgetCategoryDto);

        Optional<BudgetCategoryDto> result = service.getById("1");

        assertTrue(result.isPresent());
        assertEquals("Utilities", result.get().getName());

        verify(repository, times(1)).findById("1");
        verify(mapper, times(1)).map(budgetCategory);
    }

    @Test
    void findById() {
        when(repository.findById("1")).thenReturn(Optional.of(budgetCategory));

        BudgetCategory result = service.findById("1");

        assertNotNull(result);
        assertEquals("Utilities", result.getName());

        verify(repository, times(1)).findById("1");
    }

    @Test
    void findById_NotFound() {
        when(repository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById("1"));

        verify(repository, times(1)).findById("1");
    }

    @Test
    void create() {
        when(mapper.map(budgetCategoryDto)).thenReturn(budgetCategory);
        when(repository.save(budgetCategory)).thenReturn(budgetCategory);
        when(mapper.map(budgetCategory)).thenReturn(budgetCategoryDto);

        BudgetCategoryDto result = service.create(budgetCategoryDto);

        assertNotNull(result);
        assertEquals("Utilities", result.getName());

        verify(mapper, times(1)).map(budgetCategoryDto);
        verify(repository, times(1)).save(budgetCategory);
        verify(mapper, times(1)).map(budgetCategory);
    }

    @Test
    void update() {
        when(repository.findById("1")).thenReturn(Optional.of(budgetCategory));
        when(repository.save(budgetCategory)).thenReturn(budgetCategory);
        when(mapper.map(budgetCategory)).thenReturn(budgetCategoryDto);

        Optional<BudgetCategoryDto> result = service.update("1", budgetCategoryDto);

        assertTrue(result.isPresent());
        assertEquals("Utilities", result.get().getName());

        verify(repository, times(1)).findById("1");
        verify(repository, times(1)).save(budgetCategory);
        verify(mapper, times(1)).map(budgetCategory);
    }

    @Test
    void delete() {
        when(repository.findById("1")).thenReturn(Optional.of(budgetCategory));

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
