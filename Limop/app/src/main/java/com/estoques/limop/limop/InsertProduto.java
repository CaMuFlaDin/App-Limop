package com.estoques.limop.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.estoques.limop.limop.CRUD.CRUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InsertProduto extends AppCompatActivity {

    EditText foto,disponivel_estoque,min_estoque,max_estoque,peso_liquido,peso_bruto,nome_produto,valor_venda,valor_custo;
    Spinner fornecedor,categoriaProd;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_produto);

        nome_produto              = findViewById(R.id.nome);
        foto                      = findViewById(R.id.foto);
        disponivel_estoque        = findViewById(R.id.disponivel_estoque);
        min_estoque               = findViewById(R.id.min_estoque);
        max_estoque               = findViewById(R.id.max_estoque);
        peso_liquido              = findViewById(R.id.peso_liquido);
        peso_bruto                = findViewById(R.id.peso_bruto);
        fornecedor                = findViewById(R.id.fornecedor);
        valor_venda               = findViewById(R.id.valor_venda);
        valor_custo               = findViewById(R.id.valor_custo);
        categoriaProd             = findViewById(R.id.categoria);


        button                    = (Button)findViewById(R.id.button);
    }
    public void insertProduto(View v) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("nome", nome_produto.getText().toString().trim());
        params.put("tipo", categoriaProd.getSelectedItem().toString());
        params.put("foto", foto.getText().toString().trim());
        params.put("disponivel_estoque", disponivel_estoque.getText().toString().trim());
        params.put("min_estoque", min_estoque.getText().toString().trim());
        params.put("max_estoque", max_estoque.getText().toString().trim());
        params.put("peso_liquido", peso_liquido.getText().toString().trim());
        params.put("peso_bruto", peso_bruto.getText().toString().trim());
        params.put("valor_venda", valor_venda.getText().toString().trim());
        params.put("valor_custo", valor_custo.getText().toString().trim());

        CRUD.inserir("https://limopestoques.com.br/Android/Insert/InsertProduto.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(InsertProduto.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(InsertProduto.this, Produtos_Compras.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());

    }
}


