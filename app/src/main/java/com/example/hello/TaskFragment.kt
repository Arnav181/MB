package com.example.hello

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class TaskFragment : Fragment() {



    private lateinit var listViewTasks: ListView
    private val tasks = mutableListOf(
        "Healthy Diet",
        "Workout",
        "Drink Water",
        "Take Vitamins",
        "Go for a Walk",
        "Meditate"
    )



    private val completedTasks = BooleanArray(tasks.size) // To track completion status
    private val importantTasks = BooleanArray(tasks.size) // To track important status
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) // Enable options menu in this fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        listViewTasks = view.findViewById(R.id.listViewTasks)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Health Tracker"
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_multiple_choice, tasks)
        listViewTasks.adapter = adapter
        listViewTasks.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Initialize ListView with current completion states
        updateListView()

        // Handle item click to toggle completion status
        listViewTasks.setOnItemClickListener { _, _, position, _ ->
            completedTasks[position] = !completedTasks[position] // Toggle completion status
            updateListView() // Refresh the list view after toggling
        }

        // Handle long click to show pop-up menu for marking tasks as important
        listViewTasks.setOnItemLongClickListener { _, view, position, _ ->
            showPopupMenu(view, position)
            true // Return true to indicate that the long click was handled
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu) // Inflate the options menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                // Navigate back to the login screen (MainActivity)
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish() // Close the current activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showPopupMenu(view: View, position: Int) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.inflate(R.menu.popup_menu) // Inflate the pop-up menu

        // Set up click listeners for the menu items
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_mark_important -> {
                    importantTasks[position] = !importantTasks[position] // Toggle important status
                    updateListView() // Refresh the list view to show changes
                    Toast.makeText(requireContext(), "${tasks[position]} marked as ${if (importantTasks[position]) "important" else "not important"}", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        popupMenu.show() // Show the pop-up menu
    }

    private fun updateListView() {
        // Show tasks and prepend a star for important tasks
        val updatedTasks = tasks.mapIndexed { index, task ->
            if (importantTasks[index]) "★ $task" else task
        }

        adapter.clear()
        adapter.addAll(updatedTasks)
        adapter.notifyDataSetChanged()
    }
}
