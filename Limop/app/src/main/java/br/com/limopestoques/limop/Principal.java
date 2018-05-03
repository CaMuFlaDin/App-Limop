package br.com.limopestoques.limop;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.login.LoginManager;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }

    public void Vendas(View v){
        Intent irTela = new Intent(Principal.this, Vendas.class);
        startActivity(irTela);
    }

    public void Compras(View v){
        Intent irTela = new Intent(Principal.this, Compras.class);
        startActivity(irTela);
    }

    public void Usuarios(View v){
        Intent irTela = new Intent(Principal.this, Usuarios.class);
        startActivity(irTela);
    }

    public void Acessos(View v){
        Intent irTela = new Intent(Principal.this, Acessos.class);
        startActivity(irTela);
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Principal.this);
        builder.setCancelable(true);
        builder.setTitle("Você realmente deseja sair?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LoginManager.getInstance().logOut();
                finish();
            }
        }).setNegativeButton("Não", null);
        builder.create().show();
    }
}
