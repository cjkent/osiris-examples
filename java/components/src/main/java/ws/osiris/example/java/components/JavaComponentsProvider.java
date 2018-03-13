package ws.osiris.example.java.components;

import ws.osiris.core.ComponentsProvider;

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
