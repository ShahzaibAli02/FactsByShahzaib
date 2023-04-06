package com.facts.factsbyshahzaib

import android.app.Activity
import android.view.View
import android.widget.Toast
import com.facts.factsbyshahzaib.databinding.NativeAdLytBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView


class AdsManager(val activity: Activity)

{



    val ADMOB_NATIVE_AD_UNIT_ID = "ca-app-pub-3940256099942544/2247696110"
    val ADMOB_INTERSTITIAL_AD_ID = "ca-app-pub-3940256099942544/1033173712"
    var currentNativeAd: NativeAd? = null
    var nativeAdView: NativeAdView? = null
    lateinit var nativeAdListener : (Boolean, NativeAdView?) -> Unit
    private var mInterstitialAd: InterstitialAd? = null


    /**
     * INTERSTITIAL AD CODE
     */
    fun loadInterstitialAd()
    {
        val adRequest: AdRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity, ADMOB_INTERSTITIAL_AD_ID, adRequest, object : InterstitialAdLoadCallback()
        {
            override fun onAdLoaded(interstitialAd: InterstitialAd)
            { // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd

            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError)
            { // Handle the error
                mInterstitialAd = null
            }
        })
    }
    fun showInterstitialAd()
    {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback= object: FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    mInterstitialAd = null
                    loadInterstitialAd()
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError)
                {
                    mInterstitialAd = null
                    loadInterstitialAd()
                }


                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                }
            }
            mInterstitialAd?.show(activity)
        } else {
           Toast.makeText(activity,"The interstitial ad wasn't ready yet.",Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * NATIVE AD CODE
     * ADMOB
     */
    private fun populateNativeAdView(nativeAd: NativeAd, unifiedAdBinding: NativeAdLytBinding) {
        val nativeAdView = unifiedAdBinding.root

        // Set the media view.
        nativeAdView.mediaView = unifiedAdBinding.adMedia

        // Set other ad assets.
        nativeAdView.headlineView = unifiedAdBinding.adHeadline
        nativeAdView.bodyView = unifiedAdBinding.adBody
        nativeAdView.callToActionView = unifiedAdBinding.adCallToAction
        nativeAdView.iconView = unifiedAdBinding.adAppIcon
        nativeAdView.priceView = unifiedAdBinding.adPrice
        nativeAdView.starRatingView = unifiedAdBinding.adStars
        nativeAdView.storeView = unifiedAdBinding.adStore
        nativeAdView.advertiserView = unifiedAdBinding.adAdvertiser

        // The headline and media content are guaranteed to be in every UnifiedNativeAd.
        unifiedAdBinding.adHeadline.text = nativeAd.headline
        nativeAd.mediaContent?.let { unifiedAdBinding.adMedia.setMediaContent(it) }

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            unifiedAdBinding.adBody.visibility = View.INVISIBLE
        } else {
            unifiedAdBinding.adBody.visibility = View.VISIBLE
            unifiedAdBinding.adBody.text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            unifiedAdBinding.adCallToAction.visibility = View.INVISIBLE
        } else {
            unifiedAdBinding.adCallToAction.visibility = View.VISIBLE
            unifiedAdBinding.adCallToAction.text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            unifiedAdBinding.adAppIcon.visibility = View.GONE
        } else {
            unifiedAdBinding.adAppIcon.setImageDrawable(nativeAd.icon?.drawable)
            unifiedAdBinding.adAppIcon.visibility = View.VISIBLE
        }

        if (nativeAd.price == null) {
            unifiedAdBinding.adPrice.visibility = View.INVISIBLE
        } else {
            unifiedAdBinding.adPrice.visibility = View.VISIBLE
            unifiedAdBinding.adPrice.text = nativeAd.price
        }

        if (nativeAd.store == null) {
            unifiedAdBinding.adStore.visibility = View.INVISIBLE
        } else {
            unifiedAdBinding.adStore.visibility = View.VISIBLE
            unifiedAdBinding.adStore.text = nativeAd.store
        }

        if (nativeAd.starRating == null) {
            unifiedAdBinding.adStars.visibility = View.INVISIBLE
        } else {
            unifiedAdBinding.adStars.rating = nativeAd.starRating!!.toFloat()
            unifiedAdBinding.adStars.visibility = View.VISIBLE
        }

        if (nativeAd.advertiser == null) {
            unifiedAdBinding.adAdvertiser.visibility = View.INVISIBLE
        } else {
            unifiedAdBinding.adAdvertiser.text = nativeAd.advertiser
            unifiedAdBinding.adAdvertiser.visibility = View.VISIBLE
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        nativeAdView.setNativeAd(nativeAd)
    }


    /**
     * Creates a request for a new native ad based on the boolean parameters and calls the
     * corresponding "populate" method when one is successfully returned.
     */
     fun loadNativeAd()
    {

        val builder = AdLoader.Builder(activity, ADMOB_NATIVE_AD_UNIT_ID)
        builder.forNativeAd { nativeAd ->
            // OnUnifiedNativeAdLoadedListener implementation.
            // If this callback occurs after the activity is destroyed, you must call
            // destroy and return or you may get a memory leak.
            var activityDestroyed = false
            activityDestroyed = activity.isDestroyed
            if (activityDestroyed || activity.isFinishing || activity.isChangingConfigurations) {
                nativeAd.destroy()
                return@forNativeAd
            }
            // You must call destroy on old ads when you are done with them,
            // otherwise you will have a memory leak.
            currentNativeAd?.destroy()
            currentNativeAd = nativeAd
            val unifiedAdBinding = NativeAdLytBinding.inflate(activity.layoutInflater)
            populateNativeAdView(nativeAd, unifiedAdBinding)

            nativeAdView=unifiedAdBinding.root
            nativeAdListener(true,nativeAdView)
//            mainActivityBinding.adFrame.removeAllViews()
//            mainActivityBinding.adFrame.addView(unifiedAdBinding.root)


        }

        val videoOptions = VideoOptions.Builder().setStartMuted(true).build()

        val adOptions = NativeAdOptions.Builder().setVideoOptions(videoOptions).build()

        builder.withNativeAdOptions(adOptions)

        val adLoader = builder.withAdListener(object : AdListener()
        {
            override fun onAdFailedToLoad(loadAdError: LoadAdError)
            {
                nativeAdListener(false,null)
                Toast.makeText(activity, "Failed to load native ad with error", Toast.LENGTH_SHORT).show()
            }
        }).build()

        adLoader.loadAd(AdRequest.Builder().build())

    }

//    override fun onDestroy() {
//        currentNativeAd?.destroy()
//        super.onDestroy()
//    }
}