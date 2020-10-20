package com.haldny.filesandroid

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val FILE_NAME = "myfilename.txt"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUIElements()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnWrite -> writeOnInternalDirectory()
            R.id.btnRead -> readFromInternalDirectory()
        }
    }

    private fun readFromInternalDirectory() {
        this.openFileInput(FILE_NAME).use {
            tvContent.text = it.readBytes().decodeToString()
        }
    }

    private fun writeOnInternalDirectory() {
        val myFileContent = "Name: ${etName.text} - Age: ${etAge.text}"

        this.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(myFileContent.toByteArray())
        }

        Toast.makeText(this, "Saved content: $myFileContent", Toast.LENGTH_LONG).show()
    }

    private fun initUIElements() {
        btnWrite.setOnClickListener(this)
        btnRead.setOnClickListener(this)
    }
}