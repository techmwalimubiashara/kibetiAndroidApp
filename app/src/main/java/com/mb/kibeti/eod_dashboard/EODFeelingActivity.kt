package com.mb.kibeti.eod_dashboard

import android.Manifest
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.mb.kibeti.CelebrationPopActivity
import com.mb.kibeti.JourneyMpesaAllocation
import com.mb.kibeti.LoginActivity
import com.mb.kibeti.LoginActivity.MY_PREFERENCES
import com.mb.kibeti.R
import com.mb.kibeti.goal_tracker.GoalTrackerActivity
import com.mb.kibeti.reminder.NotificationHelper
import com.mb.kibeti.reminder.ReminderPreferences
import com.mb.kibeti.reminder.ReminderWorker
import com.mb.kibeti.tap_tracking.TapTracker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class EODFeelingActivity : AppCompatActivity() {
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var sharedPrefs_old: SharedPreferences
    private lateinit var radioGroup: RadioGroup
    private lateinit var barChart: BarChart
    private  var isEodAdded: Boolean =false;
//    private  lateinit var email: String
    private lateinit var tapTracker: TapTracker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eodfeeling)
        supportActionBar?.hide()

        sharedPrefs = getSharedPreferences("FinanceFeelings", Context.MODE_PRIVATE)
        sharedPrefs_old = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE)
        radioGroup = findViewById(R.id.rg_feelings)
        barChart = findViewById(R.id.barChart)

        tapTracker=TapTracker(this);

        val closeButton = findViewById<ImageButton>(R.id.btn_close)
        closeButton.setOnClickListener { finish() }

        val email: String? = sharedPrefs_old . getString (LoginActivity.EMAIL, "");
        val nonNullableStr: String = email!!


//        email = sharedPrefs_old . getString (LoginActivity.EMAIL, "");

        setupChart()
        radioGroup.setOnCheckedChangeListener { _, _ -> saveFeeling(email) }
        updateGraph()

        NotificationHelper.createNotificationChannel(this)
        requestNotificationPermission()
        scheduleDailyReminder(this)



        findViewById<Button>(R.id.btn_set_reminder).setOnClickListener { showTimePickerDialog() }
        findViewById<Button>(R.id.btn_my_spending).setOnClickListener {
//            val intent = Intent(this.applicationContext, GoalTrackerActivity::class.java)
//            intent.putExtra("cashflow", "outflow")
//            this.startActivity(intent)
//            if(isEodAdded){
                val intent = Intent(this, CelebrationPopActivity::class.java)
                startActivity(intent)
                finish()
//            }else{
//                Toast.makeText(this,"Please add your today's money mood", Toast.LENGTH_SHORT).show()
//            }

//            finish()
        }
//        findViewById<Button>(R.id.btn_track_goal).setOnClickListener {
//            val intent = Intent(this.applicationContext,GoalTrackerActivity::class.java)
//            intent.putExtra("cashflow", "outflow")
//             this.startActivity(intent)
//        }
    }

    private fun saveFeeling(email:String) {
        val selectedId = radioGroup.checkedRadioButtonId
        if (selectedId == -1) {
            Toast.makeText(this, "Please select a feeling", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedButton = findViewById<RadioButton>(selectedId)
        val score = when (selectedButton.id) {
            R.id.rb_very_sad -> 1
            R.id.rb_sad -> 2
            R.id.rb_neutral -> 3
            R.id.rb_happy -> 4
            R.id.rb_very_happy -> 5
            else -> 0
        }

        val currentDate = getCurrentDate()
        val feelingsList = getStoredFeelings().toMutableList()

        val existingIndex = feelingsList.indexOfFirst { it.first == currentDate }
        if (existingIndex != -1) {
            feelingsList[existingIndex] = Pair(currentDate, score)
        } else {
            feelingsList.add(Pair(currentDate, score))
            if (feelingsList.size > 7) {
                feelingsList.removeAt(0)
            }
        }


        saveFeelingsToPrefs(feelingsList)
        tapTracker.postTrack(email, "EOD Feeling * "+score+" * "+currentDate)
//        Toast.makeText(this, "Feeling saved!", Toast.LENGTH_SHORT).show()
        isEodAdded=true
        updateGraph()
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun getStoredFeelings(): List<Pair<String, Int>> {
        return sharedPrefs.getString("feelings", "")
            ?.split("|")
            ?.mapNotNull {
                val parts = it.split(",")
                if (parts.size == 2) parts[0] to parts[1].toInt() else null
            } ?: emptyList()
    }

    private fun saveFeelingsToPrefs(feelings: List<Pair<String, Int>>) {
        sharedPrefs.edit()
            .putString("feelings", feelings.joinToString("|") { "${it.first},${it.second}" })
            .apply()
    }

    private fun updateGraph() {
        val storedFeelings = getStoredFeelings()
        val entries = storedFeelings.mapIndexed { index, (date, score) ->
            BarEntry(index.toFloat(), score.toFloat())
        }

        val dataSet = BarDataSet(entries, "").apply {
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return when (value.toInt()) { // Ensure proper emoji display
                        1 -> "😢"
                        2 -> "🙁"
                        3 -> "😐"
                        4 -> "🙂"
                        5 -> "😃"
                        else -> ""
                    }
                }
            }
            valueTextSize = 38f //  Keep emoji size large
            valueTextColor = Color.BLACK
        }

        val barData = BarData(dataSet).apply {
            barWidth = 0.5f
        }

        barChart.apply {
            data = barData


            axisLeft.apply {
                axisMaximum = 5.5f
                axisMinimum = 0f
            }
            axisRight.isEnabled = false

            xAxis.apply {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val index = value.toInt()
                        return if (index in storedFeelings.indices) {
                            val dateParts = storedFeelings[index].first.split("-")
                            "${dateParts[2].toInt()}-${
                                SimpleDateFormat(
                                    "MMM",
                                    Locale.getDefault()
                                ).format(
                                    SimpleDateFormat(
                                        "MM",
                                        Locale.getDefault()
                                    ).parse(dateParts[1])!!
                                )
                            }"
                        } else ""
                    }
                }
                textSize = 12f
            }

            invalidate() // Refresh chart
        }
    }


    private fun setupChart() {

        val axisLabelColor = ContextCompat.getColor(this, R.color.text_color)
        barChart.apply {
            description.isEnabled = false
            setDrawValueAboveBar(true)
            setDrawBarShadow(false)
            setTouchEnabled(false)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(false)
                granularity = 1f
                textColor = axisLabelColor
            }

            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = 5f // Fixed Y-axis values from 0-5
                granularity = 1f
                setDrawGridLines(true)
                textColor = axisLabelColor
            }
            axisRight.isEnabled = false // Hide right Y-axis
        }
    }


    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) Log.d("NotificationPermission", "Permission granted")
                else Log.e("NotificationPermission", "Permission denied")
            }.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun showTimePickerDialog() {
        val (currentHour, currentMinute) = ReminderPreferences.getReminderTime(this)
        TimePickerDialog(this, { _, hour, minute ->
            ReminderPreferences.saveReminderTime(this, hour, minute)
            scheduleDailyReminder(this)
        }, currentHour, currentMinute, true).show()
    }

    private fun scheduleDailyReminder(context: Context) {
        val (hour, minute) = ReminderPreferences.getReminderTime(context)
        val now = Calendar.getInstance()
        val scheduledTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }
        var initialDelay = scheduledTime.timeInMillis - now.timeInMillis
        if (initialDelay < 0) {
            scheduledTime.add(Calendar.DAY_OF_MONTH, 1)
            initialDelay = scheduledTime.timeInMillis - now.timeInMillis
        }
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .setConstraints(Constraints.Builder().setRequiresBatteryNotLow(true).build())
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "mpesa_reminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}

