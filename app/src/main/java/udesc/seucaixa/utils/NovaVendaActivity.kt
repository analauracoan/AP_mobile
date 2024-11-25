package udesc.seucaixa.utils

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import udesc.seucaixa.R
import udesc.seucaixa.databinding.ActivityNovaVendaBinding

class NovaVendaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNovaVendaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.main)



    }
}