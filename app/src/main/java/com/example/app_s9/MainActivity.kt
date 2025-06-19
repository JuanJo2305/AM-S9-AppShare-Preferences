package com.example.app_s9

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat.applyTheme
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

// Falta que se muestres las visitas en el modo oscuro
//  En perfl de usuario falta que se muestren los datos  (si se puede que  también tenga modo oscuro)
class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private lateinit var editTextUsername: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonLoad: Button
    private lateinit var buttonClear: Button
    private lateinit var textViewResult: TextView
    private lateinit var buttonProfile: Button
    private lateinit var switchDarkMode: Switch
    private lateinit var buttonResetCounter: Button
    private lateinit var textViewVisits: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar SharedPreferencesHelper
        sharedPreferencesHelper = SharedPreferencesHelper(this)

        // Inicializar vistas
        initViews()

        // Configurar listeners
        setupListeners()

        // Verificar si es la primera vez que se abre la app
        checkFirstTime()

        incrementVisitCounter()

        // Aplicar el tema guardado al iniciar
        val isDarkMode = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_THEME_MODE, false)
        switchDarkMode.isChecked = isDarkMode
        applyTheme(isDarkMode)

    }

    private fun initViews() {
        editTextUsername = findViewById(R.id.editTextUsername)
        buttonSave = findViewById(R.id.buttonSave)
        buttonLoad = findViewById(R.id.buttonLoad)
        buttonClear = findViewById(R.id.buttonClear)
        textViewResult = findViewById(R.id.textViewResult)
        buttonProfile = findViewById(R.id.btnOpenProfile)
        switchDarkMode = findViewById(R.id.switchDarkMode)
        buttonResetCounter = findViewById(R.id.buttonResetCounter)
        textViewVisits = findViewById(R.id.textViewVisits)



    }

    private fun setupListeners() {
        buttonSave.setOnClickListener {
            saveData()
        }

        buttonLoad.setOnClickListener {
            loadData()
        }

        buttonClear.setOnClickListener {
            clearAllData()
        }

        buttonProfile.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferencesHelper.saveBoolean(SharedPreferencesHelper.KEY_THEME_MODE, isChecked)
            applyTheme(isChecked)
        }

        buttonResetCounter.setOnClickListener {
            sharedPreferencesHelper.saveInt(SharedPreferencesHelper.KEY_VISIT_COUNT, 0)
            textViewVisits.text = "Visitas: 0"
            Toast.makeText(this, "Contador reiniciado", Toast.LENGTH_SHORT).show()
        }




    }

    private fun applyTheme(isDarkMode: Boolean) {
        val rootLayout = findViewById<View>(R.id.main)
        val textColor: Int
        val backgroundColor: Int

        if (isDarkMode) {
            backgroundColor = Color.parseColor("#424242") // Gris oscuro
            textColor = Color.WHITE
        } else {
            backgroundColor = Color.WHITE
            textColor = Color.BLACK
        }

        rootLayout.setBackgroundColor(backgroundColor)
        textViewResult.setTextColor(textColor)
        editTextUsername.setTextColor(textColor)
        editTextUsername.setHintTextColor(textColor)

        // También puedes cambiar color a los botones si deseas
        buttonSave.setTextColor(textColor)
        buttonLoad.setTextColor(textColor)
        buttonClear.setTextColor(textColor)
        buttonProfile.setTextColor(textColor)
        switchDarkMode.setTextColor(textColor)
    }


    private fun saveData() {
        val username = editTextUsername.text.toString().trim()

        if (username.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa un nombre", Toast.LENGTH_SHORT).show()
            return
        }

        // Guardar datos
        sharedPreferencesHelper.saveString(SharedPreferencesHelper.KEY_USERNAME, username)
        sharedPreferencesHelper.saveBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, false)
        sharedPreferencesHelper.saveInt(SharedPreferencesHelper.KEY_USER_ID, (1000..9999).random())

        Toast.makeText(this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show()
        editTextUsername.setText("")
    }

    private fun incrementVisitCounter() {
        val count = sharedPreferencesHelper.getInt(SharedPreferencesHelper.KEY_VISIT_COUNT, 0) + 1
        sharedPreferencesHelper.saveInt(SharedPreferencesHelper.KEY_VISIT_COUNT, count)
        textViewVisits.text = "Visitas: $count"
    }



    private fun loadData() {
        val username = sharedPreferencesHelper.getString(SharedPreferencesHelper.KEY_USERNAME, "Sin nombre")
        val isFirstTime = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, true)
        val userId = sharedPreferencesHelper.getInt(SharedPreferencesHelper.KEY_USER_ID, 0)

        val result = "Usuario: $username\nID: $userId\nPrimera vez: ${if (isFirstTime) "Sí" else "No"}"
        textViewResult.text = result
    }

    private fun clearAllData() {
        sharedPreferencesHelper.clearAll()
        textViewResult.text = ""
        editTextUsername.setText("")
        Toast.makeText(this, "Todas las preferencias han sido eliminadas", Toast.LENGTH_SHORT).show()
    }

    private fun checkFirstTime() {
        val isFirstTime = sharedPreferencesHelper.getBoolean(SharedPreferencesHelper.KEY_IS_FIRST_TIME, true)

        if (isFirstTime) {
            Toast.makeText(this, "¡Bienvenido por primera vez!", Toast.LENGTH_LONG).show()
        }
    }
}