package org.mykola.zakharov.spring.boot.first.ecommerceshop.junit.service;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

/* Разрешитель для вндрения ссылки на объект типа SystemOutResource
внутрь класса тестов или внутрь отельных тест-кейсов*/
public class SystemOutResourceParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == SystemOutResource.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return new SystemOutResource();
    }
}