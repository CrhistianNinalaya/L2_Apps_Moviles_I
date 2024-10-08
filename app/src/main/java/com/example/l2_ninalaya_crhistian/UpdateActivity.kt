package com.example.l2_ninalaya_crhistian

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.example.l2_ninalaya_crhistian.adapters.PostAdapter
import com.example.l2_ninalaya_crhistian.interfaz.JsonPlaceHolderApi
import com.example.l2_ninalaya_crhistian.model.Posts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UpdateActivity : AppCompatActivity() {
    private lateinit var mjsonTxtView: TextView
    private lateinit var txtid: AppCompatEditText
    private lateinit var txtuserId: AppCompatEditText
    private lateinit var txtTitle: AppCompatEditText
    private lateinit var txtbody: AppCompatEditText
    private lateinit var postAdapter: PostAdapter

    private val ruta = "https://jsonplaceholder.typicode.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actualizar_post)

        // Inicializar vistas
        txtid = findViewById(R.id.txtid)
        txtuserId = findViewById(R.id.txtuserId)
        txtTitle = findViewById(R.id.txtTitle)
        txtbody = findViewById(R.id.txtbody)
        mjsonTxtView = findViewById(R.id.jsonTextInsert)

        val postId= intent.getIntExtra("postId", -1)

        getPostById(postId)
        Log.e("postId", postId.toString())

        // Botón "Volver"
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Botón "Acutalizar"
        val btnInsertar = findViewById<Button>(R.id.btnInsertar)
        btnInsertar.setOnClickListener {
        }
    }

    private fun getPostById(id: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl(ruta)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        jsonPlaceHolderApi.getPostById(id).enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                if (response.isSuccessful && response.body() != null) {
                    val post = response.body()
                    txtid.setText(post?.id.toString())
                    txtuserId.setText(post?.userId.toString())
                    txtTitle.setText(post?.title)
                    txtbody.setText(post?.body)
                } else {
                    Log.e("MainActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Posts>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }

}
