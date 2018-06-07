package com.exemplo.jordan.selivrando

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.exemplo.jordan.selivrando.models.Genero
import com.exemplo.jordan.selivrando.models.Livro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cadastro_livro.*


class CadastroLivro : AppCompatActivity() {


    private var user = FirebaseAuth.getInstance().currentUser
    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_livro)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mDatabase = FirebaseDatabase.getInstance().getReference();

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    public fun btn_Cadastrar_Livro(view: View) {
        val addValueEventListener = mDatabase?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                var idLivro = dataSnapshot.child("livros").childrenCount
                idLivro += 1

                var livro = Livro(
                        idLivro.toString(),
                        livroTitulo.text.toString(),
                        livroAutor.text.toString(),
                        livroProprietario.text.toString(),
                        livroEdicao.text.toString(),
                        livroDescricao.text.toString(),
                        Genero.CIENCIA,
                        livroPaginas.text.toString(),
                        livroISBN.text.toString(),
                        "",
                        user?.uid.toString()
                )

                Log.d("Count", "Variavel Id = " + livro.id_livro)
                mDatabase?.child("livros")?.child(livro.id_livro)?.setValue(livro)
                onBackPressed()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

    }
}
