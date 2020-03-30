package com.epam.annotation;

import com.epam.model.StorageTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //Указывает, что целью нашей Аннотации является класс.
public @interface StorageType {

    StorageTypes value();

}
