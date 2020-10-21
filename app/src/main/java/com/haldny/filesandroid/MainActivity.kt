package com.haldny.filesandroid

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey

import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class MainActivity : AppCompatActivity() {

    companion object {
        const val FILE_NAME = "myfilename.txt"
    }

    private lateinit var secretFile: File
    private lateinit var encryptedFile: EncryptedFile

    private val fileEncrypted: File by lazy {
        File(filesDir, FILE_NAME)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        secretFile = File(filesDir, FILE_NAME)

        encryptedFile = EncryptedFile.Builder(
            this,
            secretFile,
            MasterKey.Builder(this).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()
    }

    fun readFromInternalDirectory(view: View) {
        this.openFileInput(FILE_NAME).use {
            tvContent.text = it.readBytes().decodeToString()
        }
    }

    fun readFromInternalDirectoryEncrypted(view: View) {
        encryptedFile.openFileInput().use {
            tvContent.text = it.readBytes().decodeToString()
        }
    }

    fun writeOnInternalDirectory(view: View) {
        val myFileContent = "Name: ${etName.text} - Age: ${etAge.text}"

        this.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(myFileContent.toByteArray())
        }

        Toast.makeText(this, "Saved content: $myFileContent", Toast.LENGTH_LONG).show()
    }

    fun writeOnInternalDirectoryEncrypted(view: View) {
        val myFileContent = "Name: $${etName.text} - Age: ${etAge.text}"

        secretFile.delete()

        encryptedFile.openFileOutput().use {
            it.write(myFileContent.toByteArray())
        }

        Toast.makeText(this, "Saved encrypted content: $myFileContent", Toast.LENGTH_LONG).show()
    }

    private fun printFileContent(file: File) {
        if (file.exists()) {
            val strBuilder = StringBuilder()
            BufferedReader(FileReader(file)).readLines().forEach {
                strBuilder.append(it)
            }
            tvContent.text = strBuilder.toString()
        } else
            Toast.makeText(this, "File not found!", Toast.LENGTH_SHORT).show()
    }

}