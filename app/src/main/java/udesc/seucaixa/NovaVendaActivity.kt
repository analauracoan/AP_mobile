package udesc.seucaixa

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import udesc.seucaixa.classes.DBHelper
import udesc.seucaixa.databinding.ActivityNovaVendaBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import udesc.seucaixa.utils.BarcodeUtil


class NovaVendaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNovaVendaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityNovaVendaBinding.inflate(layoutInflater)
        setContentView(binding.main)

        val db = DBHelper(this)
        val listaClientes = db.clientesSelectAll()

        val clientesFormatados = listaClientes.map {
            "${it.nomeCliente}"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, clientesFormatados)
        binding.spinnerClientes.adapter = adapter

        binding.buttonLerBarcode.setOnClickListener {
            scanCode()
        }

    }

    private fun scanCode() {
        val options = ScanOptions().apply {
            setPrompt("Leia o código")
            setBeepEnabled(false)
            setOrientationLocked(true)
            setCaptureActivity(CaptureActivity::class.java)
        }
        barLauncher.launch(options)
    }

    private val barLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            binding.editBarcode.setText(result.contents.toString())
            val img = BarcodeUtil.generateBarcode(result.contents.toString())
        }
    }
}