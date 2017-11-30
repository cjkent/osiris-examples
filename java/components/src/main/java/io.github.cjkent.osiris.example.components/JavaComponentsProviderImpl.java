/*
 * Copyright (C) 2017 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package io.github.cjkent.osiris.example.components;

/**
 * Simple implementation of {@link JavaComponentsProvider} written in Java; used by the Kotlin code in
 * {@code ApiDefinition.kt} to provide components.
 */
public class JavaComponentsProviderImpl implements JavaComponentsProvider {

    private final GreetingFactory greetingFactory;

    public JavaComponentsProviderImpl(GreetingFactory greetingFactory) {
        this.greetingFactory = greetingFactory;
    }

    @Override
    public GreetingFactory getGreetingFactory() {
        return greetingFactory;
    }
}
