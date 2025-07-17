package alt.elfocrash.roboto;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author Elfocrash
 * @param <E> 
 *
 */
public class RRRRRandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<>();
    private final Random random;
    private double total = 0;

    public RRRRRandomCollection() {
        this(new Random());
    }

    public RRRRRandomCollection(Random random) {
        this.random = random;
    }

    public RRRRRandomCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}
