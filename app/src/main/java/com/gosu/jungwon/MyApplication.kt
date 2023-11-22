package com.gosu.jungwon

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.google.android.material.tabs.TabLayout.TabGravity
import java.lang.ref.WeakReference
import java.util.Date

//Make su
class MyApplication: Application(), ActivityLifecycleCallbacks, LifecycleObserver {

    lateinit var mAdView: AdView

    fun setAdView(adView: AdView) {
        mAdView = adView
    }

    fun loadBannerAd() {
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }
    private var appOpenAdManager : AppOpenAdMannager? = null
    private var currentActivity: Activity? = null

    companion object{
        private const val TAG = "MY_APPLICATION_TAG"
    }
    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(this)

        //initalize mobile ads sdk
        MobileAds.initialize(this){
            Log.d(TAG, "onCreate: onInitializationCompleted")
        }
        //Set your test devices

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("PLACE_TEST_DEVICE_ID", "PLACE_TEST_DEVICE_ID"))
                .build()
        )
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOpenAdManager = AppOpenAdMannager()

    }


    //Interface definition for a callback to be invoked when an app open ad is complete * (i.e. dismissed or fails to show).
    interface OnShowAdcompleteListener{
        fun onShowAdComplete()
    }

    //LifecycleObserver method that shows the app open ad when the app moves to foreground.
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onMoveToForeGround(){
        //Show the ad (if available) when the app moves to foreground
        appOpenAdManager!!.showAdIfAvailable(currentActivity!!)

    }

    /*Activity lifecycleCallback methods*/
    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        Log.d(TAG,"onActivityCreated: ")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.d(TAG,"onActivityStarted: ")
        /** An ad activity is started when an ad is showing, which could be AdActivity class from Google
         *  SDK or another activity class implemented by a third party mediation partener. Updating the
         *  currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
         *  on that shows the ad.
         */
        if (!appOpenAdManager!!.isShowingAd){
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(TAG,"onActivityResumed: ")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.d(TAG,"onActivityPaused: ")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.d(TAG,"onActivityStopped: ")
    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
        Log.d(TAG,"onActivitySaveInstanceState: ")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.d(TAG,"onActivityDestroyed: ")
    }

    /**
     * Show s an app open ad.
     * @param activity the activity that shows the app open ad
     * @param onShowAdCompleteListener the Listener to be notified when an app open ad is complete
     */
    fun showAdIfAvailable(activity: Activity,onShowAdCompleteListener: OnShowAdcompleteListener){
        //We wrap the showAdIfAvailable to enforce that other classes only interact with My Application
        appOpenAdManager!!.showAdIfAvailable(activity, onShowAdCompleteListener)
    }//now we will create splash activity and make it launcher activity

    //Inner class that loads and shows app open ads.
    private inner class AppOpenAdMannager {
        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false

        private var loadTime: Long = 0


        private fun loadAd(context: Context) {

            if (isLoadingAd || isAdAvailable()) {
                return
            }
            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                context.getString(R.string.app_open_ad_id_live),  // later my place live later
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    //Called when the App Open Ad failed to load
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        super.onAdFailedToLoad(adError)
                        Log.d(TAG, "onAdFailedToLoad: ${adError.message}")
                        Toast.makeText(
                            context,
                            "Failed to load ad due to ${adError.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    //Called when the App Open Ad is loaded
                    override fun onAdLoaded(ad: AppOpenAd) {
                        super.onAdLoaded(ad)
                        Log.d(TAG, "onAdLoaded: ")
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time

                        Toast.makeText(context, "Ad Loaded...", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }

        private fun wasLoadTimeLessTanNHoursAgo(numHours: Long): Boolean {
            val dateDifference = Date().time - loadTime
            val numMillisecondsPerHour: Long = 3600000
            return dateDifference < numMillisecondsPerHour * numHours
        }


        private fun isAdAvailable(): Boolean {


            return appOpenAd != null && wasLoadTimeLessTanNHoursAgo(4)
        }

        //Show the ad if one isn't already showing.
        //@param activity the activity that shows the app open ad
        fun showAdIfAvailable(activity: Activity) {
            showAdIfAvailable(activity,
                object : OnShowAdcompleteListener {
                    override fun onShowAdComplete() {

                        Log.d(TAG, "onShowAdComplete: ")
                    }
                })
        }

        /**
         * Show the ad if one isn't already showing.
         *@param activity the activity that shows the app open ad
         *@param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */

        fun showAdIfAvailable(
            activity: Activity,
            onShowAdcCompleteListener: OnShowAdcompleteListener
        ){
            if (isShowingAd){
                Log.d(TAG, "showAdIfAvailable: The app open ad is already showing...")
                return
            }
            //if the app open ad is not available yet, invoke the callback then load the ad
            if (!isAdAvailable()){
                Log.d(TAG, "showAdIfAvailable: The app open ad is not")//logd하고 Tab하면 자동작성
                onShowAdcCompleteListener.onShowAdComplete()
                loadAd(activity)
                return
            }

            appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdClicked() {
                    super.onAdClicked()
                    /*Called when App Open Ad is clicked*/
                    Log.d(TAG, "onAdClicked: ")
            }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    /*Called when App Open Ad is dismissed*/
                    Log.d(TAG, "onAdDismissedFullScreenContent: ")
                    //set the reference to null so isAdAvoilable() returns false
                    appOpenAd = null
                    isShowingAd = false
                    onShowAdcCompleteListener.onShowAdComplete()
                    loadAd(activity)

                }


                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    /*Called when App Open Ad is failed to show*/
                    Log.d(TAG, "onAdFailedToShowFullScreenContent: ${adError.message}")
                    appOpenAd = null
                    isShowingAd = false
                    onShowAdcCompleteListener.onShowAdComplete()
                    loadAd(activity)
                }
                override fun onAdImpression() {
                    super.onAdImpression()
                    /*Called when App Open Ad is recorded*/
                    Log.d(TAG, "onAdImpression: ")
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                    /*Called when App Open Ad is shown*/
                    Log.d(TAG, "onAdShowedFullScreenContent: ")
                }

        }
            isShowingAd = true
            appOpenAd!!.show(activity)
        }
    }

}