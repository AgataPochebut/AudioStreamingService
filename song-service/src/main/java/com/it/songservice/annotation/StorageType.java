package com.it.songservice.annotation;

import com.it.songservice.model.StorageTypes;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StorageType {

    StorageTypes value();

}
