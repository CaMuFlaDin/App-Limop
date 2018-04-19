package com.estoques.limop.limop;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.estoques.limop.limop.CRUD.CRUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

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

        nome = (EditText)findViewById(R.id.nome);
        email = (EditText)findViewById(R.id.email);
        telefone = (EditText)findViewById(R.id.tel_celular);
        assunto = (EditText)findViewById(R.id.assunto);
        mensagem = (EditText)findViewById(R.id.msg);

        MaskEditTextChangedListener maskTELCE = new MaskEditTextChangedListener("(##)#####-####",telefone);
        telefone.addTextChangedListener(maskTELCE);
    }

    public void enviarMensagem(View v){


        if(nome.getText().length() == 0 || email.getText().length() == 0 || telefone.getText().length() == 0 || assunto.getText().length() == 0 || mensagem.getText().length() == 0){
           try{
               Toast.makeText(this, R.string.camposVazios, Toast.LENGTH_SHORT).show();
           }catch (Exception ex){
               Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
           }

        }else{
            enviarEmail();
        }
    }
    public void enviarEmail() {
        Map<String, String> params = new HashMap<String, String>();

        params.put("nome", nome.getText().toString().trim());
        params.put("email", email.getText().toString().trim());
        params.put("telefone", telefone.getText().toString().trim());
        params.put("assunto", assunto.getText().toString().trim());
        params.put("mensagem", mensagem.getText().toString().trim());

        CRUD.inserir("https://limopestoques.com.br/Android/enviarEmail.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(Contato.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(Contato.this, MainActivity.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());

    }

}
