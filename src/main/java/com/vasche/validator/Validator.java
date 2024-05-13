package com.vasche.validator;

public interface Validator<T> {
    ValidationResult isValid(T object);
}
