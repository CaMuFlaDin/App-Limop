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
import br.com.limopestoques.limop.Construtoras.ServicosCompraConst;

public class EditarServVenda extends AppCompatActivity {

    private String idCliente;
    private String idServico;
    private String id;

    EditText data_venda,qtd, valor_unitario,desconto,vencimento,obs;
    Spinner nome_cliente,servico,stts_negociacao,contrato,cond_pagamento,forma_pagamento;
    Button button;

    ArrayAdapter<String> adapter;
    ArrayList<ClientesConst> clientes;

    ArrayAdapter<String> adapter2;
    ArrayList<ServicosCompraConst> servicos;

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Update/updateServVenda.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_serv_venda);

        nome_cliente              = findViewById(R.id.cliente);
        servico                   = findViewById(R.id.servico);
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
        servicos = new ArrayList<ServicosCompraConst>();
        carregarServico();

        nome_cliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idCliente = clientes.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        servico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idServico = servicos.get(i).getId();
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

                            qtd.setText(jo.getString("quantidade"));
                            valor_unitario.setText(jo.getString("valor"));
                            desconto.setText(jo.getString("desconto"));
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

    public void validarCampos(View v){
        if(data_venda.getText().length() == 0 || qtd.getText().length() == 0 || valor_unitario.getText().length() == 0
                || desconto.getText().length() == 0  || vencimento.getText().length() == 0) {
            Toast.makeText(this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();
        }else{
            updatevenda();
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

        params.put("update", "update");
        params.put("id_venda", id);
        params.put("data_venda", date);
        params.put("status", stts_negociacao.getSelectedItem().toString());
        params.put("contrato", contrato.getSelectedItem().toString());
        params.put("qtd", qtd.getText().toString().trim());
        params.put("valor_unitario", valor_unitario.getText().toString().trim());
        params.put("desconto", desconto.getText().toString().trim());
        params.put("vencimento", vencimento);
        params.put("cond_pagamento", cond_pagamento.getSelectedItem().toString());
        params.put("forma_pagamento", forma_pagamento.getSelectedItem().toString());
        params.put("obs", obs.getText().toString().trim());
        params.put("id_cliente", idCliente);
        params.put("id_servico", idServico);


        StringRequest stringRequest = CRUD.editar("https://limopestoques.com.br/Android/Update/updateServVenda.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(EditarServVenda.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(EditarServVenda.this, Servicos_Vendas.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(EditarServVenda.this);
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

    public void carregarServico(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://limopestoques.com.br/Android/Json/jsonServicos.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray forncompraArray = obj.getJSONArray("servicos");

                            for (int i = 0; i < forncompraArray.length(); i++){
                                JSONObject forncompraObject = forncompraArray.getJSONObject(i);

                                ServicosCompraConst servCompra = new ServicosCompraConst(forncompraObject.getString("id_servico"), forncompraObject.getString("nome_servico"), forncompraObject.getString("valor_custo"), forncompraObject.getString("valor_venda"));

                                servicos.add(servCompra);
                                adapter2.add(servCompra.getNome());
                            }
                            servico.setAdapter(adapter2);
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
        Intent irTela = new Intent(EditarServVenda.this, Servicos_Vendas.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}

