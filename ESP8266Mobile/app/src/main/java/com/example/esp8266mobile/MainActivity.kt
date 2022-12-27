package com.example.esp8266mobile

import android.content.DialogInterface.OnClickListener
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.esp8266mobile.databinding.ActivityMainBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var request: Request
    private lateinit var binding: ActivityMainBinding
    private lateinit var pref: SharedPreferences
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pref = getSharedPreferences("MyPref", MODE_PRIVATE)
        onClickSaveIp()
        getIp()
        binding.apply {
            bsavelp.setOnClickListener(onClickListener())
            bsavetime.setOnClickListener(onClickListener())
            bsavepogoda.setOnClickListener(onClickListener())
            brestart.setOnClickListener(onClickListener())
        }
    }

    private fun onClickListener(): android.view.View.OnClickListener{
        return View.OnClickListener {
            when(it.id){
                R.id.bsavelp -> {post("ssid=${binding.editlogin.text}&password=${binding.editpassword.text}")}
                R.id.bsavetime -> {post("timezone=${binding.edtime.text}")}
                R.id.bsavepogoda -> {post("city=${binding.edpogoda.text}")}
                R.id.brestart -> {post("restart")}
            }
        }
    }

    private fun getIp() = with(binding){
        val ip = pref.getString("ip", "")
        if (ip != null){
            if(ip.isNotEmpty()) edip.setText(ip)
        }
    }

    private fun onClickSaveIp() = with(binding){
        bsaveip.setOnClickListener(){
            if(edip.text.isNotEmpty())saveIp(edip.text.toString())
        }
    }

    private fun saveIp(ip: String){
        val editor = pref.edit()
        editor.putString("ip", ip)
        editor.apply()
    }

    private fun post(post: String){
        Thread {
            request = Request.Builder().url("http://${binding.edip.text}/${post}").build()
            try {
                var response = client.newCall(request).execute()
            } catch (i: IOException) {

            }
        }.start()
    }
}