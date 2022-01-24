package com.bqmz001.watchboard.alarm

import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bqmz001.watchboard.databinding.ActivityAlarmBinding
import com.bqmz001.watchboard.dpToPixelValue
import com.bqmz001.watchboard.refreshAlarm
import org.litepal.LitePal

class AlarmActivity : AppCompatActivity() {
    lateinit var binding: ActivityAlarmBinding
    lateinit var adapter: AlarmAdapter
    lateinit var list: MutableList<AlarmBean>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener { finish() }
        binding.ivAdd.setOnClickListener {
            val intent = Intent(this, AlarmEditActivity::class.java)
            intent.putExtra("type", "add")
            startActivityForResult(intent, 1)
        }

        list = LitePal.order("hour,minute").find(AlarmBean::class.java)
        binding.rvList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        adapter = AlarmAdapter(list)
        adapter.setOnItemClickListener { adapter, view, position ->
            val intent = Intent(this, AlarmEditActivity::class.java)
            intent.putExtra("type", "edit")
            intent.putExtra("id", list[position].uuid)
            startActivityForResult(intent, 1)
        }
        binding.rvList.adapter = adapter
        binding.rvList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = dpToPixelValue(this@AlarmActivity, 16)
                outRect.top = dpToPixelValue(this@AlarmActivity, 8)
                outRect.right = dpToPixelValue(this@AlarmActivity, 16)

                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.left = dpToPixelValue(this@AlarmActivity, 16)
                } else {
                    outRect.left = 0

                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            list = LitePal.order("hour,minute").find(AlarmBean::class.java)
            adapter.setList(list)
            refreshAlarm(this)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == 219) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}