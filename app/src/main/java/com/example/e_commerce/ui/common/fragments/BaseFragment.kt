package com.example.e_commerce.ui.common.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.example.e_commerce.ui.common.views.ProgressDialog
import dagger.hilt.android.AndroidEntryPoint


abstract class BaseFragment<DB: ViewDataBinding, VM: ViewModel> : Fragment() {

    val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }
    protected abstract val viewModel: VM
    protected var _binding: DB? = null
    protected val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        return binding.root

    }

    @LayoutRes abstract fun getLayoutRes(): Int
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doDataBinding()
        init()
    }
    abstract fun init()

    private fun doDataBinding() {
        // it is extra if you want to set life cycle owner in binding
        binding.lifecycleOwner = viewLifecycleOwner
        // Here your viewModel and binding variable imlementation
        binding.setVariable(
            BR.viewmodel, viewModel
        )  // In all layout the variable name should be "viewModel"
        binding.executePendingBindings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}