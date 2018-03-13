package ws.osiris.example.java.components;

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
