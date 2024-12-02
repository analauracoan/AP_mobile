package udesc.seucaixa.classes

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "banco.db", null, 2) {

    val sql = arrayOf(
        "CREATE TABLE IF NOT EXISTS produtos (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, preco DOUBLE, descricao TEXT, barcode TEXT)",
        "CREATE TABLE IF NOT EXISTS clientes (id INTEGER PRIMARY KEY AUTOINCREMENT, nomeCliente TEXT, endereco TEXT)",
        "CREATE TABLE IF NOT EXISTS itemVenda (id INTEGER PRIMARY KEY AUTOINCREMENT, clienteId INTEGER, produtoId INTEGER, FOREIGN KEY(clienteId) REFERENCES clientes(id), FOREIGN KEY(produtoId) REFERENCES produtos(id))",
    )

    override fun onCreate(db: SQLiteDatabase?) {
        sql.forEach {
            db?.execSQL(it)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS produtos")
        db?.execSQL("DROP TABLE IF EXISTS clientes")
        db?.execSQL("DROP TABLE IF EXISTS itemVenda")
        onCreate(db)
    }



    // PRODUTOS



    fun produtosSelectByBarcode(barcode: String): Produto {
        val db = this.readableDatabase
        val p = db.rawQuery("SELECT * FROM produtos WHERE barcode = ?", arrayOf(barcode.toString()))
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



    // ITEM VENDA PRODUTO



    fun itemVendaSelectById(id: Int): ItemVenda {
        val db = this.readableDatabase
        val p = db.rawQuery("SELECT * FROM itemVenda WHERE id = ?", arrayOf(id.toString()))
        var itemVenda: ItemVenda = ItemVenda()
        if (p.count == 1) {
            p.moveToFirst()

            val idIndex = p.getColumnIndex("id")
            val clienteId = p.getColumnIndex("clienteId")
            val produtoId = p.getColumnIndex("produtoId")

            if (idIndex != -1 && clienteId != -1 && produtoId != -1) {
                val id = p.getInt(idIndex)
                val clienteId = p.getInt(clienteId)
                val produtoId = p.getInt(produtoId)
                itemVenda = ItemVenda(id, clienteId, produtoId)
            }
        }
        return itemVenda
    }

    fun itemVendaInsert(clienteId: Int, produtoId: Int): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("clienteId", clienteId)
        contentValues.put("produtoId", produtoId)
        val resultado = db.insert("itemVenda", null, contentValues)
        db.close()
        return resultado
    }
}
