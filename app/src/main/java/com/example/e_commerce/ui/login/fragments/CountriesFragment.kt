package com.example.e_commerce.ui.login.fragments


import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentCountriesBinding
import com.example.e_commerce.ui.common.fragments.BaseBottomSheetFragment
import com.example.e_commerce.ui.login.adapter.CountriesAdapter
import com.example.e_commerce.ui.login.adapter.OnCountryClickListener
import com.example.e_commerce.ui.login.models.CountryUiModel
import com.example.e_commerce.ui.login.viewmodel.CountriesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CountriesFragment : BaseBottomSheetFragment<FragmentCountriesBinding, CountriesViewModel>(), OnCountryClickListener {

    override val viewModel: CountriesViewModel by viewModels()

    override fun getLayoutRes(): Int  = R.layout.fragment_countries

    override fun init() {
        initViewModel()
    }

    private fun initViewModel() {
        lifecycleScope.launch {
            viewModel.countriesUIModelState.collectLatest {
                if(it.isEmpty()) return@collectLatest
                binding.progressBar.visibility = View.GONE
                binding.countriesLayout.visibility = View.VISIBLE

                val countriesAdapter = CountriesAdapter(it, this@CountriesFragment)
                binding.countriesRv.apply {
                    adapter = countriesAdapter
                    layoutManager = LinearLayoutManager(context)
                }
            }
        }
    }


    override fun onCountryClicked(country: CountryUiModel) {
        viewModel.saveCountry(country)
        dismiss()
    }

}