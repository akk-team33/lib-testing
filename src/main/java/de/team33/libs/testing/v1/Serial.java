package de.team33.libs.testing.v1;

class Serial<X extends Exception> extends AttemptsBase<X> {

    private final int count;
    private final XConsumer<Integer, X> xConsumer;

    Serial(final int count, final XConsumer<Integer, X> xConsumer) {
        this.count = count;
        this.xConsumer = xConsumer;
    }

    final Serial<X> run() {
        for (int i = 0; i < count; ++i) {
            try {
                xConsumer.accept(i);
            } catch (final Error caught) {
                addCaught(Category.ERROR, caught);
            } catch (final RuntimeException caught) {
                addCaught(Category.UNCHECKED, caught);
            } catch (final Throwable caught) {
                // can only be of type <X> ...
                addCaught(Category.CHECKED, caught);
            }
        }
        return this;
    }
}
