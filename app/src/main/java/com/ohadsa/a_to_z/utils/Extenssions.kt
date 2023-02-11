package com.ohadsa.a_to_z.utils

import android.R
import android.text.method.LinkMovementMethod

import android.widget.TextView

import android.text.Html

import android.os.Build

import com.google.android.material.dialog.MaterialAlertDialogBuilder

import java.io.IOException

import java.io.InputStream

import android.app.Activity
import androidx.appcompat.app.AlertDialog

//
//fun openHtmlTextDialog(activity: Activity, fileNameInAssets: String?) {
//    var str: String? = ""
//    var `is`: InputStream? = null
//    try {
//        `is` = activity.assets.open(fileNameInAssets!!)
//        val size = `is`.available()
//        val buffer = ByteArray(size)
//        `is`.read(buffer)
//        `is`.close()
//        str = String(buffer)
//    } catch (e: IOException) {
//        e.printStackTrace()
//    }
//
//    val materialAlertDialogBuilder = MaterialAlertDialogBuilder(activity )
//    materialAlertDialogBuilder.setPositiveButton("Close", null)
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//        materialAlertDialogBuilder.setMessage(Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY))
//    } else {
//        materialAlertDialogBuilder.setMessage(Html.fromHtml(str))
//    }
//    val al: AlertDialog = materialAlertDialogBuilder.show()
//    val alertTextView: TextView? = al.findViewById(R.id.message)
//    alertTextView?.movementMethod = LinkMovementMethod.getInstance()
//}