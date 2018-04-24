package com.estoques.limop.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.estoques.limop.limop.CRUD.CRUD;
import com.estoques.limop.limop.Construtoras.ClientesConst;
import com.estoques.limop.limop.Construtoras.FornCompraConst;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InsertProdVenda extends AppCompatActivity {

    private String idCliente;

    EditText data_venda,disponivel_estoque,min_estoque,max_estoque,peso_liquido,peso_bruto,nome_produto,valor_venda,valor_custo;
    Spinner nome_cliente,categoriaProd;
    Button button;

    ArrayAdapter<String> adapter;
    ArrayList<ClientesConst> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_prod_venda);

        nome_cliente              = findViewById(R.id.cliente);
        data_venda                = findViewById(R.id.data_venda);
        disponivel_estoque        = findViewById(R.id.disponivel_estoque);
        min_estoque               = findViewById(R.id.min_estoque);
        max_estoque               = findViewById(R.id.max_estoque);
        peso_liquido              = findViewById(R.id.peso_liquido);
        peso_bruto                = findViewById(R.id.peso_bruto);
        nome_cliente              = findViewById(R.id.cliente);
        valor_venda               = findViewById(R.id.valor_venda);
        valor_custo               = findViewById(R.id.valor_custo);
        categoriaProd             = findViewById(R.id.categoria);


        button                    = (Button)findViewById(R.id.button);

        adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        clientes = new ArrayList<ClientesConst>();
        carregarCliente();

        nome_cliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idCliente = clientes.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void insertProduto(View v) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("cliente", nome_cliente.getSelectedItem().toString());
        params.put("tipo", categoriaProd.getSelectedItem().toString());
        params.put("foto", data_venda.getText().toString().trim());
        params.put("disponivel_estoque", disponivel_estoque.getText().toString().trim());
        params.put("min_estoque", min_estoque.getText().toString().trim());
        params.put("max_estoque", max_estoque.getText().toString().trim());
        params.put("peso_liquido", peso_liquido.getText().toString().trim());
        params.put("peso_bruto", peso_bruto.getText().toString().trim());
        params.put("valor_venda", valor_venda.getText().toString().trim());
        params.put("valor_custo", valor_custo.getText().toString().trim());
        //params.put("id_fornecedor", idFornecedor);

        CRUD.inserir("https://limopestoques.com.br/Android/Insert/InsertProdVenda.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(InsertProdVenda.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(InsertProdVenda.this, Produtos_Vendas.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());

    }
    public void carregarCliente(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://limopestoques.com.br/Android/Json/jsonClientes.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray forncompraArray = obj.getJSONArray("clientes");

                            for (int i = 0; i < forncompraArray.length(); i++){
                                JSONObject forncompraObject = forncompraArray.getJSONObject(i);

                                ClientesConst forneCompra = new ClientesConst(forncompraObject.getString("id_cliente"), forncompraObject.getString("nome_cliente"), forncompraObject.getString("tipo"), forncompraObject.getString("telefone_comercial"));

                                clientes.add(forneCompra);
                                adapter.add(forneCompra.getNome());
                            }
                            nome_cliente.setAdapter(adapter);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("select", "select");

                return params;
            }
        };

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(InsertProdVenda.this, Produtos_Vendas.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
