package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import br.com.limopestoques.limop.CRUD.CRUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InsertServico extends AppCompatActivity {

    String ServerURL = "" ;
    EditText nome, valor_custo, valor_venda;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_servico);

        nome = (EditText)findViewById(R.id.nome);
        valor_custo = (EditText)findViewById(R.id.valor_custo);
        valor_venda = (EditText)findViewById(R.id.valor_venda);
        button = (Button)findViewById(R.id.button);
    }

    public void campoVazio(View v){
        if(nome.getText().length() == 0 || valor_custo.getText().length() == 0 || valor_venda.getText().length() == 0) {
            Toast.makeText(this, R.string.camposVazios,Toast.LENGTH_SHORT).show();
        }else{
            insertServico();
        }
    }

    public void insertServico() {
        Map<String, String> params = new HashMap<String, String>();

        params.put("nome", nome.getText().toString().trim());
        params.put("valor_custo", valor_custo.getText().toString().trim());
        params.put("valor_venda", valor_venda.getText().toString().trim());

        CRUD.inserir("https://limopestoques.com.br/Android/Insert/InsertServico.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(InsertServico.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(InsertServico.this, Servicos_Compras.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());

    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(InsertServico.this, Servicos_Compras.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}