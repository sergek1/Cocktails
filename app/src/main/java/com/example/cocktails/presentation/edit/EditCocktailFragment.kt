package com.example.cocktails.presentation.edit

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cocktails.R
import com.example.cocktails.databinding.DialogAddIngredientBinding
import com.example.cocktails.databinding.FragmentEditBinding
import com.example.cocktails.presentation.edit.viewmodel.EditCocktailViewModel
import com.example.domain.model.Cocktail
import com.example.domain.model.Result
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditCocktailFragment : Fragment() {
    private var pickedPhoto: Uri? = null
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<EditCocktailViewModel>()
    private var idFromArgs: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idFromArgs = arguments?.getInt("id")
        viewModel.getCocktail(idFromArgs)
        observeViewModel()

        setCancelButton()
        setCardViewAddIngredient()
        pickPhoto()
        setDeleteButton()
        setSaveButton()
    }

    private fun setCancelButton() {
        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setCardViewAddIngredient() {
        binding.cvAddIngredient.setOnClickListener {
            showAddIngredientDialog {
                if (it != null) {
                    addChip(it)
                }
            }
        }
    }

    private fun setDeleteButton() {
        binding.btnDelete.setOnClickListener {
            if (idFromArgs == null) {
                findNavController().navigate(R.id.action_editCocktailFragment_to_myCocktailsFragment)
            } else {
                buildAlertDialog("Warning", "Are you sure you want to delete the cocktail?") {
                    deleteCocktail()
                    findNavController().navigate(R.id.action_editCocktailFragment_to_myCocktailsFragment)
                }
            }
        }
    }

    private fun setSaveButton() {
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text
            if (!title.isNullOrEmpty() && binding.chips.childCount > 0) {
                saveCocktail()
            }

            if (title?.trim()?.isNotEmpty() == true) {
                binding.tilTitle.error = null
            } else {
                binding.tilTitle.error = "Add title"
            }

            if (binding.chips.childCount == 0) {
                Snackbar.make(
                    binding.root,
                    resources.getString(R.string.error_no_chip_text),
                    Snackbar.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun pickPhoto() {
        binding.ivCocktail.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1
                )
            } else {
                ImagePicker.with(this).crop(1f, 1f)
                    .maxResultSize(1080, 1080)
                    .start(reqCode = 2)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImagePicker.with(this).crop(1f, 1f)
                    .maxResultSize(1080, 1080)
                    .start(reqCode = 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            pickedPhoto = data.data
            binding.ivCocktail.setImageURI(pickedPhoto)
        }
    }

    private fun addChip(it: String) {
        val chip = Chip(context).apply {
            text = it
        }
        chip.setOnCloseIconClickListener {
            binding.chips.removeView(chip)
        }
        binding.chips.addView(chip)
    }

    private fun showAddIngredientDialog(ingredient: (String?) -> Unit) {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val dialogBinding = DialogAddIngredientBinding.inflate(layoutInflater)

        with(builder) {
            setView(dialogBinding.root)
            setCancelable(false)
            dialogBinding.etIngredient.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s?.trim()?.isNotEmpty() == true) {
                        dialogBinding.tilIngredient.error = null
                    } else {
                        dialogBinding.tilIngredient.error = "Add ingredient"
                    }
                }
            })

            dialogBinding.btnAdd.setOnClickListener {
                if (dialogBinding.etIngredient.text?.trim()?.isNotEmpty() == true) {
                    ingredient(dialogBinding.etIngredient.text.toString())
                    builder.dismiss()
                }
            }

            dialogBinding.btnCancel.setOnClickListener {
                ingredient(null)
                builder.dismiss()
            }
            show()
        }
    }

    private fun saveCocktail() {
        val name = binding.etTitle.text.toString()

        val description = if (binding.etDescription.text == null) null
        else binding.etDescription.text.toString()

        val recipe = if (binding.etRecipe.text == null) null
        else binding.etRecipe.text.toString()

        val ingredients =
            if (binding.chips.childCount != 0) binding.chips.children.map { (it as Chip).text.toString() }
                .toList()
            else listOf()
        val image = if (pickedPhoto != null) pickedPhoto.toString() else null
        val cocktail = Cocktail(
            id = idFromArgs,
            name = name,
            description = description,
            recipe = recipe,
            image = image,
            ingredients = ingredients
        )
        viewModel.saveCocktail(cocktail, idFromArgs == null)
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_editCocktailFragment_to_myCocktailsFragment)
        }, 500)

    }

    private fun deleteCocktail() {
        if (idFromArgs != null) {
            viewModel.deleteCocktail(idFromArgs)
        }
    }

    private fun buildAlertDialog(title: String, message: String, onPositiveClicked: () -> Unit) {
        val builder = AlertDialog.Builder(context, R.style.CustomAlertDialogSmallCorners)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Yes") { _, _ ->
            onPositiveClicked()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.show()
    }

    private fun observeViewModel() {
        viewModel.cocktail.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    it.data?.let { it1 -> successUI(it1) }
                }

                is Result.Loading -> {
                    loadingUI()
                }

                is Result.Failure -> {
                    it.message?.let { it1 -> failureUI(it1) }
                }
            }
        }
    }

    private fun loadingUI() {
        with(binding) {
            progressIndicator.visibility = View.VISIBLE
            layoutContent.visibility = View.GONE
            layoutError.visibility = View.GONE
        }
    }

    private fun failureUI(message: String) {
        with(binding) {
            progressIndicator.visibility = View.GONE
            layoutContent.visibility = View.GONE
            layoutError.visibility = View.VISIBLE
            val error = "Ошибка: $message"
            tvError.text = error
        }
    }

    private fun successUI(cocktail: Cocktail) {
        with(binding) {
            progressIndicator.visibility = View.GONE
            layoutError.visibility = View.GONE
            layoutContent.visibility = View.VISIBLE


            etTitle.setText(cocktail.name)
            etTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if (s?.trim()?.isNotEmpty() == true) {
                        tilTitle.error = null
                    } else {
                        tilTitle.error = "Add title"
                    }
                }
            })


            etDescription.setText(cocktail.description)
            etRecipe.setText(cocktail.recipe)
            if (cocktail.image != null) {
                pickedPhoto = Uri.parse(cocktail.image)
                ivCocktail.setImageURI(pickedPhoto)
            }
            setChips(cocktail.ingredients)
        }
    }

    private fun setChips(ingredients: List<String>) {
        if (binding.chips.isEmpty()) {
            if (ingredients.isNotEmpty()) {
                ingredients.filter { it != "" }.forEach {
                    val chip = Chip(context).apply {
                        text = it.trim()
                    }
                    chip.setOnCloseIconClickListener {
                        binding.chips.removeView(chip)
                    }
                    binding.chips.addView(chip)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}