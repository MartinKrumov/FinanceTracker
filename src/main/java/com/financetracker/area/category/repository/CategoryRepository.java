package com.financetracker.area.category.repository;

import com.financetracker.area.category.domain.Category;
import com.financetracker.area.transaction.enums.TransactionType;
import com.financetracker.area.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByNameAndType(String name, TransactionType type);

    List<Category> findAllByUsers(User user);
}
