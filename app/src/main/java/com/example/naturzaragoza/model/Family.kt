package com.example.naturzaragoza.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_families")
data class Family(@ColumnInfo(name="id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
                  @ColumnInfo(name="familia") val family: String)