package tech.csm.aspects;

import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Aspect
@Component
public class ServiceInterceptor {

    private final Validator validator;

    public ServiceInterceptor() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Before("execution(* tech.csm.service.*.*(..)) && args(arg,..)")
    public void validateMethod(JoinPoint joinPoint, Object arg) {
    	System.out.println("Executing before method!");
        Set<ConstraintViolation<Object>> violations = validator.validate(arg);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    @AfterThrowing(pointcut = "execution(* tech.csm.service.*.*(..))", throwing = "ex")
    public void handleServiceException(JoinPoint joinPoint, Exception ex) {
    	System.out.println("Executing After throwing an execption!");
        System.out.println("Exception in " + joinPoint.getSignature().getName() + " : " + ex.getMessage());
        // Additional exception handling logic can be added here
    }
}
