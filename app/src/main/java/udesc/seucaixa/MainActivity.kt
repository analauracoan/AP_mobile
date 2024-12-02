package udesc.seucaixa

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import udesc.seucaixa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.main)

        binding.buttonNovaVenda.setOnClickListener {
            var intent = Intent(this, NovaVendaActivity::class.java)
            startActivity(intent)
        }

        binding.buttonGerenciarProdutos.setOnClickListener {
            var intent = Intent(this, GerenciarProdutosActivity::class.java)
            startActivity(intent)
        }

        binding.buttonGerenciarClientes.setOnClickListener {
            var intent = Intent(this, GerenciarClientesActivity::class.java)
            startActivity(intent)
        }

    }
}