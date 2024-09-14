/*
Используя Reflection API, напишите программу, которая выводит
на экран все методы класса String.
 */

package original;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {

        String base = "";

        // Конструктора - это тоже в некотором роде методы, получаем их список
        Class<?> cl = String.class;
        Constructor<?>[] constructors = cl.getDeclaredConstructors();

        // теперь методы
        Method[] methods = cl.getDeclaredMethods();

        // не было в задании, но для комплекта
        Field[] fields = cl.getDeclaredFields();



        System.out.println("\nFields:\n-------------------------------------");
        Arrays.stream(fields).forEach(System.out::println);
        System.out.println("\nConstructors:\n-------------------------------------");
        Arrays.stream(constructors).forEach(System.out::println);
        System.out.println("\nMethods:\n-------------------------------------");
        Arrays.stream(methods).forEach(System.out::println);

    }
}