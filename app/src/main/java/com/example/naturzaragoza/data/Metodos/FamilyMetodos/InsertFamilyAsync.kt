package com.example.naturzaragoza.data.Metodos.FamilyMetodos;

import android.os.AsyncTask
import com.example.naturzaragoza.data.DAO.FamilyDAO
import com.example.naturzaragoza.model.Family

public class InsertFamilyAsync (private val familyDao: FamilyDAO, private val call: String) : AsyncTask<Family, Void, Family>() {

  override fun doInBackground(vararg family: Family?): Family? {
      return try {
          familyDao.insertAll(family[0]!!)
          family[0]!!
      } catch (ex: Exception) {
          null
      }
  }

  override fun onPostExecute(result: Family?) {
      super.onPostExecute(result)
  }
}
