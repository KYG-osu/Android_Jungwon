package com.gosu.jungwon

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
//AdMobandrelatedimportsfordisplayingads.
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.AdRequest
// If you use any resource (like layout or string), you need this.
// If not used anywhere in your code, it can be removed.
// import com.gosu.jungwon.R

// WebView and related imports for web content display and control.
import android.webkit.WebView
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import android.webkit.WebViewClient
import android.widget.Toast
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.RequestConfiguration

const val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

// MainActivity 클래스를 정의합니다. AppCompatActivity를 상속받습니다.
class MainActivity : AppCompatActivity() {

    private final var TAG = "MainActivity"//로그태그를위한문자열선언
    lateinit var mAdView:AdView//배너광고변수선언(lateinit으로초기화는나중에)
    //private lateinit var inAppUpdate:InAppUpdate
    //onCreate메서드는액티비티가생성될때호출(초기화작업수행,사용자인터페이스초기화)
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)//부모클래스의onCreate메서드를호출합니다.
        setContentView(R.layout.activity_main)//activity_main.xml레이아웃을화면에표시합니다.
        //inAppUpdate=InAppUpdate(this)

        showBannerAD()
//URL문자열을정의합니다.앱들어가자마자새페이지로웹을여는방식valurl="https://silverangel.kr/silverangel/visit/visitLoginPage.do?f_code=jungwon"
        //WebView객체를찾아옵니다.
        val webView=findViewById<WebView>(R.id.webView)
        val webSettings=webView.settings
        webSettings.javaScriptEnabled=true//자바스크립트활성화(버튼클릭활성화)
        webSettings.domStorageEnabled=true//DOMStorage활성화(알림창활성화)
        webView.webViewClient=object:WebViewClient(){
        }
        //WebView설정및URL로드(배너)
        webView.webChromeClient=object:WebChromeClient(){
            override fun onJsAlert(view:WebView?,url:String?,message:String?,result:JsResult?):Boolean{
                return super.onJsAlert(view,url,message,result)
            }
        }
        webView.loadUrl("https://silverangel.kr/silverangel/visit/visitLoginPage.do?f_code=jungwon")
    }

    private fun showBannerAD(){
        MobileAds.initialize(this){}//앱이시작될때MobileAds초기화 https://developers.google.com/admob/android/quick-start?hl=ko#kotlin
        //테스트시 어뷰징으로 계정 정지 방지 장치
        val testDeviceIds = listOf("My device ID1", "My Device ID2")
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(testDeviceIds)
                .build()
        )
        val adRequest=AdRequest.Builder().build()//광고요청객체생성.광고를가져오는데사용
        mAdView=findViewById(R.id.adView)

        mAdView.loadAd(adRequest)
        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded(){
                Toast.makeText(applicationContext, "보호자님 환영합니다.", Toast.LENGTH_SHORT).show()
            }
            override fun onAdFailedToLoad(p0: LoadAdError) {
                Toast.makeText(applicationContext, "Banner Ad loading failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        mAdView.pause()
        super.onPause()
    }
    override fun onResume() {
        super.onResume()
        mAdView.resume()
    }
    override fun onDestroy() {
        mAdView.destroy()
        super.onDestroy()

    }
    /** Override the default implementation when the user presses the back key. */
    override fun onBackPressed() {

        // Move the task containing the MainActivity to the back of the activity stack, instead of
        // destroying it. Therefore, MainActivity will be shown when the user switches back to the app.

        moveTaskToBack(true)
    }
}

//private fun AppUpdateManager.startUpdateFlowForResult(appUpdateInfo: AppUpdateInfo?, myRequestCode: Any, mainActivity: MainActivity, build: AppUpdateOptions) {}
// Checks that the update is not stalled during 'onResume()'.(사용자와 상호작용 시작할때 호출)
    // However, you should execute this check at all entry points into the app.(앱이 활성화 될때 마다 업데이트 확인)
