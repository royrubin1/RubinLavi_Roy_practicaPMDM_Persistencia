package dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.Task
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.TaskAdapter
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.databinding.FragmentTasksBinding

class TasksFragment : Fragment() {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter
    private var allTasks = listOf<Task>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSpinner()
        initializeTasks()
    }

    private fun initializeTasks() {
        allTasks = listOf(
            Task("Tarea 1", "Descripción de la tarea 1", "Acceso a Datos"),
            Task("Tarea 2", "Descripción de la tarea 2", "Programación Multimedia y Dispositivos Móviles"),
            Task("Tarea 3", "Descripción de la tarea 3", "Desarrollo de Interfaces"),
            Task("Tarea 4", "Descripción de la tarea 4", "Sistemas de Gestión Empresarial"),
            Task("Tarea 5", "Descripción de la tarea 5", "Acceso a Datos")
        )
        taskAdapter.updateTasks(allTasks)
    }

    private fun filterTasks(module: String) {
        val filteredTasks = if (module == "Todos los módulos") {
            allTasks
        } else {
            allTasks.filter { it.module == module }
        }
        taskAdapter.updateTasks(filteredTasks)
    }

    private fun setupSpinner() {
        val modules = arrayOf("Todos los módulos", "Acceso a Datos", "Programación Multimedia y Dispositivos Móviles", "Desarrollo de Interfaces", "Sistemas de Gestión Empresarial")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, modules)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerModules.adapter = adapter

        binding.spinnerModules.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                filterTasks(modules[position])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(allTasks)

        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskAdapter
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                allTasks[position].isDone = !allTasks[position].isDone
                taskAdapter.notifyItemChanged(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewTasks)
    }

}
