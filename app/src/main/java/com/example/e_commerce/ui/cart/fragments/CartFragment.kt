package com.example.e_commerce.ui.cart.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.e_commerce.R


class CartFragment : Fragment() {

    val TAG = "CartFragment"
    private var dataIsInitialized = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (isVisible && !dataIsInitialized) { // this for init data only one time
            dataIsInitialized = true
            Log.d("InitFragment", "CartFragment")
        }
    }

}