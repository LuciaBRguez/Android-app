package com.example.naturzaragoza

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.example.naturzaragoza.data.DAO.ArbolVistoDAO
import com.example.naturzaragoza.data.DAO.FamilyDAO
import com.example.naturzaragoza.data.DataBase.AppDatabase
import com.example.naturzaragoza.model.Arbol
import com.example.naturzaragoza.model.ArbolVisto
import com.example.naturzaragoza.model.Family
import org.junit.*

import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.rules.TestRule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class BaseTest {

    @Rule
    @JvmField
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var familyDao: FamilyDAO
    private lateinit var arbolVistoDAO: ArbolVistoDAO

    @Before
    fun setup() {
        val context: Context = InstrumentationRegistry.getTargetContext()
        try {
            database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries().build()
        } catch (e: Exception) {
            Log.i("test", e.message)
        }
        familyDao = database.familyDao()
        arbolVistoDAO = database.arbolVistoDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testAddingAndRetrievingData() {
        // Datos en la base
        val preInsertRetrievedFamilies = familyDao.getAll()
        val preInsertRetrievedArbolVistos= arbolVistoDAO.getAll()

        // Inserción de datos
        val arbolVisto = ArbolVisto(commonName = "Arbol",comentario = "hola" )
        arbolVistoDAO.insertAll(arbolVisto)

        val familia = Family(family="familia")
        familyDao.insertAll(familia)

        //Comprobación de cambios en ArbolesVistos
        val postInsertRetrievedArbolVistos = arbolVistoDAO.getAll()
        val sizeDifference = postInsertRetrievedArbolVistos.size - preInsertRetrievedArbolVistos.size
        Assert.assertEquals(1, sizeDifference)
        val retrievedArbolVisto = postInsertRetrievedArbolVistos.last()
        Assert.assertEquals("Arbol", retrievedArbolVisto.commonName)
        Assert.assertEquals("hola", retrievedArbolVisto.comentario)

        //Comprobación de cambios en Familias
        val postInsertRetrievedFamilias = familyDao.getAll()
        val sizeDifferencefam = postInsertRetrievedFamilias.size - preInsertRetrievedFamilies.size
        Assert.assertEquals(1, sizeDifferencefam)
        val retrievedFamilia = postInsertRetrievedFamilias.last()
        Assert.assertEquals("familia", retrievedFamilia.family)

        //Actualizacion de datos
        val arbolVisto2=ArbolVisto(id=retrievedArbolVisto.id,commonName = retrievedArbolVisto.commonName,comentario = "adios")
        arbolVistoDAO.update(arbolVisto2)
        val postUpdateRetrievedArbolVistos = arbolVistoDAO.getAll()
        val retrievedArbolVisto2 = postUpdateRetrievedArbolVistos.last()
        Assert.assertEquals("adios", retrievedArbolVisto2.comentario)

        // Eliminación de datos
        arbolVistoDAO.delete(retrievedArbolVisto2)
        val numarboles=arbolVistoDAO.getExiste(retrievedArbolVisto2.commonName)
        Assert.assertEquals(0, numarboles.size)


    }
}
