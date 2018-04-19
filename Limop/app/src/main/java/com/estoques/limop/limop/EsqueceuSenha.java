package com.estoques.limop.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.estoques.limop.limop.CRUD.CRUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EsqueceuSenha extends AppCompatActivity {

    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);
        email = (EditText)findViewById(R.id.email_esq);
    }

    public void campoVazioSenha(View v){
        if(email.getText().length() == 0) {
            Toast.makeText(this, R.string.camposVazios,Toast.LENGTH_SHORT).show();
        }else{
            mudarSenha();
        }
    }
    public void mudarSenha(){
        Map<String, String> params = new HashMap<String, String>();

        params.put("email", email.getText().toString().trim());

        CRUD.inserir("https://limopestoques.com.br/Android/recuperarSenha.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(EsqueceuSenha.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(EsqueceuSenha.this, MainActivity.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(EsqueceuSenha.this, MainActivity.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
