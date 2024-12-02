package udesc.seucaixa

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import udesc.seucaixa.classes.DBHelper
import udesc.seucaixa.classes.Cliente
import udesc.seucaixa.classes.Produto
import udesc.seucaixa.databinding.ActivityNovaVendaBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import android.view.View
import android.widget.AdapterView

class NovaVendaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNovaVendaBinding
    private var barcodeProduto = Produto()
    private lateinit var db: DBHelper
    private var pos: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovaVendaBinding.inflate(layoutInflater)
        setContentView(binding.main)

        db = DBHelper(this)

        var clienteSelecionado: Cliente? = null
        val listaClientes = db.clientesSelectAll()
        val clientesFormatados = listaClientes.map { it.nomeCliente }
        val clienteAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, clientesFormatados)
        binding.spinnerClientes.adapter = clienteAdapter

        binding.spinnerClientes.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    clienteSelecionado = listaClientes[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    clienteSelecionado = null
                }
            }

        binding.buttonLerBarcode.setOnClickListener {
            scanCode()
            binding.editBarcode.setText(barcodeProduto.barcode)
        }

        binding.buttonAdicionarProduto.setOnClickListener {
            var barcode = binding.editBarcode.text.toString()
            var produto = db.produtosSelectByBarcode(barcode)

            if(clienteSelecionado == null) {
                Toast.makeText(applicationContext, "Selecione o cliente!", Toast.LENGTH_SHORT).show()
            }

            val resultado = clienteSelecionado?.let { it1 -> db.itemVendaInsert(it1.id, produto.id)}
            if (resultado != null) {
                if(resultado > 0) {
                    Toast.makeText(applicationContext, "Item da venda adicionado!", Toast.LENGTH_SHORT).show()
                    binding.textProduto.text = produto.nome
                    binding.textCliente.text = clienteSelecionado?.nomeCliente
                } else {
                    Toast.makeText(applicationContext, "Erro ao adicionar produto!\nVerifique os campos digitados.", Toast.LENGTH_SHORT).show()
                }
            }
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
            val barcode = result.contents.toString()
            if (barcodeProduto != null) {
                binding.editBarcode.setText(barcode)
            } else {
                Toast.makeText(this, "Produto não encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
