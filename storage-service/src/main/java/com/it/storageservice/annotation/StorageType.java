package com.it.storageservice.annotation;

import com.it.storageservice.model.StorageTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) //Указывает, что целью нашей Аннотации является класс.
@Retention(RetentionPolicy.RUNTIME)
//@Inherited
public @interface StorageType {

    StorageTypes value();

}
