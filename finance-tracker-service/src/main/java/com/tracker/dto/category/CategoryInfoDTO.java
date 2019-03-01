package com.tracker.dto.category;

import com.tracker.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfoDTO implements Serializable {

    private Long id;
    private String name;
    private TransactionType type;
}