package com.exemplo.jordan.selivrando

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.exemplo.jordan.selivrando.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.nav_header_main.*

class Profile : AppCompatActivity() {

    private var user = FirebaseAuth.getInstance().currentUser  //Pega a instancia do usuario logado
    private var user2: Usuario? = null
    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //Pesquisa novamente no banco o usuario para puxar os dados necessarios
        mDatabase = FirebaseDatabase.getInstance().getReference();
        val getUsuario = mDatabase?.addListenerForSingleValueEvent(object : ValueEventListener { //Puxa do banco dados do usuario para colocar dos dados do usuario no Nav-Drawer
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user2 = dataSnapshot.child("users").child(user?.uid).getValue(Usuario::class.java)
                profile_name.setText(user2?.nome)
                profile_email.setText(user2?.email)
                profile_address.setText(user2?.endereço)
                profile_age.setText(user2?.idade)
                profile_phone.setText(user2?.telefone)

                profile_credit.text = user2?.credito!!.toString()

                val userWithdraw: Int = user2?.retiradas!!.toInt()
                val userDonate: Int = user2?.doacoes!!.toInt()
                val userRating: Int = user2?.avaliacao!!.toInt()

                profile_withdraw.text = userWithdraw.toString()
                profile_donate.text = userDonate.toString()

                if (userRating != 0 && userWithdraw != 0 && userDonate != 0)
                    profile_rating.text = (userRating / (userWithdraw + userDonate)).toString()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }
    fun changeUser() {
//        val changeName: String = profile_name.text.toString()
//        val chageEmail: String = profile_email.text.toString()
//        val changeAddress: String = profile_address.text.toString()
//        val changeAge: String = profile_age.text.toString()
//        val changePhone: String = profile_phone.text.toString()
//
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        mDatabase?.child("users/"+user?.uid+"/nome")?.setValue(changeName)
//        mDatabase?.child("users/"+user?.uid+"/email")?.setValue(chageEmail)
//        mDatabase?.child("usersusers/"+user?.uid+"/endereço")?.setValue(changeAddress)
//        mDatabase?.child("usersusers/"+user?.uid+"/idade")?.setValue(changeAge)
//        mDatabase?.child("usersusers/"+user?.uid+"/telefone")?.setValue(changePhone)
    }
}
