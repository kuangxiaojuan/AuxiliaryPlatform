package com.terran.scheduled.api.annotation;

import com.terran.scheduled.api.annotation.handler.IsCronValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {IsCronValidator.class})
public @interface IsCron {
    //提示信息
    String message() default "cron表达式输入有误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
