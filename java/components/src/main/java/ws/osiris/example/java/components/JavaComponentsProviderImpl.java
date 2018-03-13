package ws.osiris.example.java.components;

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
