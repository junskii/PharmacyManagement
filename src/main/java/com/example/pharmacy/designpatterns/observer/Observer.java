package com.example.pharmacy.designpatterns.observer;

public interface Observer<T> {
    void update(T data);
}