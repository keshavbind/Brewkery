package com.example.brewkery

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.brewkery.databinding.ActivityMainBinding
import com.example.brewkery.viewmodel.BrewkeryViewModel
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.brewkery.ui.detail.ProductDetailActivity
import android.content.Intent
import com.example.brewkery.ui.cart.CartActivity
import com.example.brewkery.ui.home.ProductAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: BrewkeryViewModel by viewModels()

    private val productAdapter = ProductAdapter { product ->

        val intent = Intent(
            this,
            ProductDetailActivity::class.java
        )

        intent.putExtra("PRODUCT_ID", product.id)

        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCart.setOnClickListener {
            startActivity(
                Intent(this, CartActivity::class.java)
            )
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->

            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                view.paddingLeft,
                systemBars.top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        setupRecyclerView()
        observeViewModel()

        viewModel.loadMenu()
    }

    private fun setupRecyclerView() {

        binding.recyclerViewProducts.layoutManager =
            LinearLayoutManager(this)

        binding.recyclerViewProducts.adapter = productAdapter
    }

    private fun observeViewModel() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.menu.collect { menu ->

                        if (menu != null) {
                            productAdapter.submitList(menu.items)
                        }
                    }
                }

                launch {
                    viewModel.isLoading.collect { isLoading ->

                        binding.progressBar.visibility =
                            if (isLoading) View.VISIBLE else View.GONE
                    }
                }

                launch {
                    viewModel.error.collect { error ->

                        if (error != null) {
                            binding.tvError.visibility = View.VISIBLE
                            binding.tvError.text = error
                        } else {
                            binding.tvError.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }
}