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
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.databinding.FragmentAddEditTaskBinding
import dam.islasfilipinas.rubinlavi_roy_practicapmdm_persistencia.room.Task

class AddEditTaskFragment : Fragment() {

    private var _binding: FragmentAddEditTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modules = arrayOf("Acceso a Datos", "Programación Multimedia y Dispositivos Móviles", "Desarrollo de Interfaces", "Sistemas de Gestión Empresarial")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, modules)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTaskModule.adapter = adapter

        val taskId = arguments?.getInt("taskId")
        if (taskId != null && taskId != 0) {
            viewModel.getTaskById(taskId).observe(viewLifecycleOwner) { task ->
                binding.editTextTaskTitle.setText(task.title)
                binding.editTextTaskDescription.setText(task.description)
                val modulePosition = modules.indexOf(task.module)
                binding.spinnerTaskModule.setSelection(modulePosition)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
