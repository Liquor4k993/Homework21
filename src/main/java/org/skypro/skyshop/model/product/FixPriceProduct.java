package org.skypro.skyshop.model.product;


import java.util.UUID;

/**
 * Класс, представляющий товар с фиксированной ценой.
 */
public class FixPriceProduct extends Product {
    private static final int FIXED_PRICE = 99;

    /**
     * Конструктор товара с фиксированной ценой
     * @param id уникальный идентификатор
     * @param name название товара
     */
    public FixPriceProduct(UUID id, String name) {
        super(id, name);
    }

    /**
     * Получить фиксированную цену товара
     * @return фиксированная цена
     */
    @Override
    public int getPrice() {
        return FIXED_PRICE;
    }

    /**
     * Проверяет, является ли товар специальным
     * @return true - товар с фиксированной ценой является специальным
     */
    @Override
    public boolean isSpecial() {
        return true;
    }

    /**
     * Возвращает строковое представление товара
     * @return строка в формате "название: Фиксированная цена FIXED_PRICE"
     */
    @Override
    public String toString() {
        return getName() + ": Фиксированная цена " + FIXED_PRICE;
    }
}