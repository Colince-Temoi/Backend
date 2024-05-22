package tech.csm.config;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tech.csm.aspects.MailService;

@Aspect
@Component
public class ServiceInterceptor {
    @Autowired
    private MailService mailService;

	@Pointcut("execution(* tech.csm.service.*.*(..))")
    public void serviceMethods() {
        // Pointcut expression to match all methods in the service package
    }

    @Pointcut("execution(* tech.csm.service.RegionServiceImpl.saveAddress(..))")
    public void saveAddressMethod() {
        // Pointcut expression to match the saveAddress method
    }

    @Before("serviceMethods()")
    public void beforeServiceMethod() {
        // Advice that runs before any matched method
        System.out.println("A method in the service package is about to be called.");
    }

    @After("saveAddressMethod()")
    public void afterSaveAddress() {
        // Advice that runs after the saveAddress method
        mailService.sendMail();
    }
}
