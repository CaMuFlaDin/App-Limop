package com.estoques.limop.limop;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Contato extends AppCompatActivity {

    EditText nome;
    EditText email;
    EditText telefone;
    EditText assunto;
    EditText mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);
    }

    public void enviarMensagem(View v){
        nome = (EditText)findViewById(R.id.nome);
        email = (EditText)findViewById(R.id.email);
        telefone = (EditText)findViewById(R.id.tel_celular);
        assunto = (EditText)findViewById(R.id.assunto);
        mensagem = (EditText)findViewById(R.id.msg);
        if(nome.getText().length() == 0 || email.getText().length() == 0 || telefone.getText().length() == 0 || assunto.getText().length() == 0 || mensagem.getText().length() == 0){
           try{
               Toast.makeText(this, R.string.camposVazios, Toast.LENGTH_SHORT).show();
           }catch (Exception ex){
               Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
           }

        }
    }
}
