package com.example.customadapterdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userList = listOf(
            User("Cuong", "Nghe An"),
            User("Giang", "Binh Dinh"),
            User("Mai", "Soc Trang")
        )
        val listView = findViewById<ListView>(R.id.user_list_view)
        val adapter = UserAdapter(this, userList)
        listView.adapter = adapter
    }
}