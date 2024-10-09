package com.example.l2_ninalaya_crhistian

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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
    private lateinit var lstPosts: MutableList<Posts>
    private val ruta = "https://jsonplaceholder.typicode.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        postAdapter = PostAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = postAdapter

        lstPosts = mutableListOf()
        getPosts()
        if(lstPosts.isNotEmpty()){
            postAdapter.setearPost(lstPosts)
        }

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

        val btnEliminar = findViewById<Button>(R.id.btnEliminar)
        btnEliminar.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eliminar por ID")
            val inputNumber = EditText(this)
            inputNumber.inputType = InputType.TYPE_CLASS_NUMBER
            builder.setView(inputNumber)

            builder.setPositiveButton("Aceptar") { _, _ ->
                val enteredId = inputNumber.text.toString()
                val postId = deletePost(enteredId.toInt())


            }
            builder.setNegativeButton("Cancelar"){ dialog, _ ->
                dialog.cancel()
            }
            val dialog = builder.create()
            dialog.show()
        }
    }
    private fun getPosts() {
        val retrofit = Retrofit.Builder()
            .baseUrl(ruta)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        jsonPlaceHolderApi.getPosts().enqueue(object : Callback<List<Posts>> {
            override fun onResponse(call: Call<List<Posts>>, response: Response<List<Posts>>) {
                if (response.isSuccessful && response.body() != null) {
                    lstPosts.clear()
                    lstPosts.addAll(response.body()!!)
                    postAdapter.setearPost(lstPosts)
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

    private fun deletePost(postId: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)

        jsonPlaceHolderApi.deletePost(postId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    lstPosts.removeAt(postId)
                    postAdapter.notifyDataSetChanged()
                } else {
                    Log.e("MainActivity", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }
        })
    }

}
