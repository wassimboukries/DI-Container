package org.example;

import org.example.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Injector {
    private Map<Class<?>, Class<?>> myMap;

    public Injector() {
        myMap = new HashMap<>();
    }

    public void register(Class<?> myClass, Class<?> dependency) {
        myMap.put(myClass, dependency);
    }

    public <T> Object newInstance(Class<?> myClass) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> implementationClass = getImplementationClass(myClass);

        Object service = implementationClass.getDeclaredConstructor().newInstance();
        autowire(implementationClass, service);
        return service;
    }

    private Class<?> getImplementationClass(Class<?> myClass) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException{
        Set<Map.Entry<Class<?>, Class<?>>> implementationClasses = myMap.entrySet().stream()
                .filter(entry -> entry.getKey() == myClass).collect(Collectors.toSet());

        Optional<Map.Entry<Class<?>, Class<?>>> optional = implementationClasses.stream().findFirst();
        return optional.get().getValue();
    }

    private void autowire(Class<?> myClass, Object myClassInstance) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        Field[] myFields = myClass.getDeclaredFields();

        Stream<Field> annotatedFields = Arrays.stream(myFields).filter(field ->
            field.isAnnotationPresent(Autowired.class)
        );

        for (Field field : annotatedFields.toArray(Field[]::new)){
            Object fieldInstance = newInstance(field.getType());
            field.setAccessible(true);
            field.set(myClassInstance, fieldInstance);
        }
    }
}
