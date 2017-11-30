/*
 * Copyright (C) 2017 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package io.github.cjkent.osiris.example.components;

/**
 * Trivial example component written in Java; returns a greeting given a person's name.
 */
public class GreetingFactory {

    /**
     * Returns a greeting for a named person.
     *
     * @param name the name
     * @return a personalised greeting for the name
     */
    public String createGreeting(String name) {
        return "hello, " + name + "!";
    }
}
