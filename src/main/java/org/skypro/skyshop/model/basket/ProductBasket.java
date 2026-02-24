package org.skypro.skyshop.model.basket;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import java.util.*;

/**
 * Компонент корзины пользователя.
 * Хранит товары в рамках сессии пользователя.
 */
@Component
@SessionScope
public class ProductBasket {
    private final Map<UUID, Integer> basketItems;

    /**
     * Конструктор корзины
     */
    public ProductBasket() {
        this.basketItems = new HashMap<>();
    }

    /**
     * Добавляет продукт в корзину
     * @param id идентификатор продукта
     */
    public void addProduct(UUID id) {
        basketItems.computeIfAbsent(id, k -> 0);
        basketItems.compute(id, (key, count) -> count + 1);
    }

    /**
     * Возвращает неизменяемую копию корзины
     * @return неизменяемая мапа товаров
     */
    public Map<UUID, Integer> getBasketItems() {
        return Collections.unmodifiableMap(basketItems);
    }
}