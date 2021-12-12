package com.bqmz001.watchboard.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bqmz001.watchboard.databinding.ActivityCitySelectBinding
import com.orhanobut.hawk.Hawk
import org.litepal.LitePal

class CitySelectActivity : AppCompatActivity() {
    lateinit var binding: ActivityCitySelectBinding
    lateinit var list: MutableList<String>
    lateinit var adapter: CitySelectAdapter
    var province = ""
    var city = ""
    var town = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        list = mutableListOf()
        binding.ivBack.setOnClickListener { finish() }
        binding.rvList.layoutManager = LinearLayoutManager(this)
        adapter = CitySelectAdapter(list)
        adapter.setOnItemClickListener { adapter, view, position ->
            if (province.equals("") && city.equals("") && town.equals("")) {
                province = list[position]
                findCity(province, null)
            } else if (!province.equals("") && city.equals("") && town.equals("")) {
                city = list[position]
                findCity(province, city)
            } else if (!province.equals("") && !city.equals("") && town.equals("")) {
                town = list[position]
                val id = "CN${getGeoId(province, city, town)}"
                Hawk.put("qweather-city", "$province $city $town")
                Hawk.put("qweather-city-simple", town)
                Hawk.put("qweather-city-id", id)
                setResult(RESULT_OK)
                finish()
            }

        }
        binding.rvList.adapter = adapter
        binding.rvList.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

        findCity(null, null)
    }

    fun findCity(province: String?, city: String?) {
        var sql = ""
        if (province == null && city == null)
            sql = "select province from citybean group by city"
        else if (province != null && city == null)
            sql = "select city from citybean where province = '$province' group by city"
        else if (province != null && city != null)
            sql =
                "select town from citybean where province = '$province' and city = '$city' group by town"

        val cursor = LitePal.findBySQL(sql)
        list.clear()
        cursor.moveToFirst()
        do {
            list.add(cursor.getString(0))
        } while (cursor.moveToNext())
        cursor.close()
        adapter.notifyDataSetChanged()
    }

    fun getGeoId(province: String, city: String, town: String): String {
        val cursor =
            LitePal.findBySQL("select geoid from citybean where province = '$province' and city = '$city' and town='$town'")
        var result = ""
        if (cursor.count > 0) {
            cursor.moveToFirst()
            result = cursor.getString(0)
        }
        cursor.close()
        return result
    }
}