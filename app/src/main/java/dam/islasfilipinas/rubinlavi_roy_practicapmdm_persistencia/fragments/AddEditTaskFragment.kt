package dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.R
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.TaskViewModel
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.api.RetrofitInstance
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.databinding.FragmentAddEditTaskBinding
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.room.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditTaskFragment : Fragment() {

    private var _binding: FragmentAddEditTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()
    private var modulesList: List<String> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf<String>())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTaskModule.adapter = adapter
        loadModules()

        val taskId = arguments?.getInt("taskId")
        if (taskId != null && taskId != 0) {
            viewModel.getTaskById(taskId).observe(viewLifecycleOwner) { task ->
                binding.editTextTaskTitle.setText(task.title)
                binding.editTextTaskDescription.setText(task.description)
                val modulePosition = modulesList.indexOf(task.module)
                if (modulePosition >= 0) {
                    binding.spinnerTaskModule.setSelection(modulePosition)
                }
            }
        }

        binding.buttonSaveTask.setOnClickListener {
            saveTask(taskId)
        }
    }

    private fun saveTask(taskId: Int?) {
        val title = binding.editTextTaskTitle.text.toString()
        val description = binding.editTextTaskDescription.text.toString()
        val module = binding.spinnerTaskModule.selectedItem.toString()
        val isDone = binding.checkBoxTaskDone.isChecked

        val task = Task(id = taskId ?: 0, title = title, description = description, module = module, isDone = isDone)
        if (taskId == null || taskId == 0) {
            viewModel.insertTask(task)
        } else {
            viewModel.updateTask(task)
        }
        findNavController().navigate(R.id.action_addEditTaskFragment_to_tasksFragment)
    }

    private fun loadModules() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.getStudentModules().execute()
            if (response.isSuccessful && response.body() != null) {
                val modules = response.body()!!.modules
                modulesList = modules
                withContext(Dispatchers.Main) {
                    val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, modules)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerTaskModule.adapter = adapter
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
