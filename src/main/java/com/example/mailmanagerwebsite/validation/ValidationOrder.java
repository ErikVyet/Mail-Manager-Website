package com.example.mailmanagerwebsite.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({
    NotNullValidation.class,
    NotBlankValidation.class,
    SizeValidation.class,
    PatternValidation.class
})
public interface ValidationOrder { }