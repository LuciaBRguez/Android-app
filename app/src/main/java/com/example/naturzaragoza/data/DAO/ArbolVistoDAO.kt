package com.example.naturzaragoza.data.DAO

import androidx.room.*
import com.example.naturzaragoza.model.ArbolVisto

@Dao
interface ArbolVistoDAO {

    @Query("SELECT * FROM list_arboles_vistos")
    fun getAll(): MutableList<ArbolVisto>

    @Insert
    fun insertAll(vararg listArbolesVistos: ArbolVisto)

    @Query("SELECT * FROM list_arboles_vistos WHERE commonName LIKE :name")
    fun getExiste(name: String): List<ArbolVisto>

    @Delete
    fun delete(arbolVisto: ArbolVisto)

    @Update
    fun update(arbolVisto : ArbolVisto)
}