/*
    Реализовать простой фреймворк для создания SQL-запросов на основе Java-объектов

    Фреймворк должен позволять аннотировать классы и поля для связывания их
    с таблицами и столбцами в БД.

    1. Создать аннотации, такие как @Entity, @Table, @Column для маппинга классов,
        таблиц и полей в БД.

    2. Механизм генерации SQL-запросов:
        Реализовать класс QueryBuilder, который принимает объект и генерирует
        SQL-запросы для выполнения операций CRUD (Create, Read, Update, Delete) на основании аннотаций.
 */
package from_seminar;

public class Main {
    public static void main(String[] args) throws IllegalAccessException {

        Employee user = new Employee("Andrey", "andnot@yandex.ru");

        QueryBuilder queryBuilder = new QueryBuilder();

        String ins = queryBuilder.buildInsertQuery(user);
        System.out.println("Insert query: " + ins);

        String sel = queryBuilder.buildSelectQuery(Employee.class, user.getId());
        System.out.println("Select query: " + sel);

        user.setEmail("MrDemonid@yandex.ru");
        String upd = queryBuilder.buildUpdateQuery(user);
        System.out.println("Update query: " + upd);

        String del = queryBuilder.buildDeleteQuery(Employee.class, user.getId());
        System.out.println("Delete query: " + del);

    }
}