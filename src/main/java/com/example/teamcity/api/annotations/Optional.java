package com.example.teamcity.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/**
  Fields with this annotations will NOT be generated randomly or with parametrized values,
  it is needed to set the value manually
 */
public @interface Optional {

}
