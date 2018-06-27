package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
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

    Integer posCliente;
    Integer posProduto;

    ArrayAdapter<String> adapter;
    ArrayList<ClientesConst> clientes;

    ArrayAdapter<String> adapter2;
    ArrayList<ProdCompraConst> produtos;

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Update/updateProdVenda.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

        valor_unitario.setEnabled(false);


        button                    = (Button)findViewById(R.id.button);

        adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        clientes = new ArrayList<ClientesConst>();

        adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        produtos = new ArrayList<ProdCompraConst>();

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

                Map<String, String> params = new HashMap<String, String>();

                params.put("qtd", "qtd");
                params.put("idProduto", idProduto);

                CRUD.inserir("https://limopestoques.com.br/Android/puxarValorProd.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){
                        try{
                            JSONObject jo = new JSONObject(response);
                            String qtd = jo.getString("qtd");
                            valor_unitario.setText(qtd);
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },params,getApplicationContext());
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

        MaskEditTextChangedListener maskData = new MaskEditTextChangedListener("##/##/####",data_venda);
        MaskEditTextChangedListener maskDataVenc = new MaskEditTextChangedListener("##/##/####",vencimento);
        data_venda.addTextChangedListener(maskData);
        vencimento.addTextChangedListener(maskDataVenc);
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

                            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                            ParsePosition pos = new ParsePosition(0);
                            Date data = formato.parse(jo.getString("data_venda"),pos);
                            formato = new SimpleDateFormat("dd/MM/yyyy");
                            String date = formato.format(data);

                            data_venda.setText(date);

                            qtd.setText(jo.getString("quantidade"));
                            valor_unitario.setText(jo.getString("valor"));
                            desconto.setText(jo.getString("desconto"));


                            SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
                            ParsePosition pos2 = new ParsePosition(0);
                            Date data2 = formato2.parse(jo.getString("vencimento"),pos2);
                            formato2 = new SimpleDateFormat("dd/MM/yyyy");
                            String date2 = formato2.format(data2);

                            vencimento.setText(date2);

                            idCliente = jo.getString("id_cliente");
                            idProduto = jo.getString("id_produto");

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

                            if(condicao.equals("1")){
                                cond_pagamento.setSelection(0);
                            }else if(condicao.equals("2")){
                                cond_pagamento.setSelection(1);
                            }else if(condicao.equals("3")){
                                cond_pagamento.setSelection(2);
                            }else if(condicao.equals("4")){
                                cond_pagamento.setSelection(3);
                            }else if(condicao.equals("5")){
                                cond_pagamento.setSelection(4);
                            }else if(condicao.equals("6")){
                                cond_pagamento.setSelection(5);
                            }else if(condicao.equals("7")){
                                cond_pagamento.setSelection(6);
                            }else if(condicao.equals("8")){
                                cond_pagamento.setSelection(7);
                            }else if(condicao.equals("9")){
                                cond_pagamento.setSelection(8);
                            }else if(condicao.equals("10")){
                                cond_pagamento.setSelection(9);
                            }else if(condicao.equals("11")){
                                cond_pagamento.setSelection(10);
                            }else if(condicao.equals("12")){
                                cond_pagamento.setSelection(11);
                            }else if(condicao.equals("13")){
                                cond_pagamento.setSelection(12);
                            }else if(condicao.equals("14")){
                                cond_pagamento.setSelection(13);
                            }else if(condicao.equals("15")){
                                cond_pagamento.setSelection(14);
                            }else if(condicao.equals("16")){
                                cond_pagamento.setSelection(15);
                            }else if(condicao.equals("17")){
                                cond_pagamento.setSelection(16);
                            }else if(condicao.equals("18")){
                                cond_pagamento.setSelection(17);
                            }else if(condicao.equals("19")){
                                cond_pagamento.setSelection(18);
                            }else if(condicao.equals("20")){
                                cond_pagamento.setSelection(19);
                            }else if(condicao.equals("21")){
                                cond_pagamento.setSelection(20);
                            }else if(condicao.equals("22")){
                                cond_pagamento.setSelection(21);
                            }else if(condicao.equals("23")){
                                cond_pagamento.setSelection(22);
                            }else if(condicao.equals("24")){
                                cond_pagamento.setSelection(23);
                            }else if(condicao.equals("25")){
                                cond_pagamento.setSelection(24);
                            }else if(condicao.equals("26")){
                                cond_pagamento.setSelection(25);
                            }else if(condicao.equals("27")){
                                cond_pagamento.setSelection(26);
                            }else if(condicao.equals("28")){
                                cond_pagamento.setSelection(27);
                            }else if(condicao.equals("29")){
                                cond_pagamento.setSelection(28);
                            }else if(condicao.equals("30")){
                                cond_pagamento.setSelection(29);
                            }else if(condicao.equals("31")){
                                cond_pagamento.setSelection(30);
                            }else if(condicao.equals("32")){
                                cond_pagamento.setSelection(31);
                            }else if(condicao.equals("33")){
                                cond_pagamento.setSelection(32);
                            }else if(condicao.equals("34")){
                                cond_pagamento.setSelection(33);
                            }else if(condicao.equals("35")){
                                cond_pagamento.setSelection(34);
                            }else if(condicao.equals("36")){
                                cond_pagamento.setSelection(35);
                            }else if(condicao.equals("37")){
                                cond_pagamento.setSelection(36);
                            }else if(condicao.equals("38")){
                                cond_pagamento.setSelection(37);
                            }else if(condicao.equals("39")){
                                cond_pagamento.setSelection(38);
                            }else if(condicao.equals("40")){
                                cond_pagamento.setSelection(39);
                            }else if(condicao.equals("41")){
                                cond_pagamento.setSelection(40);
                            }else if(condicao.equals("42")){
                                cond_pagamento.setSelection(41);
                            }else if(condicao.equals("43")){
                                cond_pagamento.setSelection(42);
                            }else if(condicao.equals("44")){
                                cond_pagamento.setSelection(43);
                            }else if(condicao.equals("45")){
                                cond_pagamento.setSelection(44);
                            }else if(condicao.equals("46")){
                                cond_pagamento.setSelection(45);
                            }else if(condicao.equals("47")){
                                cond_pagamento.setSelection(46);
                            }else if(condicao.equals("48")){
                                cond_pagamento.setSelection(47);
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
                            }else{
                                forma_pagamento.setSelection(2);
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
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.removeRequestFinishedListener(this);

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

                                if(idCliente.equals(forneCompra.getId())){
                                    posCliente = adapter.getPosition(forneCompra.getNome());
                                }
                            }
                            nome_cliente.setAdapter(adapter);
                            nome_cliente.setSelection(posCliente);
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

        requestQueue.add(stringRequest);

        stringRequest = new StringRequest(Request.Method.POST, "https://limopestoques.com.br/Android/Json/jsonProd.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray forncompraArray = obj.getJSONArray("produtos");

                            for (int i = 0; i < forncompraArray.length(); i++){
                                JSONObject forncompraObject = forncompraArray.getJSONObject(i);

                                ProdCompraConst prodCompra = new ProdCompraConst(forncompraObject.getString("id_produto"), forncompraObject.getString("nome"), forncompraObject.getString("valor_venda"), forncompraObject.getString("disponivel_estoque"),forncompraObject.getString("fotos"));

                                produtos.add(prodCompra);
                                adapter2.add(prodCompra.getProd());

                                if(idProduto.equals(prodCompra.getId())){
                                    posProduto = adapter2.getPosition(prodCompra.getProd());
                                }
                            }
                            produto.setAdapter(adapter2);
                            produto.setSelection(posProduto);
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

        requestQueue.add(stringRequest);
            }
        });
    }

    public void validarCampos(View v){
        if(data_venda.getText().length() == 0 || qtd.getText().length() == 0 || valor_unitario.getText().length() == 0
                || desconto.getText().length() == 0  || vencimento.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.preenchaoscamposcorretamente), Toast.LENGTH_SHORT).show();
        }else{
            Double Desconto = Double.parseDouble(desconto.getText().toString());
            if(Desconto > 100){
                Toast.makeText(this, "Porcentagem inválida!", Toast.LENGTH_SHORT).show();
            }else{
                updatevenda();
            }
        }
    }

    public void updatevenda() {
        Map<String, String> params = new HashMap<String, String>();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        ParsePosition pos = new ParsePosition(0);
        Date data = formato.parse(data_venda.getText().toString(),pos);
        formato = new SimpleDateFormat("yyyy-MM-dd");
        String date = formato.format(data);

        SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
        ParsePosition pos2 = new ParsePosition(0);
        Date data2 = formato2.parse(vencimento.getText().toString(),pos2);
        formato2 = new SimpleDateFormat("yyyy-MM-dd");
        String vencimento = formato2.format(data2);


        String CondicaoPagamento = "";

        for(int i=1;i<=48;i++){
            if(i != 1) {
                if (cond_pagamento.getSelectedItem().toString().equals(i + "x")) {
                    CondicaoPagamento = String.valueOf(i);
                }
            }else{
                CondicaoPagamento = "1";
            }
        }

        params.put("update", "update");
        params.put("id_venda", id);
        params.put("data_venda", date);
        params.put("status", stts_negociacao.getSelectedItem().toString());
        params.put("contrato", contrato.getSelectedItem().toString());
        params.put("qtd", qtd.getText().toString().trim());
        params.put("valor_unitario", valor_unitario.getText().toString().trim());
        params.put("desconto", desconto.getText().toString().trim());
        params.put("vencimento", vencimento);
        params.put("cond_pagamento", CondicaoPagamento);
        params.put("forma_pagamento", forma_pagamento.getSelectedItem().toString());
        params.put("obs", obs.getText().toString().trim());
        idCliente = clientes.get(nome_cliente.getSelectedItemPosition()).getId();
        params.put("id_cliente", idCliente);
        idProduto = produtos.get(produto.getSelectedItemPosition()).getId();
        params.put("id_produto", idProduto);


        StringRequest stringRequest = CRUD.editar("https://limopestoques.com.br/Android/Update/updateProdVenda.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(EditarProdVenda.this, resposta, Toast.LENGTH_SHORT).show();
                    if(resposta.equals("Editado com sucesso!")){
                        Intent irTela = new Intent(EditarProdVenda.this, Produtos_Vendas.class);
                        startActivity(irTela);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(EditarProdVenda.this);
        requestQueue.add(stringRequest);
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(EditarProdVenda.this, Produtos_Vendas.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}

