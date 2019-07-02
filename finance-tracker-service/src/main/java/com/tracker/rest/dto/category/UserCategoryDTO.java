package com.tracker.rest.dto.category;

import com.tracker.domain.enums.TransactionType;
import lombok.Data;

@Data
public class UserCategoryDTO {

    private String name;
    private TransactionType type;
}
