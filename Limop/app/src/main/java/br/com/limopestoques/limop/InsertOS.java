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
import br.com.limopestoques.limop.Construtoras.FornCompraConst;
import br.com.limopestoques.limop.Construtoras.ProdCompraConst;
import br.com.limopestoques.limop.Sessao.Sessao;

public class InsertOS extends AppCompatActivity {

    private String id;
    private String tipoVenda;
    private String idCliente;
    private String idProduto;

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Insert/InsertOS.php";

    EditText data_inicio, previsao_entrega,
            n_serie, marca, modelo, obs_equipamento, descricao_defeito,
            descricao_servico, obs_interna;

    Spinner cliente,stts,equipamento_recebido;

    Sessao sessao;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    ArrayList<ClientesConst> clientes;
    ArrayList<ProdCompraConst> produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_os);

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

        sessao = new Sessao(InsertOS.this);

        MaskEditTextChangedListener maskData = new MaskEditTextChangedListener("##/##/####",previsao_entrega);
        previsao_entrega.addTextChangedListener(maskData);

        MaskEditTextChangedListener maskDataInicio = new MaskEditTextChangedListener("##/##/####",data_inicio);
        data_inicio.addTextChangedListener(maskDataInicio);

        try{
            Intent i = getIntent();
            this.id = i.getStringExtra("id_venda");
            this.tipoVenda = i.getStringExtra("tipo");
        }catch(NullPointerException e){
            e.printStackTrace();
        }


        adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        clientes = new ArrayList<ClientesConst>();
        carregarCliente();

        adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        produtos = new ArrayList<ProdCompraConst>();
        carregarProduto();

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

        carregarCliente();
        carregarProduto();

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
                            cliente.setAdapter(adapter);
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
                            equipamento_recebido.setAdapter(adapter2);
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

    public void validarCampos(View v){

        if(previsao_entrega.getText().length() == 0 || data_inicio.getText().length() == 0 || n_serie.getText().length() == 0 || marca.getText().length() == 0 ||
            modelo.getText().length() == 0 || obs_equipamento.getText().length() == 0 || descricao_defeito.getText().length() == 0 ||
            descricao_servico.toString().length() == 0){
        }else{
            insertOS();
        }

    }

    public void insertOS() {
        Map<String, String> params = new HashMap<String, String>();

        String id_usuario = sessao.getString("id_usuario");

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

        params.put("insert", "insert");
        params.put("cliente", idCliente);
        params.put("status", stts.getSelectedItem().toString());
        params.put("data_venda", date2);
        params.put("previsao", date);
        params.put("produto", idProduto);
        params.put("n_serie", n_serie.getText().toString().trim());
        params.put("marca", marca.getText().toString().trim());
        params.put("modelo", modelo.getText().toString().trim());
        params.put("obs_eqp", obs_equipamento.getText().toString().trim());
        params.put("desc_problema", descricao_defeito.getText().toString().trim());
        params.put("desc_servico", descricao_servico.getText().toString().trim());
        params.put("obs_internas", obs_interna.getText().toString().trim());
        params.put("id_usuario", id_usuario);

        CRUD.inserir("https://limopestoques.com.br/Android/Insert/InsertOS.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(InsertOS.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(InsertOS.this, OrdemServico.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());

    }
}
