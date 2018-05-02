package br.com.limopestoques.limop;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.com.limopestoques.limop.CRUD.CRUD;
import br.com.limopestoques.limop.Construtoras.ClientesConst;
import br.com.limopestoques.limop.Construtoras.FornCompraConst;
import br.com.limopestoques.limop.Construtoras.ProdCompraConst;

public class EditarProdVenda extends AppCompatActivity {

    private String idCliente;
    private String idProduto;
    private String id;

    EditText data_venda,qtd, valor_unitario,desconto,vencimento,obs;
    Spinner nome_cliente,produto,stts_negociacao,contrato,cond_pagamento,forma_pagamento;
    Button button;

    ArrayAdapter<String> adapter;
    ArrayList<ClientesConst> clientes;

    ArrayAdapter<String> adapter2;
    ArrayList<ProdCompraConst> produtos;

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Update/updateProdVenda.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_prod_venda);

        nome_cliente              = findViewById(R.id.cliente);
        produto                   = findViewById(R.id.produto);
        data_venda                = findViewById(R.id.data_venda);
        stts_negociacao           = findViewById(R.id.stts_negociacao);
        contrato                  = findViewById(R.id.contrato);
        qtd                       = findViewById(R.id.qtd);
        valor_unitario            = findViewById(R.id.valor_unitario);
        desconto                  = findViewById(R.id.desconto);
        vencimento                = findViewById(R.id.vencimento);
        obs                       = findViewById(R.id.obs);
        cond_pagamento            = findViewById(R.id.cond_pagamento);
        forma_pagamento            = findViewById(R.id.forma_pagamento);


        button                    = (Button)findViewById(R.id.button);

        adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        clientes = new ArrayList<ClientesConst>();
        carregarCliente();

        adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        produtos = new ArrayList<ProdCompraConst>();
        carregarProduto();

        nome_cliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idCliente = clientes.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        produto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idProduto = produtos.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        try{
            Intent i = getIntent();
            this.id = i.getStringExtra("id");
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        carregar();
    }
    public void carregar(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray prodvendaArray = obj.getJSONArray("vendas");
                            JSONObject jo = prodvendaArray.getJSONObject(0);

                            data_venda.setText(jo.getString("data_venda"));
                            qtd.setText(jo.getString("quantidade"));
                            valor_unitario.setText(jo.getString("valor"));
                            desconto.setText(jo.getString("desconto"));
                            vencimento.setText(jo.getString("vencimento"));
                            obs.setText(jo.getString("observacoes"));
                            String status = jo.getString("status_negociacao");
                            String contratoo = jo.getString("contrato");
                            String condicao = jo.getString("condicao_pagamento");
                            String forma = jo.getString("forma_pagamento");

                            if(status.equals("Venda")){
                                stts_negociacao.setSelection(0);
                            }else if(status.equals("Orçamento")){
                                stts_negociacao.setSelection(1);
                            }else if(status.equals("Orçamento Aceito")){
                                stts_negociacao.setSelection(2);
                            }else{
                                stts_negociacao.setSelection(3);
                            }

                            if(contratoo.equals("Cobrança")){
                                contrato.setSelection(0);
                            }else{
                                contrato.setSelection(1);
                            }

                            if(forma.equals("Boleto")){
                                forma_pagamento.setSelection(0);
                            }else if(forma.equals("Cartão de Crédito")){
                                forma_pagamento.setSelection(1);
                            }else if(forma.equals("Cartão de Débito")){
                                forma_pagamento.setSelection(2);
                            }else{
                                forma_pagamento.setSelection(3);
                            }

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
                params.put("id_venda", id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void updatevenda(View v) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("update", "update");
        params.put("id_venda", id);
        params.put("data_venda", data_venda.getText().toString().trim());
        params.put("status", stts_negociacao.getSelectedItem().toString());
        params.put("contrato", contrato.getSelectedItem().toString());
        params.put("qtd", qtd.getText().toString().trim());
        params.put("valor_unitario", valor_unitario.getText().toString().trim());
        params.put("desconto", desconto.getText().toString().trim());
        params.put("vencimento", vencimento.getText().toString().trim());
        params.put("cond_pagamento", cond_pagamento.getSelectedItem().toString());
        params.put("forma_pagamento", forma_pagamento.getSelectedItem().toString());
        params.put("obs", obs.getText().toString().trim());
        params.put("id_cliente", idCliente);
        params.put("id_produto", idProduto);


        StringRequest stringRequest = CRUD.editar("https://limopestoques.com.br/Android/Update/updateProdVenda.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(EditarProdVenda.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(EditarProdVenda.this, Produtos_Vendas.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(EditarProdVenda.this);
        requestQueue.add(stringRequest);
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

                                ClientesConst forneCompra = new ClientesConst(forncompraObject.getString("id_cliente"), forncompraObject.getString("nome_cliente"), forncompraObject.getString("tipo"), forncompraObject.getString("email"));

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

    public void carregarProduto(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://limopestoques.com.br/Android/Json/jsonProd.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray forncompraArray = obj.getJSONArray("produtos");

                            for (int i = 0; i < forncompraArray.length(); i++){
                                JSONObject forncompraObject = forncompraArray.getJSONObject(i);

                                ProdCompraConst prodCompra = new ProdCompraConst(forncompraObject.getString("id_produto"), forncompraObject.getString("nome"), forncompraObject.getString("valor_venda"), forncompraObject.getString("disponivel_estoque"));

                                produtos.add(prodCompra);
                                adapter2.add(prodCompra.getProd());
                            }
                            produto.setAdapter(adapter2);
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

        RequestQueue rq2 = Volley.newRequestQueue(this);
        rq2.add(stringRequest);
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(EditarProdVenda.this, Produtos_Vendas.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}

