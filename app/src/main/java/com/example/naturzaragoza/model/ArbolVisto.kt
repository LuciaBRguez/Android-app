package com.example.naturzaragoza.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_arboles_vistos")
data class ArbolVisto(@ColumnInfo(name="id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
                      @ColumnInfo(name="commonName") val commonName: String,
                      @ColumnInfo(name="comentario") val comentario: String)