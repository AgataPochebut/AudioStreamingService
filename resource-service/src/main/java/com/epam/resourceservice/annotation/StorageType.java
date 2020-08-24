package com.epam.resourceservice.annotation;

import com.epam.resourceservice.model.StorageTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //Указывает, что целью нашей Аннотации является класс.
public @interface StorageType {

    StorageTypes value();

}