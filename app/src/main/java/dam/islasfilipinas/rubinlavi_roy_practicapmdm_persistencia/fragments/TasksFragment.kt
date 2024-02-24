package dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.R
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.TaskAdapter
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.TaskViewModel
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.databinding.FragmentTasksBinding

class TasksFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSpinner()

        binding.buttonAddTask.setOnClickListener {
            findNavController().navigate(R.id.action_tasksFragment_to_addEditTaskFragment)
        }

        viewModel.getAllTasks().observe(viewLifecycleOwner, Observer { tasks ->
            taskAdapter.updateTasks(tasks)
        })
    }

    private fun filterTasks(module: String) {
        viewModel.getAllTasks().observe(viewLifecycleOwner, Observer { tasks ->
            val filteredTasks = if (module == "Todos los módulos") {
                tasks
            } else {
                tasks.filter { it.module == module }
            }
            taskAdapter.updateTasks(filteredTasks)
        })
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
        taskAdapter = TaskAdapter(emptyList()) { selectedTask ->
            val bundle = bundleOf("taskId" to selectedTask.id)
            findNavController().navigate(R.id.action_tasksFragment_to_addEditTaskFragment, bundle)
        }

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
                val task = taskAdapter.tasks[position]
                viewModel.deleteTask(task)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewTasks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
