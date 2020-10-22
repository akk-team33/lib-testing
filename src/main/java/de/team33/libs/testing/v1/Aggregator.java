package de.team33.libs.testing.v1;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Stream;

class Aggregator<C, E> {

    private final SortedMap<C, List<E>> backing;

    Aggregator(final Comparator<? super C> order) {
        backing = new TreeMap<>(order);
    }

    final synchronized Aggregator add(final C category, final E element) {
        backing.computeIfAbsent(category, c -> new LinkedList<>())
               .add(element);
        return this;
    }

    final synchronized <R> R map(final Function<Stream<E>, R> method) {
        final Stream<E> stream = backing.values()
                                        .stream()
                                        .flatMap(List::stream);
        return method.apply(stream);
    }
}
