package com.example.naturzaragoza.data.Metodos.ArbolVistoMetodos;

import android.os.AsyncTask
import com.example.naturzaragoza.data.DAO.ArbolVistoDAO
import com.example.naturzaragoza.model.ArbolVisto

public class UpdateArbolVistoAsync (private val arbolVistoDao: ArbolVistoDAO, private val call: String) : AsyncTask<ArbolVisto, Void, ArbolVisto>() {

  override fun doInBackground(vararg arbolVisto: ArbolVisto?): ArbolVisto? {
      return try {
          arbolVistoDao.update(arbolVisto[0]!!)
          arbolVisto[0]!!
      } catch (ex: Exception) {
          null
      }
  }

  override fun onPostExecute(result: ArbolVisto?) {
      super.onPostExecute(result)
  }
}
