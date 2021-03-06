package com.deepaliverma.tara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var phoneNumber:String
    private lateinit var countryCode:String
    private lateinit var alertDialogBuilder: MaterialAlertDialogBuilder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        phoneNumberEt.addTextChangedListener{
            nextBtn.isEnabled=!(it.isNullOrEmpty()|| it.length<10)
        }

        nextBtn.setOnClickListener {
            checkNumber()
        }
    }


    private fun checkNumber() {
        countryCode = ccp.selectedCountryCodeWithPlus
        phoneNumber = countryCode + phoneNumberEt.text.toString()

        if (validatePhoneNumber(phoneNumberEt.text.toString())) {
            notifyUserBeforeVerify(
                "We will be verifying the phone number:$phoneNumber\n" +
                        "Is this OK, or would you like to edit the number?"
            )
        } else {
            toast("Please enter a valid number to continue!")
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun notifyUserBeforeVerify(message: String) {
        alertDialogBuilder = MaterialAlertDialogBuilder(this).apply {
            setMessage(message)
            setPositiveButton("Ok") { _, _ ->
                showOtpActivity()
            }

            setNegativeButton("Edit") { dialog, _ ->
                dialog.dismiss()
            }

            setCancelable(false)
            create()
            show()
        }
    }

    private fun showOtpActivity() {
        startActivity(
            Intent(this, OtpActivity::class.java).putExtra(PHONE_NUMBER, phoneNumber)
        )
        finish()
    }
}

private fun validatePhoneNumber(phone: String): Boolean {
    if (phone.isEmpty()) {
        return false
    }
    return true


}

