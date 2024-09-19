package com.cabezona.heroevsmonstruo

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay

// Definición de la clase Juego que extiende AppCompatActivity, lo que la convierte en una actividad de Android
class Juego : AppCompatActivity() {

    // Declaración de las variables para los componentes de la interfaz gráfica
    private lateinit var imgHeroe: ImageView
    private lateinit var imgMonstruo: ImageView
    private lateinit var tvVidaHeroe: TextView
    private lateinit var tvVidaMonstruo: TextView
    private lateinit var tvInfo: TextView
    private lateinit var btnAtacar: Button
    private lateinit var mediaPlayerGolpe:MediaPlayer

    // Variables que almacenan la información del héroe y el monstruo
    private var vidaHeroe = 0
    private var vidaMonstruo = 0
    private var nombreHeroe = ""
    private var ataqueHeroe = 0
    private var ataqueMonstruo = 0

    // Método que se llama cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita el modo de pantalla completa o "Edge to Edge"
        setContentView(R.layout.juego) // Establece el diseño de la actividad a partir del archivo XML 'juego' ---h


        // Vincula las vistas (componentes gráficos) a las variables correspondientes
        imgHeroe = findViewById(R.id.img_heroe)
        imgMonstruo = findViewById(R.id.img_enemigo)
        tvVidaHeroe = findViewById(R.id.tv_vida_heroe)
        tvVidaMonstruo = findViewById(R.id.tv_vida_monstruo)
        tvInfo = findViewById(R.id.tv_info)
        btnAtacar = findViewById(R.id.btnAtacar)
        mediaPlayerGolpe = MediaPlayer.create(this,R.raw.golpe)

        // Obtiene el nombre y la clase del héroe desde la actividad anterior a través de un Intent
        nombreHeroe = intent.getStringExtra("nombreHeroe").toString()
        val claseHeroe = intent.getStringExtra("claseHeroe").toString()

        // Configura los atributos del héroe según su clase
        when (claseHeroe) {
            "Paladin" -> {
                imgHeroe.setImageResource(R.drawable.paladin) // Asigna la imagen del Paladín
                vidaHeroe = 100 // Asigna la vida inicial del Paladín
                ataqueHeroe = 20 // Asigna el ataque del Paladín
            }
            "Guerrero" -> {
                imgHeroe.setImageResource(R.drawable.guerrero) // Asigna la imagen del Guerrero
                vidaHeroe = 120 // Asigna la vida inicial del Guerrero
                ataqueHeroe = 25 // Asigna el ataque del Guerrero
            }
            "Mago" -> {
                imgHeroe.setImageResource(R.drawable.mago) // Asigna la imagen del Mago
                vidaHeroe = 80 // Asigna la vida inicial del Mago
                ataqueHeroe = 30 // Asigna el ataque del Mago
            }
        }

        // Inicializa los atributos del monstruo
        vidaMonstruo = 100
        ataqueMonstruo = (10 .. 25).random()

        // Actualiza los TextView para mostrar la vida actual del héroe y del monstruo
        tvVidaHeroe.text = "Vida: $vidaHeroe"
        tvVidaMonstruo.text = "Vida: $vidaMonstruo"



        //------------------------------------------------------------------------------------------
        // Configura el evento para el botón de ataque, que activa el ataque al monstruo
        btnAtacar.setOnClickListener {
            // Desactivar el botón para evitar ataques repetidos
            btnAtacar.isEnabled = false

            atacarMonstruo() // Ejecuta el ataque al monstruo

            // Verificar si el monstruo ha muerto
            if (vidaMonstruo <= 0) {
                btnAtacar.isEnabled = false
                tvInfo.text ="Has Ganado"
                tvVidaMonstruo.text = "Muerto!"

                // Agregar delay de 3 segundos antes de cerrar la actividad
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 3000)
            } else {
                // Después de 2 segundos, el monstruo ataca al héroe si no ha muerto
                Handler(Looper.getMainLooper()).postDelayed({
                    atacarHeroe()

                    // Verificar si el héroe ha muerto
                    if (vidaHeroe <= 0) {
                        btnAtacar.isEnabled = false
                        tvInfo.text = "Has Perdido"
                        tvVidaHeroe.text = "Heroe derrotado"


                        // Agregar delay de 3 segundos antes de cerrar la actividad
                        Handler(Looper.getMainLooper()).postDelayed({
                            finish()
                        }, 3000)
                    }

                    // Volver a activar el botón después del ataque del monstruo
                    btnAtacar.isEnabled = true
                }, 1000)
            }
        }
    }

    // Método para reproducir el sonido de golpe
    private fun reproducirSonidoGolpe() {
        mediaPlayerGolpe.start()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerGolpe.release()
    }

    // Método para atacar al monstruo
    private fun atacarMonstruo() {
        // Generar un daño aleatorio para el ataque
        val daño = (1..ataqueHeroe).random()
        // Restar el daño de la vida del monstruo
        vidaMonstruo -= daño
        // Actualizar la interfaz con la vida actual del monstruo
        tvVidaMonstruo.text = "Vida: $vidaMonstruo"
        // Mostrar información sobre el ataque
        tvInfo.text = "$nombreHeroe atacó al monstruo y le descontó $daño puntos de vida"

        // Mover la imagen hacia la derecha
        imgHeroe.translationX += 50f
        reproducirSonidoGolpe()

        // Agregar delay de 200 milisegundos
        Handler(Looper.getMainLooper()).postDelayed({
            // Mover la imagen hacia la izquierda (regresar a la posición original)
            imgHeroe.translationX -= 50f
        }, 200)


    }

    // Método que controla el ataque del monstruo al héroe
    private fun atacarHeroe() {
        // Calcula un daño aleatorio en un rango del poder de ataque del monstruo
        val daño = (1..ataqueMonstruo).random()
        vidaHeroe -= daño // Resta el daño a la vida del héroe
        tvVidaHeroe.text = "Vida: $vidaHeroe" // Actualiza el TextView con la nueva vida del héroe
        tvInfo.text = "El monstruo atacó a $nombreHeroe y le descontó $daño puntos de vida" // Muestra la información del ataque

        // Mover la imagen hacia la derecha
        imgMonstruo.translationX -= 50f
        reproducirSonidoGolpe()

        // Agregar delay de 200 milisegundos
        Handler(Looper.getMainLooper()).postDelayed({
            // Mover la imagen hacia la izquierda (regresar a la posición original)
            imgMonstruo.translationX += 50f
        }, 200)


    }

    fun accionBtn_salir2() {
        mediaPlayerGolpe.release()
        finish()
    }
}
