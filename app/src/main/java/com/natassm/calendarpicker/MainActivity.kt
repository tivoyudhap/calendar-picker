package com.natassm.calendarpicker

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.natassm.calendarpicker.databinding.ActivityMainBinding
import com.natassm.calendarpickerlib.NSCalendarView

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(LayoutInflater.from(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.hehe.type = NSCalendarView.CALENDAR_RANGE_PICKER
    }
}