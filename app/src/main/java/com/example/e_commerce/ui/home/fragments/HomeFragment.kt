package com.example.e_commerce.ui.home.fragments

import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R
import com.example.e_commerce.data.models.Resource
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.ui.common.fragments.BaseFragment
import com.example.e_commerce.ui.home.adapter.CategoriesAdapter
import com.example.e_commerce.ui.home.adapter.SalesAdAdapter
import com.example.e_commerce.ui.home.model.CategoryUIModel
import com.example.e_commerce.ui.home.model.SalesUiAdModel
import com.example.e_commerce.ui.home.viewmodel.HomeViewModel
import com.example.e_commerce.ui.products.adapter.ProductAdapter
import com.example.e_commerce.utils.DepthPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
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
    private fun initViews() {
        initCategories()
        initRecycler()
    }

    private fun initRecycler() {

        lifecycleScope.launch {
            viewModel.flashSaleState.collect{
                Log.d("HomeFragment", "initViewModel: $it")
                val flashSaleAdapter = ProductAdapter()
                flashSaleAdapter.submitList(it)
                binding.flashSaleProductsRv.apply {
                    adapter = flashSaleAdapter
                    layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                    setHasFixedSize(true)

                }
            }
        }

    }
    private fun initViewModel() {
        lifecycleScope.launch {
           // viewModel.getSaleProduct()
        }
        lifecycleScope.launch {
            viewModel.salesAdsStateTamp.collect{ source ->
                when(source){
                    is Resource.Success -> {
                        initSalesAdsView(source.data)
                        binding.saleAdsShimmerView.root.stopShimmer()
                        binding.saleAdsShimmerView.root.visibility = View.GONE
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






    private fun initCategories() {
        lifecycleScope.launch {
            viewModel.categoryState.collect { source ->
                when(source){
                    is Resource.Success -> {
                        initCategoryRecycler(source)
                        Log.d("HomeFragment", "initCategories: success")

                    }
                    is Resource.Error -> {
                        Log.d("HomeFragment", "initCategories: ${source.exception?.message}")
                    }
                    is Resource.Loading -> {
                        Log.d("HomeFragment", "initCategories: Loading")
                    }
                }
            }
        }
    }


    private fun initCategoryRecycler(source: Resource<List<CategoryUIModel>>) {
        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.categoriesRecyclerView.setHasFixedSize(true)

        // Check if data is available
        source.data?.let { data ->
            val adapter = CategoriesAdapter(data)
            binding.categoriesRecyclerView.adapter = adapter
        }
    }


    private fun initSalesAdsView(salesAds: List<SalesUiAdModel>?) {


        setupDots(salesAds!!.size)
        updateDots(0)
        val adapter = SalesAdAdapter(lifecycleScope,salesAds)
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

        var direction = 1  // 1 for forward, -1 for backward

        autoScrollJob = CoroutineScope(IO).launch {
            while (isActive) {
                delay(5000)
                withContext(Main) {
                    binding.saleAdsViewPager.currentItem += direction
                    if (binding.saleAdsViewPager.currentItem == totalPages - 1 || binding.saleAdsViewPager.currentItem == 0) {
                        direction *= -1
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