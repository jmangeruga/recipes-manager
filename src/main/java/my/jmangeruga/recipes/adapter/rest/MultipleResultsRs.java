package my.jmangeruga.recipes.adapter.rest;

import java.util.Collection;

final class MultipleResultsRs<T> {

    private final Collection<T> items;

    public MultipleResultsRs(Collection<T> items) {
        this.items = items;
    }

    public Collection<T> getItems() {
        return items;
    }
}
