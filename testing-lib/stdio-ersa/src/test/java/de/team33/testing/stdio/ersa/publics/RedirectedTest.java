package de.team33.testing.stdio.ersa.publics;

import de.team33.testing.stdio.ersa.Redirected;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("UseOfSystemOutOrSystemErr")
class RedirectedTest {

    @Test
    final void outputOf() throws IOException {
        final String original = UUID.randomUUID().toString();
        final String result = Redirected.outputOf(() -> System.out.println(original));
        final String expected = String.format("%s%n", original);
        assertEquals(expected, result);
    }
}
