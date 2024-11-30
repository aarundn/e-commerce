package com.example.e_commerce.ui.products.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentProductDetailsBinding
import com.example.e_commerce.ui.common.fragments.BaseFragment
import com.example.e_commerce.ui.common.views.setIndicator
import com.example.e_commerce.ui.common.views.updateIndicator
import com.example.e_commerce.ui.products.adapter.ProductImagesAdAdapter
import com.example.e_commerce.ui.products.models.ProductUIModel
import com.example.e_commerce.ui.products.viewmodel.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : BaseFragment<FragmentProductDetailsBinding, ProductDetailsViewModel>() {


    override val viewModel: ProductDetailsViewModel by activityViewModels()

    override fun getLayoutRes(): Int = R.layout.fragment_product_details

    override fun init() {
        initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.productDetailsState.collectLatest {
                initView(it)
            }
        }

    }

    private fun initView(uiModel: ProductUIModel) {
        binding.titleTv.text = uiModel.name
        initImagesView(uiModel.images)
    }

    private fun initImagesView(images: List<String>?) {
        setIndicator(images!!.size,requireContext(),binding.indicatorView)
        updateIndicator(1,binding.indicatorView,requireContext())
        binding.productImagesViewPager.apply {
            adapter = ProductImagesAdAdapter(images)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updateIndicator(position,binding.indicatorView,requireContext())
                }
            })
        }
    }

    companion object {

    }
}