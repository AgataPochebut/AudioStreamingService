package com.it.songservice.annotation;

import com.it.songservice.model.StorageTypes;

import java.lang.annotation.*;

@Target(ElementType.TYPE) //Указывает, что целью нашей Аннотации является класс.
@Retention(RetentionPolicy.RUNTIME)
//@Inherited
public @interface StorageType {

    StorageTypes value();

}
