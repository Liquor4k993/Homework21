package org.skypro.skyshop.model.basket;

import org.skypro.skyshop.model.product.Product;

/**
 * Модель элемента корзины.
 * Содержит продукт и его количество.
 */
public class BasketItem {
    private final Product product;
    private final int quantity;

    /**
     * Конструктор элемента корзины
     * @param product продукт
     * @param quantity количество
     */
    public BasketItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * Возвращает продукт
     * @return продукт
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Возвращает количество
     * @return количество
     */
    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "BasketItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}