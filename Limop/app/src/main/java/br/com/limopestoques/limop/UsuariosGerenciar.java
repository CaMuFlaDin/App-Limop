package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UsuariosGerenciar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios_gerenciar);
    }

    public void UsuariosGerenciar(View v){
        Intent irTela = new Intent(UsuariosGerenciar.this, Usuarios.class);
        startActivity(irTela);
    }

    public void Acessos(View v){
        Intent irTela = new Intent(UsuariosGerenciar.this, Acessos.class);
        startActivity(irTela);
    }
}
