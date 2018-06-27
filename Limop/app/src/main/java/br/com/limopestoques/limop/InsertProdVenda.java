package br.com.limopestoques.limop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import br.com.limopestoques.limop.CRUD.CRUD;
import br.com.limopestoques.limop.Construtoras.ClientesConst;
import br.com.limopestoques.limop.Construtoras.ProdCompraConst;
import br.com.limopestoques.limop.Sessao.Sessao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class InsertProdVenda extends AppCompatActivity {

    private String idCliente;
    private String idProduto;

    EditText data_venda,qtd, valor_unitario,desconto,vencimento,obs;
    Spinner nome_cliente,produto,stts_negociacao,contrato,cond_pagamento,forma_pagamento;
    Button button;
    Sessao sessao;

    ArrayAdapter<String> adapter;
    ArrayList<ClientesConst> clientes;

    ArrayAdapter<String> adapter2;
    ArrayList<ProdCompraConst> produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_prod_venda);

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
        forma_pagamento           = findViewById(R.id.forma_pagamento);

        valor_unitario.setEnabled(false);

        SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);

        data_venda.setEnabled(false);
        data_venda.setText(dataFormatada);


        sessao = new Sessao(InsertProdVenda.this);

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

        MaskEditTextChangedListener maskData = new MaskEditTextChangedListener("##/##/####",data_venda);
        MaskEditTextChangedListener maskDataVenc = new MaskEditTextChangedListener("##/##/####",vencimento);
        data_venda.addTextChangedListener(maskData);
        vencimento.addTextChangedListener(maskDataVenc);

    }

    public void validarCampos(View v){
        if(data_venda.getText().length() == 0 || qtd.getText().length() == 0 || valor_unitario.getText().length() == 0
                || desconto.getText().length() == 0  || vencimento.getText().length() == 0) {
                    Toast.makeText(this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();
        }else{
            Double Desconto = Double.parseDouble(desconto.getText().toString());
            if(Desconto > 100){
                Toast.makeText(this, "Porcentagem inválida!", Toast.LENGTH_SHORT).show();
            }else{
                insertProduto();
            }
        }
    }

    public void insertProduto() {
        Map<String, String> params = new HashMap<String, String>();

        String id_usuario = sessao.getString("id_usuario");

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

        params.put("data_venda", date);
        params.put("status", stts_negociacao.getSelectedItem().toString());
        params.put("contrato", contrato.getSelectedItem().toString());
        params.put("qtd", qtd.getText().toString().trim());
        params.put("valor_unitario", valor_unitario.getText().toString().trim());
        params.put("desconto", desconto.getText().toString().trim());
        params.put("vencimento", vencimento);
        params.put("obs", obs.getText().toString().trim());
        params.put("cond_pagamento", cond_pagamento.getSelectedItem().toString());
        params.put("forma_pagamento", forma_pagamento.getSelectedItem().toString());
        params.put("id_cliente", idCliente);
        params.put("id_produto", idProduto);
        params.put("id_usuario", id_usuario);

        CRUD.inserir("https://limopestoques.com.br/Android/Insert/InsertVendaProd.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(InsertProdVenda.this, resposta, Toast.LENGTH_SHORT).show();
                    if(resposta.equals("Cadastrado com sucesso!")){
                        Intent irTela = new Intent(InsertProdVenda.this, Produtos_Vendas.class);
                        startActivity(irTela);
                    }
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

                                ProdCompraConst prodCompra = new ProdCompraConst(forncompraObject.getString("id_produto"), forncompraObject.getString("nome"), forncompraObject.getString("valor_venda"), forncompraObject.getString("disponivel_estoque"),forncompraObject.getString("fotos"));

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
        Intent irTela = new Intent(InsertProdVenda.this, Produtos_Vendas.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
