package com.example.customadapterdemo1

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val arrayOfUsers = ArrayList<User>()
        arrayOfUsers.add(User("Harry", "San Diego"))
        arrayOfUsers.add(User("Marla", "San Francisco"))
        arrayOfUsers.add(User("Sarah", "San Marco"))

        val adapter = UsersAdapter(this, arrayOfUsers)

        val listView = findViewById<ListView>(R.id.lvItems)

        listView.adapter = adapter
    }
}