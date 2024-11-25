package udesc.seucaixa

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import udesc.seucaixa.classes.DBHelper
import udesc.seucaixa.classes.Produto
import udesc.seucaixa.databinding.ActivityGerenciarProdutosBinding
import udesc.seucaixa.databinding.ActivityMainBinding
import udesc.seucaixa.utils.BarcodeUtil
import java.util.Objects
import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import udesc.seucaixa.utils.ImageUtil
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import com.journeyapps.barcodescanner.BarcodeEncoder

class GerenciarProdutosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGerenciarProdutosBinding
    private lateinit var adapter: ArrayAdapter<Produto>
    private var pos: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityGerenciarProdutosBinding.inflate(layoutInflater)
        setContentView(binding.main)

        val db = DBHelper(this)
        val listaProdutos = db.produtosSelectAll()

        val produtosFormatados = listaProdutos.map {
            "Nome: ${it.nome}\nPreço: R$ %.2f".format(it.preco)
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, produtosFormatados)
        binding.listViewProdutos.adapter = adapter

        binding.buttonGerarBarcode.setOnClickListener {
            binding.editBarcode.setText((1000000000..9999999999).random().toString())
        }

        binding.buttonVisualizarBarcode.setOnClickListener {
            val bitmap = BarcodeUtil.generateBarcode(binding.editBarcode.text.toString())
            binding.imageBarcode.setImageBitmap(bitmap)
            binding.buttonCompartilhar.isVisible = true
        }

        binding.buttonCompartilhar.setOnClickListener {
            try {
                val bitmapView = ImageUtil.getBitmapFromView(binding.barcodeLinearLayout)
                val imageUri: Uri = ImageUtil.saveBitmapToFile(this, bitmapView)
                ImageUtil.shareImage(this, imageUri)
            } catch (e: Exception) {
                Toast.makeText(this, "Não há imagem", Toast.LENGTH_LONG).show()
            }
        }

        binding.listViewProdutos.setOnItemClickListener { _, _, i, _ ->
            binding.editNomeProduto.setText(listaProdutos[i].nome)
            binding.editPreco.setText(listaProdutos[i].preco.toString())
            binding.editDescricao.setText(listaProdutos[i].descricao)
            binding.editBarcode.setText(listaProdutos[i].barcode)

            pos = i
        }

        binding.buttonAdicionar.setOnClickListener {
            val nome = binding.editNomeProduto.text.toString()
            val preco = binding.editPreco.text.toString().toDouble()
            val descricao = binding.editDescricao.text.toString()
            val barcode = binding.editBarcode.text.toString()

            val resultado = db.produtoInsert(nome, preco, descricao, barcode)
            if(resultado > 0) {
                Toast.makeText(applicationContext, "Produto adicionado!", Toast.LENGTH_SHORT).show()

                val novoProduto = Produto(resultado.toInt(), nome, preco, descricao, barcode)
                listaProdutos.add(novoProduto)

                val produtosFormatadosAtualizados = listaProdutos.map {
                    "Nome: ${it.nome}\nPreço: R$ %.2f".format(it.preco)
                }

                adapter.clear()
                adapter.addAll(produtosFormatadosAtualizados)
                adapter.notifyDataSetChanged()

                binding.editNomeProduto.setText("")
                binding.editPreco.setText("")
                binding.editDescricao.setText("")
                binding.editBarcode.setText("")
            } else {
                Toast.makeText(applicationContext, "Erro ao adicionar produto!\nVerifique os campos digitados.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonAtualizar.setOnClickListener {
            if (pos >= 0) {
                val id = listaProdutos[pos].id
                val nome = binding.editNomeProduto.text.toString()
                val preco = binding.editPreco.text.toString().toDouble()
                val descricao = binding.editDescricao.text.toString()
                val barcode = binding.editBarcode.text.toString()

                val resultado = db.produtoUpdate(id, nome, preco, descricao, barcode)

                if(resultado > 0) {
                    Toast.makeText(applicationContext, "Produto atualizado!", Toast.LENGTH_SHORT).show()
                    listaProdutos[pos].nome = nome
                    listaProdutos[pos].preco = preco
                    listaProdutos[pos].descricao = descricao
                    listaProdutos[pos].barcode = barcode

                    val produtosFormatadosAtualizados = listaProdutos.map {
                        "Nome: ${it.nome}\nPreço: R$ %.2f".format(it.preco)
                    }

                    adapter.clear()
                    adapter.addAll(produtosFormatadosAtualizados)
                    adapter.notifyDataSetChanged()

                    pos = -1

                    binding.editNomeProduto.setText("")
                    binding.editPreco.setText("")
                    binding.editDescricao.setText("")
                    binding.editBarcode.setText("")
                } else {
                    Toast.makeText(applicationContext, "Erro ao atualizar produto!\nVerifique os campos digitados.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.buttonExcluir.setOnClickListener {
            if(pos >= 0) {
                val id = listaProdutos[pos].id
                val resultado = db.produtoDelete(id)

                if(resultado > 0) {
                    Toast.makeText(applicationContext, "Produto excluído!", Toast.LENGTH_SHORT).show()
                    listaProdutos.removeAt(pos)

                    val produtosFormatadosAtualizados = listaProdutos.map {
                        "Nome: ${it.nome}\nPreço: R$ %.2f".format(it.preco)
                    }

                    adapter.clear()
                    adapter.addAll(produtosFormatadosAtualizados)
                    adapter.notifyDataSetChanged()

                    pos = -1

                    binding.editNomeProduto.setText("")
                    binding.editPreco.setText("")
                    binding.editDescricao.setText("")
                    binding.editBarcode.setText("")
                } else {
                    Toast.makeText(applicationContext, "Erro ao excluir produto!\nVerifique os campos digitados.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}