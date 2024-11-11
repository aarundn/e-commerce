package com.example.e_commerce.ui.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.ui.home.adapter.SalesAdAdapter
import com.example.e_commerce.ui.home.model.SalesUiAdModel
import com.example.e_commerce.utils.DepthPageTransformer


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
               endAt =  System.currentTimeMillis() + 3600000
            ),
            SalesUiAdModel(
                title = "Super Flash Sale\n" +
                        "50% Off",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-commerce-b134b.appspot.com/o/promo_image.png?alt=media&token=f5709168-3afe-4335-be55-ed25ae8afe91",
                endAt =  System.currentTimeMillis() + 3600000
            ),
            SalesUiAdModel(
                title = "Super Flash Sale\n" +
                        "50% Off",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-commerce-b134b.appspot.com/o/promo_image.png?alt=media&token=f5709168-3afe-4335-be55-ed25ae8afe91",
                endAt =  System.currentTimeMillis() + 3600000
            ),
            SalesUiAdModel(
                title = "Super Flash Sale\n" +
                        "50% Off",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/e-commerce-b134b.appspot.com/o/promo_image.png?alt=media&token=f5709168-3afe-4335-be55-ed25ae8afe91",
                endAt =  System.currentTimeMillis() + 3600000
            )
        )
        setupDots(salesAds.size)
        updateDots(0)
        val adapter = SalesAdAdapter(salesAds)
        binding.saleAdsViewPager.adapter = adapter
        binding.saleAdsViewPager.setPageTransformer(DepthPageTransformer())

        binding.saleAdsViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })
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
    companion object {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}