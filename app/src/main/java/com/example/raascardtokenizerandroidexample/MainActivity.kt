package com.example.raascardtokenizerandroidexample

import android.content.Intent
import android.os.Bundle

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.pangea.raas.data.models.CardInformation
import com.pangea.raas.data.models.TokenResponse
import com.pangea.raas.domain.CallBack
import com.pangea.raas.domain.Pangea
import com.pangea.raas.domain.Pangea.Environment
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var mPangea: Pangea

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUI()
        setListeners()
        mPangea = Pangea.createSession(context = applicationContext, debugInfo = true, pangeaSessionID = "4254372383834",environment =  Environment.INTEGRATION)
        displaySession(mPangea)
    }

    private fun setListeners(){
        setTokenButton()
        setGetClientButton()
        setGoToJavaButton()
    }

    private fun displaySession(pangea: Pangea){
        sessionIDTv.text = pangea.getSessionId()
        Log.e(TAG, "displaySession: ${pangea.getSessionId()}")
    }

    private fun setTokenButton(){
        getTokenBtn.setOnClickListener {
            token.text = ""
            createToken()
        }
    }

    private fun setGetClientButton(){

        getDataClientBase64.setOnClickListener {
            dataClientBase64.text = ""
            getSessionClient(mPangea)
        }
    }

    private fun setGoToJavaButton(){
        goToJava.setOnClickListener {
            goToJava()
        }
    }


    private fun createToken(){
        val cardInformation = CardInformation(
            publicKey = publicKey.text.toString(),
            partnerIdentifier = Tenant.text.toString(),
            cardNumber = cardNumber.text.toString(),
            cvv = cvv.text.toString())
        mPangea.createToken(cardInformation, object : CallBack<TokenResponse> {
            override fun onResponse(result: TokenResponse) {
                Log.e(TAG, "onResponse: The token is; ${result.token}" )
                token.text = result.token
            }
            override fun onFailure(result: TokenResponse, throwable: Throwable?) {
                Log.e(TAG, "onResponse: The token is; ${result.token}" )
                Log.e(TAG, "the error is ${throwable?.localizedMessage}" )
                runOnUiThread {
                    token.text = "${result?.token} , ${throwable?.localizedMessage} "
                }

            }
        })
    }

    private fun setUI(){
        setPublicKey()
    }

    private fun setPublicKey(){
        publicKey.setText("WRITE OUT HERE YOUR PUBLIC KEY")
        //Example format for public key:
        //publicKey.setText("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8BMIIBCgKCAQEAs4sA7kSYb7obZpjpB56+RB0hcW7v0pvCvFqel1EsbPiD8bWXUq4sfZ3cEZlCHgcdC8UcxFMAYBQxKUReVV9TUMkMFLEdZKJ2KnaWYBX01tqRftpWAMLhS+IcXnq902ActCT/o5rNrm0bNLQMuqNFvBGJGzXTAubjGMwAbr5CzxmYhWW246Od4/yXSZE0v3dRljM+AOFc5mL3tf0TEQOXDYfYHvq/6yozonyA3NyZp/Brsa+SXQFiCoZWtvUZDFKSMnxGwW2dEAhhgMDxMUULvaXoRsRcaGqgSmv2kSf9AAGFEgdM/peWTOlMQD/mBVm1Pq5Lz0PLsZmfMIiB0v4OwwIDAQAB")
    }

    private fun getSessionClient(pangea: Pangea){
        pangea.getClientData(object :CallBack<String>{
            override fun onResponse(clientInfo: String) {
                println(clientInfo)
                dataClientBase64.text = clientInfo
            }
            override fun onFailure(result: String, throwable: Throwable?) {
                dataClientBase64.text = "error"
                println("getting 64 encoded  failed: ${throwable?.localizedMessage}")
            }
        })
    }

    private fun goToJava(){
        val intent =  Intent(this, MainActivityJava::class.java)
        startActivity(intent)
    }

}
