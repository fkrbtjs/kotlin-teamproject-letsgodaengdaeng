package kr.or.mrhi.letsgodaengdaeng.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kr.or.mrhi.letsgodaengdaeng.databinding.ActivityAddressBinding

class AddressActivity : AppCompatActivity() {
    lateinit var activityAddressApiBinding : ActivityAddressBinding

    /** 주소 검색이 완료되면 data로 받아 인텐트로 넘겨줌 */
    inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun processDATA(data: String?) {
            SignupUserActivity.address = data
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** 파이어 베이스 Hostiong 서비스에 등록한 html 주소 */
        val blogspot = "https://letsgodaengdaeng.web.app/"

        activityAddressApiBinding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(activityAddressApiBinding.root)
        activityAddressApiBinding.webView!!.settings.javaScriptEnabled = true
        activityAddressApiBinding.webView!!.addJavascriptInterface(MyJavaScriptInterface(), "Android")
        activityAddressApiBinding.webView!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                /** 위 웹페이지 로드가 끝나면 html 에디터에서 코드로 작성했던 script 를 호출 */
                view.loadUrl("javascript:sample2_execDaumPostcode();")
                activityAddressApiBinding.webView.visibility = View.VISIBLE
            }
        }
        /** 위에 작성한 블로그 주소를 로드 */
        activityAddressApiBinding.webView!!.loadUrl(blogspot)
    }
}