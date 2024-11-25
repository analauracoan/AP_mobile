package udesc.seucaixa

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import udesc.seucaixa.classes.Cliente
import udesc.seucaixa.classes.DBHelper
import udesc.seucaixa.classes.Produto
import udesc.seucaixa.databinding.ActivityGerenciarClientesBinding
import udesc.seucaixa.databinding.ActivityGerenciarProdutosBinding

class GerenciarClientesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGerenciarClientesBinding
    private lateinit var adapter: ArrayAdapter<Produto>
    private var pos: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityGerenciarClientesBinding.inflate(layoutInflater)
        setContentView(binding.main)

        val db = DBHelper(this)
        val listaClientes = db.clientesSelectAll()

        val clientesFormatados = listaClientes.map {
            "Nome: ${it.nomeCliente}"
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, clientesFormatados)
        binding.listViewClientes.adapter = adapter

        binding.listViewClientes.setOnItemClickListener { _, _, i, _ ->
            binding.editNomeCliente.setText(listaClientes[i].nomeCliente)
            binding.editEndereco.setText(listaClientes[i].endereco)

            pos = i
        }

        binding.buttonAdicionar.setOnClickListener {
            val nomeCliente = binding.editNomeCliente.text.toString()
            val endereco = binding.editEndereco.text.toString()

            val resultado = db.clienteInsert(nomeCliente, endereco)
            if(resultado > 0) {
                Toast.makeText(applicationContext, "Cliente adicionado!", Toast.LENGTH_SHORT).show()

                val novoCliente = Cliente(resultado.toInt(), nomeCliente, endereco)
                listaClientes.add(novoCliente)

                val clientesFormatadosAtualizados = listaClientes.map {
                    "Nome: ${it.nomeCliente}"
                }

                adapter.clear()
                adapter.addAll(clientesFormatadosAtualizados)
                adapter.notifyDataSetChanged()

                binding.editNomeCliente.setText("")
                binding.editEndereco.setText("")
            } else {
                Toast.makeText(applicationContext, "Erro ao adicionar cliente!\nVerifique os campos digitados.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonAtualizar.setOnClickListener {
            if (pos >= 0) {
                val id = listaClientes[pos].id
                val nomeCliente = binding.editNomeCliente.text.toString()
                val endereco= binding.editEndereco.text.toString()

                val resultado = db.clienteUpdate(id, nomeCliente, endereco)

                if(resultado > 0) {
                    Toast.makeText(applicationContext, "Cliente atualizado!", Toast.LENGTH_SHORT).show()
                    listaClientes[pos].nomeCliente = nomeCliente
                    listaClientes[pos].endereco = endereco

                    val clientesFormatadosAtualizados = listaClientes.map {
                        "Nome: ${it.nomeCliente}"
                    }

                    adapter.clear()
                    adapter.addAll(clientesFormatadosAtualizados)
                    adapter.notifyDataSetChanged()

                    pos = -1

                    binding.editNomeCliente.setText("")
                    binding.editEndereco.setText("")
                } else {
                    Toast.makeText(applicationContext, "Erro ao atualizar cliente!\nVerifique os campos digitados.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.buttonExcluir.setOnClickListener {
            if(pos >= 0) {
                val id = listaClientes[pos].id
                val resultado = db.clienteDelete(id)

                if(resultado > 0) {
                    Toast.makeText(applicationContext, "Cliente excluído!", Toast.LENGTH_SHORT).show()
                    listaClientes.removeAt(pos)

                    val clientesFormatadosAtualizados = listaClientes.map {
                        "Nome: ${it.nomeCliente}"
                    }

                    adapter.clear()
                    adapter.addAll(clientesFormatadosAtualizados)
                    adapter.notifyDataSetChanged()

                    pos = -1

                    binding.editNomeCliente.setText("")
                    binding.editEndereco.setText("")
                } else {
                    Toast.makeText(applicationContext, "Erro ao excluir cliente!\nVerifique os campos digitados.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}