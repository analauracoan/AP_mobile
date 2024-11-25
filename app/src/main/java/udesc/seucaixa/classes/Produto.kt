package udesc.seucaixa.classes

class Produto(var id: Int = 0, var nome: String = "", var preco: Double = 0.0, var descricao: String = "", var barcode: String = "") {

    fun conteudoProduto(): List<Any> {
        return listOf(id, nome, preco, descricao, barcode)
    }

}
