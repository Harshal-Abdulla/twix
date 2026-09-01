package com.twix.app.observer;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
public class UserSubject implements Subject {
    private final Set<Observer> observers = new HashSet<>();

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        observers.forEach(observer -> observer.update(message));
    }
}