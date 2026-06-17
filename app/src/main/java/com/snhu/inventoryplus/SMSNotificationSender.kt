package com.snhu.inventoryplus

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object SMSNotificationSender {

    const val SMS_PERMISSION_CODE = 101

    fun hasSmsPermission(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestSmsPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.SEND_SMS),
            SMS_PERMISSION_CODE
        )
    }

    fun sendLowInventoryAlert(activity: Activity, itemName: String,quantity: Int, lowAlert: Int){
        if (quantity > lowAlert) {
            return
        }

        val message = activity.getString(
            R.string.low_inventory_sms_message,
            itemName,
            quantity,
            lowAlert
        )
        //TODO: The phone number needs to be changed to the testing phone to confirm functionality.
        // Format the phone as a 10 digit, covering the country code, area code, central office, line number
        sendSms(activity,"11234567890", message)

    }

    private fun sendSms(activity: Activity, phoneNumber: String, message: String) {
        if (!hasSmsPermission(activity)) {
            requestSmsPermission(activity)
            return
        }

        try {
            val smsManager: SmsManager =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    activity.getSystemService(SmsManager::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    SmsManager.getDefault()
                }

            smsManager.sendTextMessage(
                phoneNumber,
                null,
                message,
                null,
                null
            )

            Toast.makeText(activity, "Low Stock SMS sent", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(
                activity,
                "SMS failed: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }


}