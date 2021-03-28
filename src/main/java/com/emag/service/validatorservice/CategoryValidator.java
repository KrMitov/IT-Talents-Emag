package com.emag.service.validatorservice;

import com.emag.exceptions.BadRequestException;

public class CategoryValidator {

    public static void validateCategoryName(String name){
        if (name == null || name.equals("")){
            throw new BadRequestException("Category name cannot be null or empty string");
        }
    }
}
