package org.skypro.skyshop.model.product;

import java.util.UUID;

/**
 * Класс, представляющий обычный товар без скидок и специальных условий.
 */
public class SimpleProduct extends Product {
    private final int price;

    /**
     * Конструктор обычного товара
     * @param id уникальный идентификатор
     * @param name название товара
     * @param price цена товара в рублях
     * @throws IllegalArgumentException если цена <= 0
     */
    public SimpleProduct(UUID id, String name, int price) {
        super(id, name);
        if (price <= 0) {
            throw new IllegalArgumentException(
                    "Цена товара должна быть строго больше 0. Получено: " + price
            );
        }
        this.price = price;
    }

    /**
     * Получить цену товара
     * @return цена товара
     */
    @Override
    public int getPrice() {
        return price;
    }

    /**
     * Проверяет, является ли товар специальным
     * @return false - обычный товар не является специальным
     */
    @Override
    public boolean isSpecial() {
        return false;
    }

    /**
     * Возвращает строковое представление товара
     * @return строка в формате "название: цена"
     */
    @Override
    public String toString() {
        return getName() + ": " + getPrice();
    }
}