package com.exemplo.jordan.selivrando.models

class Doacoes(
        var livro: Livro,
        var local_retirada: String,
        var doador: Usuario,
        var data_postagem: String,
        var interessado: String
) {}