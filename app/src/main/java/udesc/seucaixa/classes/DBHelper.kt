package udesc.seucaixa.classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class DBHelper(context: Context) : SQLiteOpenHelper(context, "banco.db", null, 1) {

    val sql = arrayOf(
        "CREATE TABLE IF NOT EXISTS produtos (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, preco DOUBLE, descricao TEXT, barcode TEXT)",
        "CREATE TABLE IF NOT EXISTS clientes (id INTEGER PRIMARY KEY AUTOINCREMENT, nomeCliente TEXT, endereco TEXT)",
        "CREATE TABLE IF NOT EXISTS vendas (id INTEGER PRIMARY KEY AUTOINCREMENT, clienteId INTEGER, produtoId INTEGER, quantidade INTEGER, valorTotal DOUBLE, " +
                "FOREIGN KEY(clienteId) REFERENCES clientes(id), FOREIGN KEY(produtoId) REFERENCES produtos(id))"
    )

    override fun onCreate(db: SQLiteDatabase?) {
        sql.forEach {
            db?.execSQL(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS vendas")
        db?.execSQL("DROP TABLE IF EXISTS produtos")
        db?.execSQL("DROP TABLE IF EXISTS clientes")
        onCreate(db)
    }



    // PRODUTOS



    fun produtosSelectById(id: Int): Produto {
        val db = this.readableDatabase
        val p = db.rawQuery("SELECT * FROM produtos WHERE id = ?", arrayOf(id.toString()))
        var produto: Produto = Produto()
        if (p.count == 1) {
            p.moveToFirst()

            val idIndex = p.getColumnIndex("id")
            val idNome = p.getColumnIndex("nome")
            val idPreco = p.getColumnIndex("preco")
            val idDescricao = p.getColumnIndex("descricao")
            val idBarcode = p.getColumnIndex("barcode")

            if (idIndex != -1 && idNome != -1 && idPreco != -1 && idDescricao != -1 && idBarcode != -1) {
                val id = p.getInt(idIndex)
                val nome = p.getString(idNome)
                val preco = p.getDouble(idPreco)
                val descricao = p.getString(idDescricao)
                val barcode = p.getString(idBarcode)
                produto = Produto(id, nome, preco, descricao, barcode)
            }
        }
        return produto
    }

    fun produtosSelectAll() : ArrayList<Produto> {
        val db = this.readableDatabase
        val p = db.rawQuery("SELECT * FROM produtos", null)
        var listaProdutos: ArrayList<Produto> = ArrayList()
        if(p.count > 0) {
            p.moveToFirst()
            do {
                val idIndex = p.getColumnIndex("id")
                val idNome = p.getColumnIndex("nome")
                val idPreco = p.getColumnIndex("preco")
                val idDescricao = p.getColumnIndex("descricao")
                val idBarcode = p.getColumnIndex("barcode")
                val id = p.getInt(idIndex)
                val nome = p.getString(idNome)
                val preco = p.getDouble(idPreco)
                val descricao = p.getString(idDescricao)
                val barcode  = p.getString(idBarcode)
                listaProdutos.add(Produto(id, nome, preco, descricao, barcode))
            } while (p.moveToNext())
        }

        p.close()
        return listaProdutos
    }

    fun produtoInsert(nome: String, preco: Double, descricao: String, barcode: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nome", nome)
        contentValues.put("preco", preco)
        contentValues.put("descricao", descricao)
        contentValues.put("barcode", barcode)
        val resultado = db.insert("produtos", null, contentValues)
        db.close()
        return resultado
    }

    fun produtoUpdate(id: Int, nome: String, preco: Double, descricao: String, barcode: String) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nome", nome)
        contentValues.put("preco", preco)
        contentValues.put("descricao", descricao)
        contentValues.put("barcode", barcode)
        val resultado = db.update("produtos", contentValues, "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }

    fun produtoDelete(id: Int) : Int {
        val db = this.writableDatabase
        val resultado = db.delete("produtos", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }



    // CLIENTES



    fun clientesSelectById(id: Int): Cliente {
        val db = this.readableDatabase
        val p = db.rawQuery("SELECT * FROM clientes WHERE id = ?", arrayOf(id.toString()))
        var cliente: Cliente = Cliente()
        if (p.count == 1) {
            p.moveToFirst()

            val idIndex = p.getColumnIndex("id")
            val idNomeCliente = p.getColumnIndex("nomeCliente")
            val idEndereco = p.getColumnIndex("endereco")

            if (idIndex != -1 && idNomeCliente != -1 && idEndereco != -1) {
                val id = p.getInt(idIndex)
                val nomeCliente = p.getString(idNomeCliente)
                val endereco = p.getString(idEndereco)
                cliente = Cliente(id, nomeCliente, endereco)
            }
        }
        return cliente
    }

    fun clientesSelectAll() : ArrayList<Cliente> {
        val db = this.readableDatabase
        val p = db.rawQuery("SELECT * FROM clientes", null)
        var listaClientes: ArrayList<Cliente> = ArrayList()
        if(p.count > 0) {
            p.moveToFirst()
            do {
                val idIndex = p.getColumnIndex("id")
                val idNomeCliente = p.getColumnIndex("nomeCliente")
                val idEndereco = p.getColumnIndex("endereco")
                val id = p.getInt(idIndex)
                val nomeCliente = p.getString(idNomeCliente)
                val endereco = p.getString(idEndereco)
                listaClientes.add(Cliente(id, nomeCliente, endereco))
            } while (p.moveToNext())
        }

        p.close()
        return listaClientes
    }

    fun clienteInsert(nomeCliente: String, endereco: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nomeCliente", nomeCliente)
        contentValues.put("endereco", endereco)
        val resultado = db.insert("clientes", null, contentValues)
        db.close()
        return resultado
    }

    fun clienteUpdate(id: Int, nomeCliente: String, endereco: String) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("nomeCliente", nomeCliente)
        contentValues.put("endereco", endereco)
        val resultado = db.update("clientes", contentValues, "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }

    fun clienteDelete(id: Int) : Int {
        val db = this.writableDatabase
        val resultado = db.delete("clientes", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }



    // VENDAS



    fun vendasInsert(clienteId: Int, produtoId: Int, quantidade: Int, valorTotal: Double): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("clienteId", clienteId)
        contentValues.put("produtoId", produtoId)
        contentValues.put("quantidade", quantidade)
        contentValues.put("valorTotal", valorTotal)
        val resultado = db.insert("vendas", null, contentValues)
        db.close()
        return resultado
    }

    fun vendasSelectAll(): ArrayList<Venda> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT vendas.id, clientes.nomeCliente, produtos.nome, vendas.quantidade, vendas.valorTotal " +
                    "FROM vendas " +
                    "INNER JOIN clientes ON vendas.clienteId = clientes.id " +
                    "INNER JOIN produtos ON vendas.produtoId = produtos.id",
            null
        )
        val listaVendas = ArrayList<Venda>()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val nomeCliente = cursor.getString(cursor.getColumnIndexOrThrow("nomeCliente"))
                val nomeProduto = cursor.getString(cursor.getColumnIndexOrThrow("nome"))
                val quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade"))
                val valorTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("valorTotal"))
                listaVendas.add(Venda(id, nomeCliente, nomeProduto, quantidade, valorTotal))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return listaVendas
    }

    fun vendasDelete(id: Int): Int {
        val db = this.writableDatabase
        val resultado = db.delete("vendas", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }
}
