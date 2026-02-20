package org.skypro.skyshop.model.basket;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Модель корзины для отображения пользователю.
 * Содержит список элементов корзины и общую стоимость.
 */
public class UserBasket {
    private final List<BasketItem> items;
    private final int total;

    /**
     * Конструктор корзины пользователя
     * @param items список элементов корзины
     */
    public UserBasket(List<BasketItem> items) {
        this.items = List.copyOf(items); // Создаем неизменяемую копию
        this.total = calculateTotal(items);
    }

    /**
     * Вычисляет общую стоимость корзины
     * @param items список элементов
     * @return общая стоимость
     */
    private int calculateTotal(List<BasketItem> items) {
        return items.stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    /**
     * Возвращает список элементов корзины
     * @return неизменяемый список элементов
     */
    public List<BasketItem> getItems() {
        return items;
    }

    /**
     * Возвращает общую стоимость корзины
     * @return общая стоимость
     */
    public int getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "UserBasket{" +
                "items=" + items +
                ", total=" + total +
                '}';
    }
}