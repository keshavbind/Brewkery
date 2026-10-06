package com.example.brewkery.ui.detail

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.brewkery.R
import com.example.brewkery.data.api.RetrofitInstance
import com.example.brewkery.data.cart.CartItem
import com.example.brewkery.data.cart.CartManager
import com.example.brewkery.data.model.Product
import com.example.brewkery.databinding.ActivityProductDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val productId = intent.getIntExtra("PRODUCT_ID", -1)

        if (productId == -1) {
            finish()
            return
        }

        loadProduct(productId)
    }

    private fun loadProduct(productId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val product = RetrofitInstance.api.getItem(productId)

                withContext(Dispatchers.Main) {
                    displayProduct(product)
                }

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {
                    binding.tvDescription.text = "Unable to load product."
                }
            }
        }
    }

    private fun displayProduct(product: Product) {

        binding.tvProductName.text = product.name
        binding.tvTagline.text = product.tagline

        binding.tvRating.text =
            "★ ${product.rating} (${product.reviewCount} reviews)"

        binding.tvPrice.text =
            "₹${String.format("%.2f", product.basePrice)}"

        binding.tvDescription.text =
            product.description

        binding.tvIngredients.text =
            "Ingredients: ${product.ingredients.joinToString(", ")}"

        Glide.with(this)
            .load(product.imageUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.ivProduct)

        setupSizes(product)
        setupMilk(product)
        setupSugar(product)

        setupAddToCart(product)
    }

    private fun setupSizes(product: Product) {

        binding.rgSize.removeAllViews()

        product.customizations.sizes.forEachIndexed { index, size ->

            val radioButton = RadioButton(this)

            radioButton.text =
                "${size.label} (+₹${String.format("%.2f", size.extraPrice)})"

            radioButton.tag = size

            binding.rgSize.addView(radioButton)

            if (index == 0) {
                radioButton.isChecked = true
            }
        }
    }

    private fun setupMilk(product: Product) {

        val milkNames = product.customizations.milkOptions.map {
            "${it.name} (+₹${String.format("%.2f", it.extraPrice)})"
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            milkNames
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerMilk.adapter = adapter
    }

    private fun setupSugar(product: Product) {

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            product.customizations.sugarLevels
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerSugar.adapter = adapter
    }

    private fun setupAddToCart(product: Product) {

        binding.btnAddToCart.setOnClickListener {

            // Selected size
            val checkedRadioButtonId =
                binding.rgSize.checkedRadioButtonId

            if (checkedRadioButtonId == -1) {
                Toast.makeText(
                    this,
                    "Please select a size",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val selectedRadioButton =
                binding.rgSize.findViewById<RadioButton>(
                    checkedRadioButtonId
                )

            val selectedSize =
                selectedRadioButton.tag
                        as com.example.brewkery.data.model.SizeOption

            // Selected milk
            val selectedMilk =
                product.customizations.milkOptions[
                    binding.spinnerMilk.selectedItemPosition
                ]

            // Selected sugar
            val selectedSugar =
                product.customizations.sugarLevels[
                    binding.spinnerSugar.selectedItemPosition
                ]

            // Calculate final price
            val finalPrice =
                product.basePrice +
                        selectedSize.extraPrice +
                        selectedMilk.extraPrice

            // Create cart item
            val cartItem = CartItem(
                product = product,
                selectedSize = selectedSize.label,
                selectedMilk = selectedMilk.name,
                selectedSugar = selectedSugar,
                quantity = 1,
                finalPrice = finalPrice
            )

            // Add to cart
            CartManager.addItem(cartItem)

            Toast.makeText(
                this,
                "${product.name} added to cart",
                Toast.LENGTH_SHORT
            ).show()

            val intent = android.content.Intent(
                this,
                com.example.brewkery.ui.cart.CartActivity::class.java
            )

            startActivity(intent)
        }
    }
}