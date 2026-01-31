package org.skypro.skyshop.model.article;

import org.skypro.skyshop.model.search.Searchable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import java.util.UUID;

/**
 * Класс, представляющий статью о товаре.
 * Статья является неизменяемым объектом.
 */
public class Article implements Searchable {
    private final UUID id;
    private final String title;
    private final String text;

    /**
     * Конструктор статьи
     * @param id уникальный идентификатор
     * @param title название статьи
     * @param text текст статьи
     * @throws IllegalArgumentException если название null или пустое
     */
    public Article(UUID id, String title, String text) {
        if (id == null) {
            throw new IllegalArgumentException("ID статьи не может быть null");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Название статьи не может быть пустым или состоять только из пробелов"
            );
        }

        if (text == null) {
            text = ""; // Разрешаем пустой текст
        }

        this.id = id;
        this.title = title;
        this.text = text;
    }

    @Override
    public UUID getId() {
        return id;
    }

    /**
     * Получить название статьи
     * @return название статьи
     */
    public String getTitle() {
        return title;
    }

    /**
     * Получить текст статьи
     * @return текст статьи
     */
    public String getText() {
        return text;
    }

    /**
     * Преобразует статью в строку для отображения
     * @return строка в формате "Название\nТекст"
     */
    @Override
    public String toString() {
        return title + "\n" + text;
    }

    // Реализация методов интерфейса Searchable

    /**
     * Возвращает термин для поиска (название + текст)
     * @return строка для поиска
     */
    @Override
    @JsonIgnore
    public String getSearchTerm() {
        return toString();
    }

    /**
     * Возвращает тип контента
     * @return "ARTICLE"
     */
    @Override
    @JsonIgnore
    public String getContentType() {
        return "ARTICLE";
    }

    /**
     * Возвращает имя объекта для поиска
     * @return название статьи
     */
    @Override
    public String getSearchableName() {
        return title;
    }

    // Реализация equals и hashCode

    /**
     * Сравнивает статьи по ID
     * @param o объект для сравнения
     * @return true если ID статей равны
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    /**
     * Возвращает хэш-код статьи на основе ID
     * @return хэш-код
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}