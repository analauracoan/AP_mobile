package udesc.seucaixa.classes

class Venda(val id: Int, val cliente: String, val produtos: String, val data: String, val valorTotal: Double = produtos.sumOf { it.preco }){
}