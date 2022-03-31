package com.bqmz001.watchboard.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.*
import com.bqmz001.watchboard.R
import androidx.core.content.ContextCompat.startActivity
import com.lxj.xpopup.XPopup
import com.orhanobut.hawk.Hawk


class SettingsActivity : AppCompatActivity() {
    var shortPress = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            finish()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val weather_public_id = findPreference<EditTextPreference>("weather_public_id")
            val weather_api_key = findPreference<EditTextPreference>("weather_api_key")
            val weather_location = findPreference<Preference>("weather_location")
            val weather_refresh = findPreference<ListPreference>("weather_refresh")
            val weather_advanced = findPreference<SwitchPreference>("weather_advanced")
            val alarm_longest_time = findPreference<ListPreference>("alarm_longest_time")
            val alarm_volume=findPreference<Preference>("alarm_volume")
            val system_settings = findPreference<Preference>("system_settings")
            val dark = findPreference<SwitchPreference>("dark")

            val about = findPreference<Preference>("about")

            weather_public_id!!.setSummary(Hawk.get("qweather_public_id", "未设置"))
            weather_api_key!!.setSummary(Hawk.get("qweather_api_key", "未设置"))
            weather_location!!.setSummary(Hawk.get("qweather-city", "未设置"))

            weather_public_id!!.setOnPreferenceChangeListener { preference, newValue ->
                Hawk.put("qweather_public_id", newValue.toString())
                preference.setSummary(newValue.toString())
                true
            }
            weather_api_key!!.setOnPreferenceChangeListener { preference, newValue ->
                Hawk.put("qweather_api_key", newValue.toString())
                preference.setSummary(newValue.toString())
                true
            }
            weather_location.setOnPreferenceClickListener {
                val intent = Intent(requireContext(), CitySelectActivity::class.java)
                startActivityForResult(intent, 100)
                true
            }
            weather_refresh!!.setOnPreferenceChangeListener { preference, newValue ->
                Hawk.put("refresh", newValue.toString().toInt())
                true
            }
            alarm_longest_time!!.setOnPreferenceChangeListener { preference, newValue ->
                Hawk.put("alarm_long", newValue.toString().toInt())
                if(newValue.toString().toInt()==0){
                    Toast.makeText(requireContext(), "请注意扰民问题", Toast.LENGTH_LONG).show();
                }
                true
            }
            alarm_volume!!.setOnPreferenceClickListener {
                XPopup.Builder(requireContext())
                    .isDestroyOnDismiss(true)
                    .asCustom(AlarmVolumeDialog(requireContext()))
                    .show()
                true
            }
            system_settings!!.setOnPreferenceClickListener {
                requireContext().startActivity(Intent(Settings.ACTION_SETTINGS))
                true
            }
            weather_advanced!!.setOnPreferenceChangeListener { prefrence, newValue ->
                Hawk.put("advanced", newValue)
                true
            }
            dark!!.setOnPreferenceChangeListener { preference, newValue ->
                AppCompatDelegate.setDefaultNightMode(
                    when (newValue as Boolean) {
                        true -> AppCompatDelegate.MODE_NIGHT_YES
                        false -> AppCompatDelegate.MODE_NIGHT_NO
                    }
                )
                Hawk.put("dark", newValue)
                true
            }

        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == 100 && resultCode == RESULT_OK) {
                val weather_location = findPreference<Preference>("weather_location")
                weather_location!!.summary = Hawk.get("qweather-city", "")
                Toast.makeText(requireContext(), "设置完成后下次刷新生效", Toast.LENGTH_LONG).show();

            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //这鬼玩意keycode 219
        if (keyCode == 219) {
            if (event!!.getAction() == KeyEvent.ACTION_DOWN) {
                event.startTracking();
                if (event.getRepeatCount() == 0) {
                    shortPress = true;
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == 219) {
            shortPress = false;
            finish()
            return true;
        }
        return false;
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == 219) {
            if (shortPress) {
                Toast.makeText(this, "长按离开设置页面", Toast.LENGTH_LONG).show();
            } else {

            }
            shortPress = false;
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}