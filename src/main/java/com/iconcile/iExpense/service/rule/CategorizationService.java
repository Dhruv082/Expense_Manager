package com.iconcile.iExpense.service.rule;

import com.iconcile.iExpense.model.Category;
import com.iconcile.iExpense.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CategorizationService {

    private final CategoryRepository categoryRepository;

    private static final Map<String, String> RULES = Map.of(
            "swiggy", "Food",
            "zomato", "Food",
            "uber", "Transport",
            "ola", "Transport",
            "amazon", "Shopping",
            "flipkart", "Shopping"
    );

    public CategorizationService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category resolveCategoryForVendor(String vendorName) {

        String key = vendorName.toLowerCase().trim();

        String categoryName = RULES.getOrDefault(key, "Others");

        return categoryRepository
                .findByNameIgnoreCase(categoryName)
                .orElseGet(() -> categoryRepository.save(new Category(categoryName)));
    }
}