package com.example.reddittoppublication.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.reddittoppublication.R
import com.example.reddittoppublication.presentation.fragments.postlist.PostListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.commit {
            replace<PostListFragment>(R.id.fragment_main_view)
            setReorderingAllowed(true)
        }
    }
}