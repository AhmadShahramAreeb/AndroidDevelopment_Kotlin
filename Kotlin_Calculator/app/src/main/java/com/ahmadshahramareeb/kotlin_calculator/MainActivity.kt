package com.ahmadshahramareeb.kotlin_calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ahmadshahramareeb.kotlin_calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var numberOne:Double?=null
    private var numberTwo:Double?=null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.addButton.setOnClickListener{
            numberOne=binding.numberOne.text.toString().toDoubleOrNull()
            numberTwo=binding.numberTwo.text.toString().toDoubleOrNull()
            if (numberOne!=null&&numberTwo!=null){
                var calculator=Calculator(numberOne!!,numberTwo!!)
                binding.resultTextView.text="Result : ${calculator.numberOne+calculator.numberTwo}"
            }
            else{
                binding.resultTextView.text="Check Number 1 or Number 2 !"
            }
        }

        binding.subtractButton.setOnClickListener{
            numberOne=binding.numberOne.text.toString().toDoubleOrNull()
            numberTwo=binding.numberTwo.text.toString().toDoubleOrNull()
            if (numberOne!=null&&numberTwo!=null){
                var calculator=Calculator(numberOne!!,numberTwo!!)
                binding.resultTextView.text="Result : ${(calculator.numberOne-calculator.numberTwo)}"
            }
            else{
                binding.resultTextView.text="Check Number 1 or Number 2 !"
            }
        }

        binding.multiplyButton.setOnClickListener{
             numberOne=binding.numberOne.text.toString().toDoubleOrNull()
             numberTwo=binding.numberTwo.text.toString().toDoubleOrNull()
            if (numberOne!=null&&numberTwo!=null){
                var calculator=Calculator(numberOne!!,numberTwo!!)
                binding.resultTextView.text="Result : ${(calculator.numberOne*calculator.numberTwo)}"
            }
            else{
                binding.resultTextView.text="Check Number 1 or Number 2 !"
            }
        }

        binding.divideButton.setOnClickListener{
            numberOne=binding.numberOne.text.toString().toDoubleOrNull()
            numberTwo=binding.numberTwo.text.toString().toDoubleOrNull()
            if (numberOne!=null&&numberTwo!=null){
                var calculator=Calculator(numberOne!!,numberTwo!!)
                binding.resultTextView.text="Result : ${(calculator.numberOne/calculator.numberTwo)}"
            }
            else{
                binding.resultTextView.text="Check Number 1 or Number 2 !"
            }
        }
    }
}