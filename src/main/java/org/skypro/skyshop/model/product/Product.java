package org.skypro.skyshop.model.product;

import org.skypro.skyshop.model.search.Searchable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import java.util.UUID;

/**
 * Абстрактный класс, представляющий товар в интернет-магазине.
 * Является корнем иерархии для всех типов товаров.
 * Реализует интерфейс Searchable для поддержки поиска.
 */
public abstract class Product implements Searchable {
    private final UUID id;
    private final String name;

    /**
     * Конструктор товара
     * @param id уникальный идентификатор
     * @param name название товара
     * @throws IllegalArgumentException если название null или пустое
     */
    public Product(UUID id, String name) {
        if (id == null) {
            throw new IllegalArgumentException("ID товара не может быть null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Название продукта не может быть пустым или состоять только из пробелов"
            );
        }
        this.id = id;
        this.name = name;
    }

    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Получить название товара
     * @return название товара
     */
    public String getName() {
        return name;
    }

    /**
     * Получить цену товара
     * @return цена товара
     */
    public abstract int getPrice();

    /**
     * Проверяет, является ли товар специальным
     * @return true если товар специальный, false если обычный
     */
    public abstract boolean isSpecial();

    /**
     * Возвращает строковое представление товара
     * @return строка с описанием товара
     */
    @Override
    public abstract String toString();

    // Реализация методов интерфейса Searchable

    /**
     * Возвращает термин для поиска (имя товара)
     * @return имя товара
     */
    @Override
    @JsonIgnore
    public String getSearchTerm() {
        return name;
    }

    /**
     * Возвращает тип контента
     * @return "PRODUCT"
     */
    @Override
    @JsonIgnore
    public String getContentType() {
        return "PRODUCT";
    }

    /**
     * Возвращает имя объекта для поиска
     * @return название товара
     */
    @Override
    public String getSearchableName() {
        return name;
    }

    // Реализация equals и hashCode

    /**
     * Сравнивает продукты по имени
     * @param o объект для сравнения
     * @return true если имена продуктов равны
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    /**
     * Возвращает хэш-код продукта на основе ID
     * @return хэш-код
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}