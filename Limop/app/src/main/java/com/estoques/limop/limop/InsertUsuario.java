package com.estoques.limop.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.estoques.limop.limop.CRUD.CRUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InsertUsuario extends AppCompatActivity {

    EditText nome,foto,email,senha,confirmar_senha,data_nascimento;
    Spinner  tipo;
    RadioGroup sexoGroup;
    Button   button;
    String sexo,senhaa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_usuario);

        nome              = (EditText)findViewById(R.id.nome);
        foto              = (EditText)findViewById(R.id.foto);
        email             = (EditText)findViewById(R.id.email);
        senha             = (EditText)findViewById(R.id.senha);
        confirmar_senha   = (EditText)findViewById(R.id.confirmar_senha);
        data_nascimento   = (EditText)findViewById(R.id.data_nascimento);
        tipo              = (Spinner)findViewById(R.id.tipo);
        sexoGroup         = (RadioGroup)findViewById(R.id.sexo);
        button            = (Button)findViewById(R.id.button);

    }

    public void insertUsuario(View v) {
        Map<String, String> params = new HashMap<String, String>();

        if(sexoGroup.getCheckedRadioButtonId() == R.id.masc){
            sexo = "Masculino";
        }else{
            sexo = "Feminino";
        }

        if(senha.getText().toString().equals(confirmar_senha.getText().toString())){
            senhaa = senha.getText().toString();

        }else{
            Toast.makeText(this, "Senhas não conferem", Toast.LENGTH_SHORT).show();
        }

        params.put("nome", nome.getText().toString().trim());
        params.put("foto", foto.getText().toString().trim());
        params.put("email", email.getText().toString().trim());
        params.put("senha", senhaa);
        params.put("sexo", sexo);
        params.put("nascimento", data_nascimento.getText().toString().trim());
        params.put("tipo", tipo.getSelectedItem().toString());

        CRUD.inserir("https://limopestoques.com.br/Android/Insert/InsertUsuario.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(InsertUsuario.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(InsertUsuario.this, Usuarios.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());

    }

}
