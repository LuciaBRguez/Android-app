package com.example.naturzaragoza.data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.naturzaragoza.model.Family

@Dao
interface FamilyDAO {

    @Query("SELECT * FROM list_families")
    fun getAll(): List<Family>

    @Insert
    fun insertAll(vararg listFamilies: Family)
}