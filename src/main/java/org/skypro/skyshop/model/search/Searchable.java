package org.skypro.skyshop.model.search;

import java.util.UUID;

/**
 * Интерфейс для объектов, поддерживающих поиск.
 * Реализуется товарами и статьями.
 */
public interface Searchable {

    /**
     * Возвращает уникальный идентификатор объекта
     * @return UUID идентификатор
     */
    UUID getId();

    /**
     * Возвращает термин для поиска
     * @return строка для поиска
     */
    String getSearchTerm();

    /**
     * Возвращает тип контента
     * @return тип контента (например, "PRODUCT" или "ARTICLE")
     */
    String getContentType();

    /**
     * Возвращает имя объекта для поиска
     * @return имя объекта
     */
    String getSearchableName();

    /**
     * Возвращает строковое представление объекта
     * @return строка в формате "имя - тип"
     */
    default String getStringRepresentation() {
        return getSearchableName() + " — " + getContentType();
    }
}