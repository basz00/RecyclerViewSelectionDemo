package com.baz.rvselection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private companion object {

        private const val SELECTION_ID = "selectionId"
    }

    private val adapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        adapter.list = createRandomIntList()
        adapter.notifyDataSetChanged()
        adapter.tracker = createSelectionTracker()
    }

    private fun createSelectionTracker(): SelectionTracker<Long> {
        val tracker = SelectionTracker.Builder<Long>(
            SELECTION_ID,
            recyclerView,
            StableIdKeyProvider(recyclerView),
            MainItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        tracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {

            override fun onSelectionChanged() {
                super.onSelectionChanged()
                val items = tracker.selection.size()
                if (items == 2) {
                    launchSum(tracker.selection)
                }
            }
        })
        return tracker
    }

    private fun launchSum(selection: Selection<Long>) {
        val list = selection.map { adapter.list[it.toInt()] }.toList()
        Toast.makeText(this, "$list", Toast.LENGTH_SHORT).show()
    }

    private fun createRandomIntList(): List<Int> {
        val random = Random()
        return (1..10).map { random.nextInt() }
    }
}
