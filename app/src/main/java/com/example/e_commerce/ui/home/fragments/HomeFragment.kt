package com.example.e_commerce.ui.home.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.ui.common.fragments.BaseFragment
import com.example.e_commerce.ui.home.adapter.SalesAdAdapter
import com.example.e_commerce.ui.home.model.SalesUiAdModel
import com.example.e_commerce.ui.home.viewmodel.HomeViewModel
import com.example.e_commerce.ui.login.viewmodel.LoginViewModel
import com.example.e_commerce.utils.DepthPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel> (){

    override val viewModel: HomeViewModel by viewModels()
    override fun getLayoutRes(): Int = R.layout.fragment_home



    override fun init() {
        initViews()
        initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.salesAdsStateTamp.collect{ source ->
                when(source){
                    is Resource.Success -> {
                        Log.d("HomeFragment", "initViewModel: ${source.data}")
                    }
                    is Resource.Error -> {
                        Log.d("HomeFragment", "initViewModel: ${source.exception?.message}")
                    }
                    is Resource.Loading -> {
                        Log.d("HomeFragment", "initViewModel: Loading")
                    }
                }
            }

        }
    }

    private fun initViews() {
        initSalesAdsView()
    }

    private fun initSalesAdsView() {
        val salesAds = listOf(
            SalesUiAdModel(
                title = "Super Flash Sale\n" +
                        "50% Off",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-commerce-b134b.appspot.com/o/promo_image.png?alt=media&token=f5709168-3afe-4335-be55-ed25ae8afe91",
                endAt = System.currentTimeMillis() + 7200000
            ),
            SalesUiAdModel(
                title = "Super Flash Sale\n" +
                        "50% Off",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-commerce-b134b.appspot.com/o/promo_image.png?alt=media&token=f5709168-3afe-4335-be55-ed25ae8afe91",
                endAt = System.currentTimeMillis() + 3600000
            ),
            SalesUiAdModel(
                title = "Super Flash Sale\n" +
                        "50% Off",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-commerce-b134b.appspot.com/o/promo_image.png?alt=media&token=f5709168-3afe-4335-be55-ed25ae8afe91",
                endAt = System.currentTimeMillis() + 3600000
            ),
            SalesUiAdModel(
                title = "Super Flash Sale\n" +
                        "50% Off",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-commerce-b134b.appspot.com/o/promo_image.png?alt=media&token=f5709168-3afe-4335-be55-ed25ae8afe91",
                endAt = System.currentTimeMillis() + 3600000
            ),
            SalesUiAdModel(
                title = "Super Flash Sale\n" +
                        "50% Off",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-commerce-b134b.appspot.com/o/promo_image.png?alt=media&token=f5709168-3afe-4335-be55-ed25ae8afe91",
                endAt = System.currentTimeMillis() + 3600000
            )
        )
        setupDots(salesAds.size)
        updateDots(0)
        val adapter = SalesAdAdapter(salesAds)
        binding.saleAdsViewPager.adapter = adapter
        binding.saleAdsViewPager.setPageTransformer(DepthPageTransformer())


        binding.saleAdsViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })


        autoScroll(salesAds.size)
    }


    private fun setupDots(count: Int) {
        for (i in 0 until count) {
            val dot = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(20, 20).apply {
                    marginStart = 8
                    marginEnd = 8
                }
                background = ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.dot_unselected
                )
            }
            binding.indicatorView.addView(dot)
        }
    }

    private fun updateDots(position: Int) {
        for (i in 0 until binding.indicatorView.childCount) {
            val dot = binding.indicatorView.getChildAt(i)
            dot.background = if (i == position) {
                ContextCompat.getDrawable(requireActivity(), R.drawable.dot_selected)
            } else {
                ContextCompat.getDrawable(requireContext(), R.drawable.dot_unselected)
            }
        }
    }

    private var autoScrollJob: Job? = null
    private fun autoScroll(totalPages: Int) {
        autoScrollJob = CoroutineScope(IO).launch {
            while (isActive) {
                delay(5000)
                withContext(Main){
                    if (binding.saleAdsViewPager.currentItem + 1 > totalPages - 1) {
                        binding.saleAdsViewPager.currentItem = 0
                    } else {
                        binding.saleAdsViewPager.currentItem++
                    }
                }

            }
        }
    }

    companion object {

    }

    override fun onDestroy() {
        super.onDestroy()
        autoScrollJob?.cancel()
        _binding = null
    }
}