package com.flaringapp.reqres.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flaringapp.reqres.R
import com.flaringapp.reqres.main.model.MainScreenModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var model: MainScreenModel

    private var recyclerView: RecyclerView?= null
    private var listAdapter: UsersListAdapter?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()

        init()
    }

    private fun bindViews() {
        recyclerView = findViewById(R.id.list)
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView = null
    }

    private fun init() {
        model = MainScreenModel(this)

        val d = model.requestLoadUsersData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    listAdapter?.updateModels(it)
                },
                {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                }
            )

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        listAdapter = UsersListAdapter()

        recyclerView!!.adapter = listAdapter
        recyclerView!!.layoutManager = layoutManager

        recyclerView!!.setHasFixedSize(false)
    }
}
