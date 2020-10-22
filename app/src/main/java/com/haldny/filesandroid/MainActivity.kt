package com.haldny.filesandroid

import android.content.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    private lateinit var encryptedSharedPreferences: SharedPreferences
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
    }

    private fun init() {
        val masterKey =
            MasterKey.Builder(this)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

        prefs = getSharedPreferences("aula03", 0)

        encryptedSharedPreferences = EncryptedSharedPreferences.create(
            this,
            "values_secured",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    }

    fun nextPage(view: View) {
        startActivity(Intent(this, MainActivity2::class.java))
    }

    fun readFromSecureSharedPreference(view: View) {
        val name = encryptedSharedPreferences.getString("name", "")
        val age = encryptedSharedPreferences.getInt("age", -1)

        tvContent.text = "Name: $name - Age: $age"
    }

    fun readFromSharedPreference(view: View) {
        val name = prefs.getString("name", "")
        val age = prefs.getInt("age", -1)

        tvContent.text = "Name: $name - Age: $age"
    }

    fun writeOnSecureSharedPreference(view: View) {
        val editor = encryptedSharedPreferences.edit()

        editor.apply {
            putString("name", etName.text.toString())
            putInt("age", etAge.text.toString().toInt())

            apply()

            Toast.makeText(
                this@MainActivity, "content name = ${etName.text}, age = ${etAge.text}", Toast.LENGTH_LONG).show()
        }

    }

    fun writeOnSharedPreference(view: View) {
        val editor = prefs.edit()

        editor.apply {
            putString("name", etName.text.toString())
            putInt("age", etAge.text.toString().toInt())

            apply()

            Toast.makeText(
                this@MainActivity, "content name = ${etName.text}, age = ${etAge.text}", Toast.LENGTH_LONG).show()
        }

    }

}

data class Image(val id: Long, val name: String, val uri: Uri)