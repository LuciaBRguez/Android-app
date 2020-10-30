package com.example.naturzaragoza.data.DataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.naturzaragoza.data.DAO.ArbolVistoDAO
import com.example.naturzaragoza.data.DAO.FamilyDAO
import com.example.naturzaragoza.model.ArbolVisto
import com.example.naturzaragoza.model.Family

@Database(entities = [(Family::class), (ArbolVisto::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun familyDao(): FamilyDAO
    abstract fun arbolVistoDao(): ArbolVistoDAO
}

