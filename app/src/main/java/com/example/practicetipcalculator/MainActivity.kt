package com.example.practicetipcalculator

import android.animation.ArgbEvaluator
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.example.practicetipcalculator.databinding.ActivityMainBinding
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {


    private val INITIAL_TIP_PERCENT = 15
    private val INITIAL_NUM_PEOPLE = 3

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateTipDesc(INITIAL_TIP_PERCENT)
        binding.sb.progress = INITIAL_TIP_PERCENT
        binding.tvTipPercent.text = "$INITIAL_TIP_PERCENT%"
        binding.sbSplitBy.progress = INITIAL_NUM_PEOPLE
        binding.tvPerson.text = "$INITIAL_NUM_PEOPLE People"

        binding.sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "on progress changed $progress")
                binding.tvTipPercent.text = "$progress%"
                computeTipAndTotal()
                updateTipDesc(progress)
                computeSplitTipAndTotal()

            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })


        binding.etBillAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.etBillAmount.toString().isEmpty()) {
                    return
                } else {
                    computeTipAndTotal()
                    computeSplitTipAndTotal()
                }

            }

        })

        binding.sbSplitBy.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvPerson.text = "$progress People"
                if (binding.etBillAmount.text.isEmpty()) {
                    binding.tvTipAmount.text = ""
                    binding.tvTotalAmount.text = ""
                    return
                } else {
                    computeSplitTipAndTotal()
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

    }


    private fun computeSplitTipAndTotal() {

        if (binding.etBillAmount.text.isEmpty()) {
            binding.tvSplitTipAmouunt.text = ""
            binding.tvSplitTotalAmount.text = ""
            return
        }
        val people = binding.sbSplitBy.progress
        val splitTip = binding.tvTipAmount.text.toString().toDouble() / people
        binding.tvSplitTipAmouunt.text = "%.2f".format(splitTip)
        if (people == 0) {
            binding.tvPerson.text = "0 People"
            binding.tvSplitTotalAmount.text = ""
            binding.tvSplitTipAmouunt.text = ""

        } else {
            binding.tvPerson.text = "$people People"
            val splitBill = binding.tvTotalAmount.text.toString().toDouble() / people
            binding.tvSplitTotalAmount.text = "%.2f".format(splitBill)
            binding.btnRound.setOnClickListener {
                binding.tvSplitTotalAmount.text =
                    splitBill.toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString()
                binding.tvSplitTipAmouunt.text =
                    splitTip.toBigDecimal().setScale(1, RoundingMode.UP).toDouble().toString()

            }
        }


    }

    private fun updateTipDesc(progress: Int) {
        val tipDesc = when (progress) {
            in 0..9 -> "poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "amazing"
        }
        binding.tvTipDesc.text = tipDesc

        val color = ArgbEvaluator().evaluate(
            progress.toFloat() / binding.sb.max,
            ContextCompat.getColor(this, R.color.red),
            ContextCompat.getColor(this, R.color.green)
        ) as Int

        binding.tvTipDesc.setTextColor(color)
    }

    private fun computeTipAndTotal() {

        if (binding.etBillAmount.text.isEmpty()) {
            binding.tvTipAmount.text = ""
            binding.tvTotalAmount.text = ""
            return
        }
        val base = binding.etBillAmount.text.toString().toDouble()
        val tipPercent = binding.sb.progress
        var tipAmout = base * tipPercent / 100
        var totalAmount = base + tipAmout

        binding.tvTipAmount.text = "%.2f".format(tipAmout)
        binding.tvTotalAmount.text = "%.2f".format(totalAmount)

    }
}