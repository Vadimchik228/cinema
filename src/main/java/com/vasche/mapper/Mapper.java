package com.vasche.mapper;

public interface Mapper<F, T> {

    T mapFrom(F object);
}
