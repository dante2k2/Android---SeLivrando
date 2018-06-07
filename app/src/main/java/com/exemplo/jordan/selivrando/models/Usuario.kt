package com.exemplo.jordan.selivrando.models

class Usuario (
    var uid: String? = "",
    var nome: String? = "",
    var email: String? = "",
    var telefone: String? = "",
    var endereço: String? = "",
    var idade: String? = "",
    var credito: Int? = 1,
    var doacoes: Int? = 0,
    var avaliacao: Int? = 0,
    var retiradas: Int? = 0,
    var avatar: String? = ""
) {}