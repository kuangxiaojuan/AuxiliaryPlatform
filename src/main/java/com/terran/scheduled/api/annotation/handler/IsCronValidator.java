package com.terran.scheduled.api.annotation.handler;

import com.terran.scheduled.api.annotation.IsCron;
import org.apache.xmlbeans.impl.validator.ValidatorUtil;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsCronValidator implements ConstraintValidator<IsCron, String> {
    @Override
    public void initialize(IsCron constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return CronSequenceGenerator.isValidExpression(value);
    }
}
