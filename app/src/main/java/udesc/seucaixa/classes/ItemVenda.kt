package udesc.seucaixa.classes

class ItemVenda(val id: Int = 0, val clienteId: Int = 0, val produtoId: Int = 0){

    fun conteudoItemVenda(): List<Any> {
        return listOf(id, clienteId, produtoId)
    }
}