package br.com.limopestoques.limop;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import br.com.limopestoques.limop.Sessao.Sessao;

public class Principal extends AppCompatActivity {
    Sessao sessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        sessao = new Sessao(Principal.this);

        String tipo = sessao.getString("tipo");
        if(tipo.equals("Administrador")){
            Administrador administrador = Administrador.newInstance();
            LayoutInflater li = getLayoutInflater();
            View v = administrador.onCreateView(li, null, savedInstanceState);
            LinearLayout um = findViewById(R.id.um);
            um.addView(v);
        }else{

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sair, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sair:
                sessao.setBoolean("login", false);
                sessao.setString("id_usuario", null);

                LoginManager.getInstance().logOut();
                GoogleSignInOptions go = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                GoogleSignInClient gc = GoogleSignIn.getClient(Principal.this,go);
                gc.signOut();
                Intent intent = new Intent(Principal.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Cadastros(View v){
        Intent irTela = new Intent(Principal.this, Cadastros.class);
        startActivity(irTela);
    }

    public void Produtos(View v){
        Intent irTela = new Intent(Principal.this, Produtos_Compras.class);
        startActivity(irTela);
    }

    public void Servicos(View v){
        Intent irTela = new Intent(Principal.this, Servicos_Compras.class);
        startActivity(irTela);
    }

    public void Vendas(View v){
        Intent irTela = new Intent(Principal.this, Vendas.class);
        startActivity(irTela);
    }

    public void SituacaoEstoque(View v){
        Intent irTela = new Intent(Principal.this, SituacaoEstoque.class);
        startActivity(irTela);
    }

    public void OrdemServico(View v){
        Intent irTela = new Intent(Principal.this, OrdemServico.class);
        startActivity(irTela);
    }

    public void Relatorios(View v){
        Intent irTela = new Intent(Principal.this, Relatorios.class);
        startActivity(irTela);
    }

    public void Graficos(View v){
        Intent irTela = new Intent(Principal.this, Graficos.class);
        startActivity(irTela);
    }

    public void Usuarios(View v){
        Intent irTela = new Intent(Principal.this, UsuariosGerenciar.class);
        startActivity(irTela);
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Principal.this);
        builder.setCancelable(true);
        builder.setTitle("Você realmente deseja sair?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sessao.setBoolean("login", false);
                sessao.setString("id_usuario", null);

                LoginManager.getInstance().logOut();
                GoogleSignInOptions go = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
                GoogleSignInClient gc = GoogleSignIn.getClient(Principal.this,go);
                gc.signOut();
                Intent intent = new Intent(Principal.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).setNegativeButton("Não", null);
        builder.create().show();
    }
}
