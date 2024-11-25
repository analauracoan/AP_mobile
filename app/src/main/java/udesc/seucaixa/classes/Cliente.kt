package udesc.seucaixa.classes

class Cliente(var id: Int = 0, var nomeCliente: String = "", var endereco: String = "") {

    fun conteudoCliente(): List<Any> {
        return listOf(id, nomeCliente, endereco)
    }

}
