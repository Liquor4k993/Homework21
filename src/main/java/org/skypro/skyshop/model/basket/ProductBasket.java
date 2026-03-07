package org.skypro.skyshop.model.basket;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import java.util.*;

@Component
@SessionScope
public class ProductBasket {
    private final Map<UUID, Integer> basketItems;

    public ProductBasket() {
        this.basketItems = new HashMap<>();
    }

    public void addProduct(UUID id) {
        basketItems.computeIfAbsent(id, k -> 0);
        basketItems.compute(id, (key, count) -> count + 1);
    }

    public Map<UUID, Integer> getBasketItems() {
        return Collections.unmodifiableMap(basketItems);
    }
}