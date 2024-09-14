package from_seminar;

import from_seminar.annotations.*;

import java.lang.reflect.Field;
import java.util.UUID;

public class QueryBuilder {

    /**
     * Создание запроса INSERT INTO
     * @param obj объект, с которого берем данные для запроса
     */
    public String buildInsertQuery(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        StringBuilder query = new StringBuilder("INSERT INTO ");
        StringBuilder datas = new StringBuilder(") VALUES ('");

        if (clazz.isAnnotationPresent(Table.class))         // класс помечен аннотацией @Table?
        {
            // добавляем в запрос имя таблицы, ассоциированной с классом
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            query.append(tableAnnotation.name()).append(" (");

            // добавляем в запрос поля
            Field[] fields = clazz.getDeclaredFields();

            for (int i = 0; i < fields.length; i++)
                if (fields[i].isAnnotationPresent(Column.class))
                {
                    Column column = fields[i].getAnnotation(Column.class);
                    if (i != 0)
                    {
                        query.append(", ");
                        datas.append(", '");
                    }
                    query.append(column.name());
                    fields[i].setAccessible(true);
                    datas.append(fields[i].get(obj)).append("'");
                }

            return query.append(datas).append(")").toString();
        }
        return null;
    }



    /**
     * Создание запроса SELECT
     * @param clazz      Тип класса таблицы
     * @param primaryKey UUID ключа искомой записи
     */
    public String buildSelectQuery(Class<?> clazz, UUID primaryKey)
    {
        return buildWhere(clazz, primaryKey, "SELECT *");
    }


    /**
     * Создание запроса на обновление данных
     * @param obj Объект для обновления
     */
    public String buildUpdateQuery(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        StringBuilder query = new StringBuilder("UPDATE ");

        if (clazz.isAnnotationPresent(Table.class))         // класс помечен аннотацией @Table?
        {
            // добавляем в запрос имя таблицы, ассоциированной с классом
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            query.append(tableAnnotation.name()).append(" SET ");

            Field[] fields = clazz.getDeclaredFields();
            Field primary = null;

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class))
                {
                    field.setAccessible(true);
                    Column annotate = field.getAnnotation(Column.class);
                    if (annotate.primaryKey())
                    {
                        primary = field;    // чтобы не бегать по циклу второй раз, сразу запомним ключ
                    } else {
                        query.append(annotate.name()).append(" = '").append(field.get(obj)).append("', ");
                    }
                }
            }
            if (query.lastIndexOf(",") != -1)
                query.delete(query.lastIndexOf(","), query.length());
            query.append(" WHERE ");
            if (primary == null)
                return null;

            primary.setAccessible(true);
            query.append(primary.getAnnotation(Column.class).name()).append(" = '").append(primary.get(obj)).append("'");

            return query.toString();
        }
        return null;
    }

    /**
     * Создаёт запрос на удаление записи по ключу
     * @param clazz      Тип класса таблицы
     * @param primaryKey UUID ключа удаляемой записи
     */
    public String buildDeleteQuery(Class<?> clazz, UUID primaryKey)
    {
        return buildWhere(clazz, primaryKey, "DELETE");
    }


    /**
     * Создает запрос вида " FROM <table> WHERE id = 'key'
     * @param clazz      Тип класса таблицы
     * @param primaryKey UUID ключа искомой записи, над которой проводятся манипуляции
     * @param beginSQL   Начало строки запроса SQL ("SELEC *", "DELETE")
     */
    private String buildWhere(Class<?> clazz, UUID primaryKey, String beginSQL)
    {
        StringBuilder query = new StringBuilder(beginSQL);
        if (clazz.isAnnotationPresent(Table.class))         // класс помечен аннотацией @Table?
        {
            // добавляем в запрос имя таблицы, ассоциированной с классом
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            query.append(" FROM ").append(tableAnnotation.name()).append(" WHERE ");

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class))
                {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    if (columnAnnotation.primaryKey())
                    {
                        query.append(columnAnnotation.name()).append(" = '").append(primaryKey).append("'");
                        break;
                    }
                }
            }
            return query.toString();
        }
        return null;
    }

}
