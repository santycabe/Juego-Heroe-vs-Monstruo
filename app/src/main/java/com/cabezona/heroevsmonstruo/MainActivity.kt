package com.cabezona.heroevsmonstruo

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

// Actividad principal del juego
class MainActivity : AppCompatActivity() {
    // Referencias a los elementos de la interfaz
    private lateinit var tvNombreHeroe: TextInputEditText
    private lateinit var chkPaladin: CheckBox
    private lateinit var chkGuerrero: CheckBox
    private lateinit var chkMago: CheckBox
    private lateinit var mediaPlayerCortina: MediaPlayer
    private lateinit var switchMusica: Switch

    // Método llamado al crear la actividad
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilitar el modo edge-to-edge
        enableEdgeToEdge()
        // Establecer el layout de la actividad
        setContentView(R.layout.activity_main)

        // Inicializar las referencias a los elementos de la interfaz
        tvNombreHeroe = findViewById(R.id.in_nombre)
        chkPaladin = findViewById(R.id.elige_Paladin)
        chkGuerrero = findViewById(R.id.elige_Guerrero)
        chkMago = findViewById(R.id.elige_Mago)
        switchMusica = findViewById(R.id.switch_musica)
        mediaPlayerCortina = MediaPlayer.create(this,R.raw.cortina)

        // Configuración del switch
        switchMusica.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                // Activar música
                mediaPlayerCortina.start()
            } else {
                // Detener música
                mediaPlayerCortina.pause()
            }
        }

        // Establecer listeners para los checkboxes
        chkPaladin.setOnCheckedChangeListener { buttonView, isChecked ->
            // Desactivar los otros checkboxes cuando se selecciona uno
            if (isChecked) {
                chkGuerrero.isChecked = false
                chkMago.isChecked = false
            }
        }

        chkGuerrero.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                chkPaladin.isChecked = false
                chkMago.isChecked = false
            }
        }

        chkMago.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                chkPaladin.isChecked = false
                chkGuerrero.isChecked = false
            }
        }
        reproducirCortina()
    }
    // Método para reproducir la cortina musical
    private fun reproducirCortina() {
        mediaPlayerCortina.start()
        mediaPlayerCortina.isLooping=true
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerCortina.release()
    }

    // Método llamado al pulsar el botón "Comenzar"
    fun accionBtn_comenzar(view: View) {
        // Obtener el nombre del héroe y la clase seleccionada
        val nombreHeroe = tvNombreHeroe.text.toString()
        val claseHeroe = when {
            chkPaladin.isChecked -> "Paladin"
            chkGuerrero.isChecked -> "Guerrero"
            chkMago.isChecked -> "Mago"
            else -> ""
        }

        // Crear un intent para pasar datos a la siguiente actividad
        val intent = Intent(this, Juego::class.java).apply {
            putExtra("nombreHeroe", nombreHeroe)
            putExtra("claseHeroe", claseHeroe)
        }
        // Iniciar la siguiente actividad
        startActivity(intent)

    }
    fun accionBtn_salir(){
        onDestroy()
        finish()
    }
}