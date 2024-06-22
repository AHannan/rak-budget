package com.rak.budget.service.budget;

import com.rak.budget.controller.budget.dto.BudgetCategoryDto;
import com.rak.budget.dao.model.budget.BudgetCategory;
import com.rak.budget.dao.repository.budget.BudgetCategoryRepository;
import com.rak.budget.service.budget.mapper.BudgetCategoryDtoMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetCategoryService {

    private final BudgetCategoryRepository repository;
    private final BudgetCategoryDtoMapper mapper;

    public List<BudgetCategoryDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    public Optional<BudgetCategoryDto> getById(String id) {
        return repository.findById(id)
                .map(mapper::map);
    }

    public BudgetCategory findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Budget Category with id: " + id + " not exists"));
    }

    public BudgetCategoryDto create(BudgetCategoryDto dto) {
        var result = mapper.map(dto);
        return mapper.map(repository.save(result));
    }

    public Optional<BudgetCategoryDto> update(String id, BudgetCategoryDto dto) {
        return repository.findById(id).map(existing -> {
            existing.setName(dto.getName());
            return mapper.map(repository.save(existing));
        });
    }

    public boolean delete(String id) {
        var result = repository.findById(id);
        if (result.isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

}
