package com.example.l2_ninalaya_crhistian

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.l2_ninalaya_crhistian.adapters.PostAdapter
import com.example.l2_ninalaya_crhistian.databinding.ActivityMainBinding
import com.example.l2_ninalaya_crhistian.interfaz.JsonPlaceHolderApi
import com.example.l2_ninalaya_crhistian.model.Posts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var postAdapter: PostAdapter

    private val ruta = "https://jsonplaceholder.typicode.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postAdapter = PostAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = postAdapter



        // Inicializar el TextView usando findViewById después de setContentView

        getPosts()

        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        btnBuscar.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Buscar por ID")
            val inputNumber = EditText(this)
            inputNumber.inputType = InputType.TYPE_CLASS_NUMBER
            builder.setView(inputNumber)
            builder.setPositiveButton("Aceptar") { _, _ ->
                val enteredId = inputNumber.text.toString()
                getPostById(enteredId.toInt())
            }
            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
            val dialog = builder.create()
            dialog.show()
        }
        val btnInsertar = findViewById<Button>(R.id.btnInsertar)
        btnInsertar.setOnClickListener {
            val intent = Intent(this, InsertActivity::class.java)
            startActivity(intent)
        }

        val btnActualizar = findViewById<Button>(R.id.btnActualizar)
        btnActualizar.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Actualizar por ID")
            val inputNumber = EditText(this)
            inputNumber.inputType = InputType.TYPE_CLASS_NUMBER
            builder.setView(inputNumber)

            builder.setPositiveButton("Aceptar") { _, _ ->
                val enteredId = inputNumber.text.toString()

                val intent = Intent(this, UpdateActivity::class.java)
                intent.putExtra("postId", enteredId.toInt())
                startActivity(intent)
            }

            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.cancel()
            }
            val dialog = builder.create()
            dialog.show()
        }
        // updatePost(12)
//        deletePost(12)
    }
    private fun getPosts() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ruta)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        jsonPlaceHolderApi.getPosts().enqueue(object :Callback<List<Posts>>{
            override fun onResponse(call: Call<List<Posts>>, response: Response<List<Posts>>) {
                if (response.isSuccessful && response.body() != null) {
                    postAdapter.setearPost(response.body()!!)
                }
            }
            override fun onFailure(call: Call<List<Posts>>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
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
                    // Por ejemplo, actualiza tu RecyclerView con este post
                    postAdapter.setearPost(listOf(response.body()!!))
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
//
//
//    private fun updatePost(postId: Int) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://jsonplaceholder.typicode.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)
//
//        // Crear un nuevo objeto Posts con los datos actualizados
//        val updatedPost = Posts()
//        updatedPost.setUserId(1)
//        updatedPost.setId(postId)  // Establecer el ID del post que quieres actualizar
//        updatedPost.setTitle("Título actualizado")
//        updatedPost.setBody("Contenido actualizado")
//
//        val call = jsonPlaceHolderApi.updatePost(postId, updatedPost)
//
//        call.enqueue(object : Callback<Posts> {
//            override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
//                if (!response.isSuccessful) {
//                    println("Código: ${response.code()}")
//                    return
//                }
//
//                val postResponse = response.body()
//                if (postResponse != null) {
//                    println("Post actualizado con éxito:")
//                    println("ID: ${postResponse.getId()}")
//                    println("Título: ${postResponse.getTitle()}")
//                    println("Contenido: ${postResponse.getBody()}")
//                    var content="Post insertado con éxito:"
//                    content+="ID: ${postResponse.getId()}" +"\n"
//                    content+="Título: ${postResponse.getTitle()}" +"\n"
//                    content+="Contenido: ${postResponse.getBody()}"+"\n"
//                }
//            }
//
//            override fun onFailure(call: Call<Posts>, t: Throwable) {
//                println("Error: ${t.message}")
//            }
//        })
//    }

//    private fun deletePost(postId: Int) {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://jsonplaceholder.typicode.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)
//
//        val call = jsonPlaceHolderApi.deletePost(postId)
//
//        call.enqueue(object : Callback<Void> {
//            override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                if (!response.isSuccessful) {
//                    println("Código: ${response.code()}")
//                    return
//                }
//
//                println("Post eliminado con éxito, ID: $postId")
//                mjsonTxtView.append("Post eliminado con éxito, ID: $postId")
//            }
//
//            override fun onFailure(call: Call<Void>, t: Throwable) {
//                println("Error: ${t.message}")
//            }
//        })
//    }