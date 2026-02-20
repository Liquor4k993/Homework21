package org.skypro.skyshop.model.search;


import java.util.UUID;

/**
 * Модель результата поиска.
 * Представляет упрощенное представление объекта Searchable для отображения в UI.
 */
public class SearchResult {
    private final UUID id;
    private final String name;
    private final String contentType;

    /**
     * Конструктор результата поиска
     * @param id уникальный идентификатор
     * @param name название объекта
     * @param contentType тип контента
     */
    public SearchResult(UUID id, String name, String contentType) {
        this.id = id;
        this.name = name;
        this.contentType = contentType;
    }

    /**
     * Фабричный метод для создания SearchResult из Searchable
     * @param searchable объект, поддерживающий поиск
     * @return результат поиска
     */
    public static SearchResult fromSearchable(Searchable searchable) {
        return new SearchResult(
                searchable.getId(),
                searchable.getSearchableName(),
                searchable.getContentType()
        );
    }

    /**
     * Возвращает уникальный идентификатор
     * @return UUID идентификатор
     */
    public UUID getId() {
        return id;
    }

    /**
     * Возвращает название объекта
     * @return название
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает тип контента
     * @return тип контента
     */
    public String getContentType() {
        return contentType;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}