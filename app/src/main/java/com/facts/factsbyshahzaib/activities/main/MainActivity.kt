package com.facts.factsbyshahzaib.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.facts.factsbyshahzaib.AdsManager
import com.facts.factsbyshahzaib.R
import com.facts.factsbyshahzaib.adapters.FactAdapter
import com.facts.factsbyshahzaib.databinding.ActivityMainBinding
import com.facts.factsbyshahzaib.model.FactsResponse
import com.facts.factsbyshahzaib.model.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity()
{

    private lateinit var binding: ActivityMainBinding
    val viewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }
    val factAdapter by lazy { FactAdapter() }
    val adsManager by lazy { AdsManager(this) }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvFacts.adapter=factAdapter

        initObservers()
        loadNativeAd()
        loadInterstitialAd()

        if(viewModel.response.value is Resource.None)
            loadFacts()


        binding.btnReload.setOnClickListener { loadFacts() }
        binding.btnShowAd.setOnClickListener { adsManager.showInterstitialAd() }
    }

    private fun loadInterstitialAd()
    {
        adsManager.loadInterstitialAd()
    }

    private fun loadNativeAd()
    {
        adsManager.nativeAdListener={isLoaded,nativeAd ->

            nativeAd?.let {
                binding.adFrame.removeAllViews()
                binding.adFrame.addView(it)
            }

        }
        adsManager.loadNativeAd()
    }

    private fun initObservers()
    {
        viewModel.response.observe(this,::onData)
    }


    /**
     * Method to load facts from Api
     */
    fun loadFacts()
    {

        //If Not Already Loading
        if (viewModel.response.value !is Resource.Loading)
        {
            viewModel.loadFacts()
        }

    }

    /**
     * Method runs when data loads or changes
     */
    private fun onData(it: Resource<FactsResponse>)
    {
        resetLayouts()
        when(it)
        {
            is Resource.Error -> {
                showError(it.message)
            }
            is Resource.Loading -> {
                showLoading()
            }
            is Resource.Success -> {
                binding.rvFacts.visibility=View.VISIBLE
                it.data?.let {
                    factAdapter.setList(it)
                }
            }
            is Resource.None ->{}
        }
    }

    private fun showError(message: String?)
    {
        val errorMsg=message?:"Error"
        binding.lytError.apply {
            parent.visibility=View.VISIBLE
            txtMessage.text=errorMsg
            btnRetry.setOnClickListener {
                loadFacts()
            }
        }

    }


    /**
     * Method to hide all layouts Progress,Error,RecyclerView
     */
    private fun resetLayouts()
    {

        binding.apply {
            lytLoading.visibility= View.GONE
            lytError.parent.visibility= View.GONE
            rvFacts.visibility= View.GONE
        }

    }

    override fun onBackPressed()
    {
        //super.onBackPressed()
        showConfirmDialog()
    }

    private fun showConfirmDialog()
    {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.exit))
            .setMessage(getString(R.string.exit_message))
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                finish()
            }
            .setNegativeButton(getString(R.string.cancel),null)
            .create()

        dialog.show()
    }

    private fun showLoading()
    {

        binding.lytLoading.visibility= View.VISIBLE

        Glide
            .with(this)
            .load(R.drawable.loading)
            .into(binding.imgLoading)

    }

    override fun onDestroy()
    {
        super.onDestroy()
        adsManager.currentNativeAd?.destroy()
    }
}