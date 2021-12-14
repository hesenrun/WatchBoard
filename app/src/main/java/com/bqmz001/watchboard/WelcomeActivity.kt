package com.bqmz001.watchboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bqmz001.watchboard.databinding.ActivityWelcomeBinding
import com.bqmz001.watchboard.settings.CityBean
import com.bqmz001.watchboard.settings.CitySelectActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView
import com.orhanobut.hawk.Hawk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.litepal.LitePal
import org.litepal.extension.saveAll
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder

class WelcomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityWelcomeBinding
    var cityCode = ""
    var city = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isInit = Hawk.get<Boolean>("init", false)
        if (isInit) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSelectCity.setOnClickListener {
            val intent = Intent(this, CitySelectActivity::class.java)
            startActivityForResult(intent, 100)
        }
        binding.cvGo.setOnClickListener {
            if (binding.etPublicId.text.toString().equals("")) {
                Toast.makeText(this, "请填写Public ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (binding.etApiKey.text.toString().equals("")) {
                Toast.makeText(this, "请填写API Key", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (cityCode.equals("")) {
                Toast.makeText(this, "请选择城市", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Hawk.put("qweather_public_id", binding.etPublicId.text.toString())
            Hawk.put("qweather_api_key", binding.etApiKey.text.toString())
            Hawk.put("refresh", 600000)
            Hawk.put("init", true)
            val app = application as App
            app.initQWeather()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        initCityList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            cityCode = Hawk.get("qweather-city-id")
            city = Hawk.get("qweather-city")
            binding.btnSelectCity.setText(city)
        }
    }


    fun initCityList() {
        LitePal.deleteAll(CityBean::class.java)
        val popup = XPopup.Builder(this)
            .isDestroyOnDismiss(true)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .asLoading("加载中") as LoadingPopupView
        popup.show()
        lifecycleScope.launch {
            val list = withContext(Dispatchers.IO) {
                val inputReader =
                    InputStreamReader(getResources().getAssets().open("city.json"), "UTF-8")
                val buffer = CharArray(2048)
                val builder = StringBuilder()
                while (true) {
                    val rsz: Int = inputReader.read(buffer, 0, buffer.size)
                    if (rsz < 0) break
                    builder.append(buffer, 0, rsz)
                }
                inputReader.close()
                val result = builder.toString()

                val gson = Gson()
                gson.fromJson(
                    result,
                    object : TypeToken<List<CityBean?>?>() {}.type
                ) as MutableList<CityBean>
            }

            val result = withContext(Dispatchers.IO) {
                list.saveAll()
            }
            popup.dismiss()

        }
    }
}