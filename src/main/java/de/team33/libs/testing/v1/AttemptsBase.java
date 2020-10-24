package de.team33.libs.testing.v1;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AttemptsBase<X extends Exception> implements Attempts<X> {

    private static final BinaryOperator<Throwable> ADD_SUPPRESSED = (a, b) -> {
        a.addSuppressed(b);
        return a;
    };

    private final SortedMap<Category, List<Throwable>> backing = new TreeMap<>(Enum::compareTo);

    final synchronized void addCaught(final Category category, final Throwable caught) {
        backing.computeIfAbsent(category, c -> new LinkedList<>())
               .add(caught);
    }

    @Override
    public final void reThrowCaught() throws X {
        final Throwable result = mapCaught(stream -> stream.reduce(ADD_SUPPRESSED).orElse(null));
        if (result instanceof Error) {
            throw (Error)result;
        } else if (result instanceof RuntimeException) {
            throw (RuntimeException)result;
        } else if (result != null) {
            // can only be of type <X> ...
            // noinspection unchecked
            throw (X)result;
        }
    }

    @Override
    public List<Throwable> getCaught() {
        return mapCaught(stream -> stream.collect(Collectors.toList()));
    }

    // @Override
    private <R> R mapCaught(final Function<Stream<Throwable>, R> mapper) {
        return mapper.apply(backing.values()
                                   .stream()
                                   .flatMap(List::stream));
    }

    enum Category {
        ERROR,
        UNCHECKED,
        CHECKED,
        ADDITIONAL;
    }
}
