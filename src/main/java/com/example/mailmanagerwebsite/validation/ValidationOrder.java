package com.example.mailmanagerwebsite.validation;

import jakarta.validation.GroupSequence;

@GroupSequence({NotBlankValidation.class, SizeValidation.class, PatternValidation.class})
public interface ValidationOrder { }