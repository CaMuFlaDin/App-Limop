package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import br.com.limopestoques.limop.Construtoras.ProdCompraConst;

public class EditarOS extends AppCompatActivity {

    private String id;

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Update/updateOS.php";

    EditText data_inicio, previsao_entrega,
            n_serie, marca, modelo, obs_equipamento, descricao_defeito,
            descricao_servico, obs_interna;

    Spinner cliente, equipamento_recebido,stts;

    Integer posCliente;
    Integer posProduto;

    private String idCliente;
    private String idProduto;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    ArrayList<ClientesConst> clientes;
    ArrayList<ProdCompraConst> produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_os);

        cliente = findViewById(R.id.cliente);
        stts = findViewById(R.id.stts);
        data_inicio = findViewById(R.id.data_inicio);
        previsao_entrega = findViewById(R.id.previsao_entrega);
        equipamento_recebido = findViewById(R.id.equipamento_recebido);
        n_serie = findViewById(R.id.n_serie);
        marca = findViewById(R.id.marca);
        modelo = findViewById(R.id.modelo);
        obs_equipamento = findViewById(R.id.obs_equipamento);
        descricao_defeito = findViewById(R.id.descricao_defeito);
        descricao_servico = findViewById(R.id.descricao_servico);
        obs_interna = findViewById(R.id.obs_interna);

        try{
            Intent i = getIntent();
            this.id = i.getStringExtra("id_os");
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        MaskEditTextChangedListener maskData = new MaskEditTextChangedListener("##/##/####",previsao_entrega);
        previsao_entrega.addTextChangedListener(maskData);

        MaskEditTextChangedListener maskDataa = new MaskEditTextChangedListener("##/##/####",data_inicio);
        data_inicio.addTextChangedListener(maskDataa);

        carregar();

        adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        clientes = new ArrayList<ClientesConst>();

        adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        produtos = new ArrayList<ProdCompraConst>();

        cliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idCliente = clientes.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        equipamento_recebido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idProduto = produtos.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void carregar(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray osArray = obj.getJSONArray("os");
                            JSONObject jo = osArray.getJSONObject(0);

                            n_serie.setText(jo.getString("n_serie"));
                            marca.setText(jo.getString("marca"));
                            modelo.setText(jo.getString("modelo"));
                            obs_equipamento.setText(jo.getString("obs_recebimento_eqp"));
                            descricao_defeito.setText(jo.getString("descricao_problema"));
                            descricao_servico.setText(jo.getString("descricao_servico"));
                            obs_interna.setText(jo.getString("obs_internas"));
                            String sttsOS = jo.getString("status");

                            idCliente = jo.getString("cliente");
                            idProduto = jo.getString("eqp_recebido");

                            if(sttsOS.equals("Orçamento Pendente")){
                                stts.setSelection(0);
                            }else if(sttsOS.equals("Serviço Pendente")){
                                stts.setSelection(1);
                            }else if(sttsOS.equals("Em Andamento")){
                                stts.setSelection(2);
                            }else if(sttsOS.equals("Concluído")){
                                stts.setSelection(3);
                            }else{
                                stts.setSelection(4);
                            }

                            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                            ParsePosition pos = new ParsePosition(0);
                            Date data = formato.parse(jo.getString("previsao_entrega"),pos);
                            formato = new SimpleDateFormat("dd/MM/yyyy");
                            String date = formato.format(data);

                            previsao_entrega.setText(date);

                            SimpleDateFormat formato2 = new SimpleDateFormat("yyyy-MM-dd");
                            ParsePosition pos2 = new ParsePosition(0);
                            Date data2 = formato2.parse(jo.getString("data_inicio"),pos2);
                            formato2 = new SimpleDateFormat("dd/MM/yyyy");
                            String date2 = formato2.format(data2);

                            data_inicio.setText(date2);

                        }catch (JSONException e) {
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
                params.put("id_os", id);

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
                            try {
                                JSONObject obj = new JSONObject(response);

                                JSONArray forncompraArray = obj.getJSONArray("clientes");

                                for (int i = 0; i < forncompraArray.length(); i++) {
                                    JSONObject forncompraObject = forncompraArray.getJSONObject(i);

                                    ClientesConst forneCompra = new ClientesConst(forncompraObject.getString("id_cliente"), forncompraObject.getString("nome_cliente"), forncompraObject.getString("tipo"), forncompraObject.getString("email"));

                                    clientes.add(forneCompra);
                                    adapter.add(forneCompra.getNome());

                                    if (idCliente.equals(forneCompra.getId())) {
                                        posCliente = adapter.getPosition(forneCompra.getNome());
                                    }
                                }
                                cliente.setAdapter(adapter);
                                cliente.setSelection(posCliente);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
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
                            equipamento_recebido.setAdapter(adapter2);
                            equipamento_recebido.setSelection(posProduto);
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


    public void updateOS(){
        Map<String, String> params = new HashMap<String, String>();

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        ParsePosition pos = new ParsePosition(0);
        Date data = formato.parse(previsao_entrega.getText().toString(),pos);
        formato = new SimpleDateFormat("yyyy-MM-dd");
        String date = formato.format(data);

        SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
        ParsePosition pos2 = new ParsePosition(0);
        Date data2 = formato2.parse(data_inicio.getText().toString(),pos2);
        formato2 = new SimpleDateFormat("yyyy-MM-dd");
        String date2 = formato2.format(data2);

        params.put("update", "update");
        params.put("id_os", id);
        idCliente = clientes.get(cliente.getSelectedItemPosition()).getId();
        params.put("cliente", idCliente);
        params.put("status", stts.getSelectedItem().toString());
        params.put("previsao_entrega", date);
        params.put("data_inicio", date2);
        idProduto = produtos.get(equipamento_recebido.getSelectedItemPosition()).getId();
        params.put("eqp_recebido",idProduto);
        params.put("n_serie", n_serie.getText().toString().trim());
        params.put("marca", marca.getText().toString().trim());
        params.put("modelo", modelo.getText().toString().trim());
        params.put("obs_recebimento_eqp", obs_equipamento.getText().toString().trim());
        params.put("descricao_problema", descricao_defeito.getText().toString().trim());
        params.put("descricao_servico", descricao_servico.getText().toString().trim());
        params.put("obs_interna", obs_interna.getText().toString().trim());


        StringRequest stringRequest = CRUD.editar("https://limopestoques.com.br/Android/Update/updateOS.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(EditarOS.this, getString(R.string.editadocomsucesso), Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(EditarOS.this, OrdemServico.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(EditarOS.this);
        requestQueue.add(stringRequest);

    }

    public void validarCampos(View v){
        if(previsao_entrega.getText().length() == 0 ||
                 n_serie.getText().length() == 0 || marca.getText().length() == 0 ||
                obs_equipamento.getText().length() == 0 || descricao_defeito.getText().length() == 0 ||
                descricao_servico.getText().length() == 0 || data_inicio.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.preenchaoscamposcorretamente),Toast.LENGTH_SHORT).show();
        }else{
            updateOS();
        }
    }
}
