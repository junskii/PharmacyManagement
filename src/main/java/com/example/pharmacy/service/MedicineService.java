package com.example.pharmacy.service;

import com.example.pharmacy.mapper.MedicineMapper;
import com.example.pharmacy.models.Medicine;
import com.example.pharmacy.designpatterns.observer.Observable;
import com.example.pharmacy.designpatterns.observer.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MedicineService implements Observable<Medicine> {
    @Autowired
    private MedicineMapper medicineMapper;

    private List<Observer<Medicine>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<Medicine> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<Medicine> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Medicine data) {
        for (Observer<Medicine> observer : observers) {
            observer.update(data);
        }
    }

    public List<Medicine> findAll() {
        return medicineMapper.findAll();
    }

    public Medicine getById(Integer id) {
        return medicineMapper.findById(id);
    }

    public boolean add(Medicine medicine) {
        if (medicine.getStock() == null || medicine.getStock() < 0)
            return false;
        medicineMapper.insert(medicine);
        notifyObservers(medicine);
        return true;
    }

    public boolean update(Medicine medicine) {
        if (medicine.getStock() == null || medicine.getStock() < 0)
            return false;
        medicineMapper.update(medicine);
        notifyObservers(medicine);
        return true;
    }

    public void delete(Integer id) {
        medicineMapper.delete(id);
    }
}