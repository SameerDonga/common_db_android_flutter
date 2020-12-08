package com.retroportalstudio.www.background_service.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.retroportalstudio.www.background_service.Database.Objects.Dog;

import java.util.List;

@Dao
public interface DogDao {

    @Query("SELECT * FROM dogs")
    List<Dog> loadAllDogs();

    @Insert
    void insertDog(Dog dog);

    @Update
    void updateDog(Dog dog);

    @Delete
    void delete(Dog dog);

    @Query("SELECT * FROM dogs WHERE id = :id")
    Dog loadDogById(int id);
}