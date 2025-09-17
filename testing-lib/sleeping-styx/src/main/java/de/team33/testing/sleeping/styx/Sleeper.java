package de.team33.testing.sleeping.styx;

import java.util.concurrent.TimeUnit;

public class Sleeper {

    public final void sleep(final long milliseconds) {
        sleep(milliseconds, TimeUnit.MILLISECONDS);
    }

    public final void sleep(final long time, final TimeUnit unit) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
