package udesc.seucaixa

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import udesc.seucaixa.classes.DBHelper
import udesc.seucaixa.classes.Cliente
import udesc.seucaixa.classes.Produto
import udesc.seucaixa.classes.Venda
import udesc.seucaixa.databinding.ActivityNovaVendaBinding
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import android.view.View
import android.widget.AdapterView

class NovaVendaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNovaVendaBinding
    private lateinit var db: DBHelper
    private var clienteSelecionado: Cliente? = null
    private val produtosSelecionados = mutableListOf<Produto>()
    private lateinit var produtosAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNovaVendaBinding.inflate(layoutInflater)
        setContentView(binding.main)

        db = DBHelper(this)
        configurarSpinnerClientes()
        configurarBotoes()

        produtosAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        binding.listViewProdutos.adapter = produtosAdapter
    }

    private fun configurarSpinnerClientes() {
        val listaClientes = db.clientesSelectAll()
        val clientesFormatados = listaClientes.map { it.nomeCliente }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, clientesFormatados)
        binding.spinnerClientes.adapter = adapter

        binding.spinnerClientes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                clienteSelecionado = listaClientes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                clienteSelecionado = null
            }
        }

    }

    private fun configurarBotoes() {
        binding.buttonLerBarcode.setOnClickListener {
            scanCode()
        }

        binding.buttonAdicionarProduto.setOnClickListener {
            finalizarVenda()
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
            val produto = db.buscarProdutoPorCodigo(barcode)
            if (produto != null) {
                produtosSelecionados.add(produto)
                atualizarResumoVenda()
            } else {
                Toast.makeText(this, "Produto não encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun atualizarResumoVenda() {
        // Atualizar a lista de produtos no adapter
        val produtosNomes = produtosSelecionados.map { it.nome }
        produtosAdapter.clear()
        produtosAdapter.addAll(produtosNomes)
        produtosAdapter.notifyDataSetChanged()

        // Atualizar o valor total
        val valorTotal = produtosSelecionados.sumOf { it.preco }
        binding.textValorTotal.text = "Total: R$ %.2f".format(valorTotal)
    }

    private fun finalizarVenda() {
        if (clienteSelecionado == null) {
            Toast.makeText(this, "Selecione um cliente", Toast.LENGTH_SHORT).show()
            return
        }

        if (produtosSelecionados.isEmpty()) {
            Toast.makeText(this, "Adicione pelo menos um produto", Toast.LENGTH_SHORT).show()
            return
        }

        val venda = Venda(
            id = 0, // O banco pode gerar automaticamente o ID
            cliente = clienteSelecionado!!.toString(),
            produtos = produtosSelecionados.toString(),
            data = System.currentTimeMillis().toString(), // Formatar conforme necessário
        )

        val sucesso = db.salvarVenda(venda)
        if (sucesso) {
            Toast.makeText(this, "Venda concluída!", Toast.LENGTH_SHORT).show()
            limparFormulario()
        } else {
            Toast.makeText(this, "Erro ao salvar venda", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limparFormulario() {
        produtosSelecionados.clear()
        clienteSelecionado = null
        binding.spinnerClientes.setSelection(0)
        binding.textValorTotal.text = ""
    }
}
