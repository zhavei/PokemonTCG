package com.syafei.pokemontcg.helper

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.syafei.pokemontcg.R
import com.syafei.pokemontcg.databinding.CostumDialogBinding

class CostumDialog(context: Context) {

    private val dialog = Dialog(context, R.style.CustomDialogTheme)

    fun showDownloadDialog(
        context: Context,
        title: String = "Please Wait ..."
    ) {
        try {
            // init dialog binding
            val dialogBinding = CostumDialogBinding.inflate(
                LayoutInflater.from(context)
            )

            // set dialog content view
            with(dialog) {
                setContentView(dialogBinding.root)
                setCanceledOnTouchOutside(true)
                window?.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                with(dialogBinding) {
                    tvDownload.text = title
                }

                show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismissDownloadDialog() {
        dialog.dismiss()
    }
}