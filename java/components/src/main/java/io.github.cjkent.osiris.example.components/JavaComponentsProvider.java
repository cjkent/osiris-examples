/*
 * Copyright (C) 2017 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package io.github.cjkent.osiris.example.components;

import io.github.cjkent.osiris.core.ComponentsProvider;

/**
 * Implementation of {@link ComponentsProvider} written in Java; used by the code in {@code ApiDefinition.kt}.
 */
public interface JavaComponentsProvider extends ComponentsProvider {

    /**
     * Returns a factory that creates personalised greetings.
     *
     * @return a factory that creates personalised greetings
     */
    GreetingFactory getGreetingFactory();
}
