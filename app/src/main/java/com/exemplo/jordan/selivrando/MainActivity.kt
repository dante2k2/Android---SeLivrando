package com.exemplo.jordan.selivrando

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.exemplo.jordan.selivrando.models.Livro
import com.exemplo.jordan.selivrando.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var user = FirebaseAuth.getInstance().currentUser  //Pega a instancia do usuario logado
    private var user2: Usuario? = null
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null
    private var mDatabase: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->    //Se o usario estiver logado, Mostrar na tela o email
            user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                Toast.makeText(this@MainActivity, "Conectado: " + user?.email, Toast.LENGTH_LONG).show()
            } else {                                                        //Se fizer logout, Va para tela do login
                // User is signed out
                Toast.makeText(this@MainActivity, "Desconectado", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            // ...
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();      //Vai no banco pegar os dados do usuario
        val getUsuario = mDatabase?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user2 = dataSnapshot.child("users").child(user?.uid).getValue(Usuario::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })


        fab.setOnClickListener { view ->        //FloatButton
            startActivity(Intent(this@MainActivity, CadastroLivro::class.java))
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)

        val postListener = mDatabase?.addValueEventListener( object : ValueEventListener { // Pega no banco os livros para montar a Recyclo View
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                var arrayLivros:ArrayList<Livro> = ArrayList<Livro>()
                var livros = dataSnapshot.child("livros").children
                livros.forEach {
                    var livro: Livro? = it.getValue(Livro::class.java)
                    livro?.proprietario = dataSnapshot.child("users").child(livro?.proprietario).child("nome").getValue().toString()
                    arrayLivros.add(livro!!)
                }
                var ea = MyAdapter(this, arrayLivros){  //CAso clicado em algum item da Recyclo View, Acessar o ID em questão e enviar para outra Activity, Para puxar os dados!
                    var i = Intent(this@MainActivity, BookdescActivity::class.java)
                    i.putExtra("livroId", it.id_livro)
                    startActivity(i)
                }
                rv.adapter = ea
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                // ...
            }
        })

        /*
        var eventos:ArrayList<Livro> = ArrayList<Livro>()
        for(i in 0..100){
            var e = Livro("Livro: ${i}", "Descrição: ${i}", "Autor: ${i}")
            eventos.add(e)
        }
        var ea = MyAdapter(this, eventos){
            //var i = Intent(Intent.ACTION_VIEW, Uri.parse("http:/www.google.com"))
            var i = Intent(this@MainActivity, BookdescActivity::class.java)
            startActivity(i)
        }
        rv.adapter = ea
        */
    }

    public override fun onStart() {
        super.onStart()
        mAuth?.addAuthStateListener(mAuthListener!!)
    }

    public override fun onStop() {
        super.onStop()
        if (mAuthListener != null) {
            mAuth?.removeAuthStateListener(mAuthListener!!)
        }
    }

    override fun onBackPressed() {  //Voltar tela
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { //Criação da nav-drawe
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        //Pesquisa novamente no banco o usuario para puxar os dados necessarios
        mDatabase = FirebaseDatabase.getInstance().getReference();
        val getUsuario = mDatabase?.addListenerForSingleValueEvent(object : ValueEventListener { //Puxa do banco dados do usuario para colocar dos dados do usuario no Nav-Drawer
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                user2 = dataSnapshot.child("users").child(user?.uid).getValue(Usuario::class.java)
                userName.text = user2?.nome  //Aqui
                userEmail.text = user2?.email//Aqui
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.profile -> {
                startActivity(Intent(this@MainActivity, Profile::class.java))
            }
            R.id.my_books -> {

            }
            R.id.requests -> {

            }
            R.id.nav_logout ->{   // Caso ser feito logout, esquecer os SharedPreference
                FirebaseAuth.getInstance().signOut()
                PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("ManterConectado", false).commit()
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("UserToken", "").commit()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    public fun btn_FirstBook(view: View){
        startActivity(Intent(this@MainActivity, BookdescActivity::class.java))
    }
}
