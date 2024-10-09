package com.example.l2_ninalaya_crhistian

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.example.l2_ninalaya_crhistian.interfaz.JsonPlaceHolderApi
import com.example.l2_ninalaya_crhistian.model.Posts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InsertActivity : AppCompatActivity() {
    private lateinit var mjsonTxtView: TextView
    private lateinit var txtid: AppCompatEditText
    private lateinit var txtuserId: AppCompatEditText
    private lateinit var txtTitle: AppCompatEditText
    private lateinit var txtbody: AppCompatEditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.insert_post)

        // Inicializar vistas
        txtid = findViewById(R.id.txtid)
        txtuserId = findViewById(R.id.txtuserId)
        txtTitle = findViewById(R.id.txtTitle)
        txtbody = findViewById(R.id.txtbody)
        mjsonTxtView = findViewById(R.id.jsonTextInsert)

        // Botón "Volver"
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Botón "Insertar"
        val btnInsertar = findViewById<Button>(R.id.btnInsertar)
        btnInsertar.setOnClickListener {
            createPost()
        }
    }

    private fun createPost() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        val userId = txtuserId.text.toString().toInt()
        val title = txtTitle.text.toString()
        val body = txtbody.text.toString()

        val newPost = Posts()
        newPost.setUserId(userId)
        newPost.setTitle(title)
        newPost.setBody(body)

        val call = jsonPlaceHolderApi.createPost(newPost)

        call.enqueue(object : Callback<Posts> {
            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                if (!response.isSuccessful) {
                    println("Código: ${response.code()}")
                    return
                }

                val postResponse = response.body()
                if (postResponse != null) {
                    println("Post insertado con éxito:")
                    println("ID: ${postResponse.getId()}")
                    println("UserID: ${postResponse.getUserId()}")
                    println("Título: ${postResponse.getTitle()}")
                    println("Contenido: ${postResponse.getBody()}")

                    var content = "Post insertado con éxito:\n"
                    content += "ID: ${postResponse.getId()}\n"
                    content += "UserID: ${postResponse.getUserId()}\n"
                    content += "Título: ${postResponse.getTitle()}\n"
                    content += "Contenido: ${postResponse.getBody()}\n"
                    mjsonTxtView.append(content)
                    Toast.makeText(this@InsertActivity, "Post insertado con éxito", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Posts>, t: Throwable) {
                println("Error: ${t.message}")
            }
        })
    }

}
