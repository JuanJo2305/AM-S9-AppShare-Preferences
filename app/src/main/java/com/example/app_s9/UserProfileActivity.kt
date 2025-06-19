package com.example.app_s9

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class UserProfileActivity : AppCompatActivity() {

    private lateinit var prefsHelper: SharedPreferencesHelper
    private lateinit var etName: EditText
    private lateinit var etAge: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSave: Button
    private lateinit var btnLoad: Button
    private lateinit var tvProfileData: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        prefsHelper = SharedPreferencesHelper(this)

        etName = findViewById(R.id.etName)
        etAge = findViewById(R.id.etAge)
        etEmail = findViewById(R.id.etEmail)
        btnSave = findViewById(R.id.btnSave)
        btnLoad = findViewById(R.id.btnLoad)
        tvProfileData = findViewById(R.id.tvProfileData)


        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val age = etAge.text.toString().toIntOrNull()
            val email = etEmail.text.toString().trim()

            if (name.isEmpty() || age == null || email.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefsHelper.saveString(SharedPreferencesHelper.KEY_PROFILE_NAME, name)
            prefsHelper.saveInt(SharedPreferencesHelper.KEY_PROFILE_AGE, age)
            prefsHelper.saveString(SharedPreferencesHelper.KEY_PROFILE_EMAIL, email)

            Toast.makeText(this, "Perfil guardado", Toast.LENGTH_SHORT).show()
        }

        btnLoad.setOnClickListener {
            val name = prefsHelper.getString(SharedPreferencesHelper.KEY_PROFILE_NAME, "No registrado")
            val age = prefsHelper.getInt(SharedPreferencesHelper.KEY_PROFILE_AGE, 0)
            val email = prefsHelper.getString(SharedPreferencesHelper.KEY_PROFILE_EMAIL, "Sin email")

            etName.setText(name)
            etAge.setText(age.toString())
            etEmail.setText(email)

            val profileInfo = "Nombre: $name\nEdad: $age\nEmail: $email"
            tvProfileData.text = profileInfo
        }

    }
}


