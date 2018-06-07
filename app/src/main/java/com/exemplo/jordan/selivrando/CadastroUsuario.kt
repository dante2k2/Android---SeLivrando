package com.exemplo.jordan.selivrando

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.exemplo.jordan.selivrando.models.Usuario
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.activity_cadastro_usuario.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import android.util.Log
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class CadastroUsuario : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    public fun Cadastrar(view: View) {

        mAuth?.createUserWithEmailAndPassword(cad_Email.text.toString(), cad_Senha.text.toString())
                ?.addOnCompleteListener(this) { task ->
                    Log.d("FirebaseCadastro", "Cadastrado com sucesso" + task.isSuccessful)

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful) {
                        Toast.makeText(this@CadastroUsuario, "Falha ao Cadastrar",
                                Toast.LENGTH_SHORT).show()
                    }
                    var usuario = Usuario(
                            task.getResult().user.uid.toString(),
                            cad_Nome.text.toString(),
                            cad_Email.text.toString(),
                            cad_Telefone.text.toString(),
                            cad_Endereco.text.toString(),
                            cad_Idade.text.toString(),
                            1,
                            0,
                            0,
                            0,
                            ""
                    )

                    mDatabase?.child("users")?.child(usuario.uid)?.setValue(usuario)
                    onBackPressed()
                }
    }
}
