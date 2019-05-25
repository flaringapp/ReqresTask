package com.flaringapp.reqres.main.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.flaringapp.reqres.R
import com.flaringapp.reqres.main.model.MainScreenModel
import com.flaringapp.reqres.main.model.objects.ListUser
import com.flaringapp.reqres.main.view.userFragment.UserFragment
import com.flaringapp.reqres.main.view.usersList.UsersListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var model: MainScreenModel

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var recyclerView: RecyclerView? = null
    private var listAdapter: UsersListAdapter? = null

    private val disposables: ArrayList<Disposable> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerView = null
        listAdapter = null
        swipeRefreshLayout = null

        for (disposable in disposables) disposable.dispose()

        model.destroy()
    }

    private fun bindViews() {
        recyclerView = findViewById(R.id.list)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
    }

    private fun init() {
        model = MainScreenModel(this)

        initRecyclerView()
        initSwipeRefreshLayout()

        loadUsersList()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        listAdapter = UsersListAdapter()

        disposables += listAdapter!!.clickEvent.subscribe { openUser(it) }

        recyclerView!!.adapter = listAdapter
        recyclerView!!.layoutManager = layoutManager

        recyclerView!!.setHasFixedSize(false)
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout!!.setOnRefreshListener { updateUsersList() }
    }

    /**
     * Load list of users from database
     */
    private fun loadUsersList() {
        swipeRefreshLayout?.isEnabled = false

        disposables += model.loadUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    listAdapter?.setModels(it)
                    swipeRefreshLayout?.isEnabled = true

                    updateUsersList()
                },
                {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout?.isEnabled = true
                }
            )
    }

    /**
     * Update list of users from internet
     */
    private fun updateUsersList() {
        swipeRefreshLayout?.isRefreshing = true

        disposables += model.downloadUsersData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    listAdapter?.setModels(it)
                },
                {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    swipeRefreshLayout?.isRefreshing = false
                },
                {
                    swipeRefreshLayout?.isRefreshing = false
                }
            )
    }

    private fun openUser(user: ListUser) {
        val fm = supportFragmentManager
        if (fm.findFragmentById(R.id.user_fragment_container) != null) return

        val fragment = UserFragment.newInstance(user.id, user.avatarLink)

        fm.beginTransaction()
            .add(R.id.user_fragment_container, fragment)
            .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right)
            .addToBackStack(null)
            .commit()
    }
}
