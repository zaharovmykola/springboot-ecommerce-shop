package org.mykola.zakharov.spring.boot.first.ecommerceshop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.exception.ConstraintViolationException;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.springframework.context.annotation.Configuration;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.util.ErrorsGetter;

@Configuration
@Aspect
public class ExceptionsProcessor {
    @Around("execution(* org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.*.*(..))")
    public Object onDaoException(ProceedingJoinPoint pjp) throws Exception {
        Object output = null;
        try {
            output = pjp.proceed();
        } catch (Exception ex) {
            System.out.println("sql error is " + ErrorsGetter.getException(ex));
            if (ErrorsGetter.getException(ex).contains("ConstraintViolationException")){
                throw new ConstraintViolationException("", null, "");
            }
            throw ex;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return output;
    }

    @Around("execution(* org.mykola.zakharov.spring.boot.first.ecommerceshop.service.*.*(..))")
    public Object onServiceException(ProceedingJoinPoint pjp) {
        Object output = null;
        try {
            output = pjp.proceed();
        } catch (ConstraintViolationException ex) {
            System.out.println("my service error");
            output =
                    ResponseModel.builder()
                            .status(ResponseModel.FAIL_STATUS)
                            .message("This name is already taken")
                            .build();
        } catch (Exception ex) {
            output =
                    ResponseModel.builder()
                            .status(ResponseModel.FAIL_STATUS)
                            .message("Unknown database error")
                            .build();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return output;
    }
}
