package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import br.com.limopestoques.limop.CRUD.CRUD;
import br.com.limopestoques.limop.Sessao.Sessao;

public class InsertOS extends AppCompatActivity {

    private String id;
    private String tipoVenda;

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Insert/InsertOS.php";

    EditText cliente, stts, data_inicio, previsao_entrega, equipamento_recebido,
            n_serie, marca, modelo, obs_equipamento, descricao_defeito,
            descricao_servico, obs_interna;

    Sessao sessao;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        try{
            Intent i = getIntent();
            this.id = i.getStringExtra("id_venda");
            this.tipoVenda = i.getStringExtra("tipo");
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

                            JSONArray osArray = obj.getJSONArray("vendas");
                            JSONObject jo = osArray.getJSONObject(0);

                            cliente.setText(jo.getString("nome_cliente"));
                            stts.setText(jo.getString("status_negociacao"));
                            equipamento_recebido.setText(jo.getString("eqp"));

                            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                            ParsePosition pos = new ParsePosition(0);
                            Date data = formato.parse(jo.getString("data_venda"),pos);
                            formato = new SimpleDateFormat("dd/MM/yyyy");
                            String date = formato.format(data);

                            data_inicio.setText(date);

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
                params.put("tipo", tipoVenda);
                params.put("id_venda", id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void validarCampos(View v){

        if(previsao_entrega.getText().length() == 0 || n_serie.getText().length() == 0 || marca.getText().length() == 0 ||
            modelo.getText().length() == 0 || obs_equipamento.getText().length() == 0 || descricao_defeito.getText().length() == 0 ||
            descricao_servico.toString().length() == 0){
            Toast.makeText(this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();
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

        params.put("insert", "insert");
        params.put("cliente", cliente.getText().toString().trim());
        params.put("status", stts.getText().toString());
        params.put("data_venda", data_inicio.getText().toString().trim());
        params.put("previsao", date);
        params.put("produto", equipamento_recebido.getText().toString().trim());
        params.put("n_serie", n_serie.getText().toString().trim());
        params.put("marca", marca.getText().toString().trim());
        params.put("modelo", modelo.getText().toString().trim());
        params.put("obs_eqp", obs_equipamento.getText().toString().trim());
        params.put("desc_problema", descricao_defeito.getText().toString().trim());
        params.put("desc_servico", descricao_servico.getText().toString().trim());
        params.put("obs_internas", obs_interna.getText().toString().trim());
        params.put("id_venda", id);
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
