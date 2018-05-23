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

public class EditarOS extends AppCompatActivity {

    private String id;

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Update/updateOS.php";

    EditText cliente, stts, data_inicio, previsao_entrega, equipamento_recebido,
            n_serie, marca, modelo, obs_equipamento, descricao_defeito,
            descricao_servico, obs_interna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

                            cliente.setText(jo.getString("cliente"));
                            stts.setText(jo.getString("status"));
                            equipamento_recebido.setText(jo.getString("eqp_recebido"));
                            n_serie.setText(jo.getString("n_serie"));
                            marca.setText(jo.getString("marca"));
                            modelo.setText(jo.getString("modelo"));
                            obs_equipamento.setText(jo.getString("obs_recebimento_eqp"));
                            descricao_defeito.setText(jo.getString("descricao_problema"));
                            descricao_servico.setText(jo.getString("descricao_servico"));
                            obs_interna.setText(jo.getString("obs_internas"));
                            data_inicio.setText(jo.getString("data_inicio"));

                            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                            ParsePosition pos = new ParsePosition(0);
                            Date data = formato.parse(jo.getString("previsao_entrega"),pos);
                            formato = new SimpleDateFormat("dd/MM/yyyy");
                            String date = formato.format(data);

                            previsao_entrega.setText(date);

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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /*public void updateOS(){
        Map<String, String> params = new HashMap<String, String>();

        params.put("update", "update");
        params.put("id_os", id);
        params.put("cliente", cliente.getText().toString().trim());
        params.put("status", stts.getText().toString().trim());
        params.put("previsao_entrega", previsao_entrega.getText().toString().trim());
        params.put("eqp_recebido", equipamento_recebido.getText().toString().trim());
        params.put("n_serie", n_serie.getText().toString().trim());
        params.put("marca", marca.getText().toString().trim());
        params.put("modelo", modelo.getText().toString().trim());
        params.put("obs_recebimento_eqp", obs_equipamento.getText().toString().trim());
        params.put("descricao_problema", descricao_defeito.getText().toString().trim());
        params.put("descricao_servico", descricao_servico.getText().toString().trim());
        params.put("obs_interna", obs_interna.getText().toString().trim());


        StringRequest stringRequest = CRUD.editar("https://limopestoques.com.br/Android/Update/updateServico.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(EditarOS.this, "", Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(EditarOS.this, Servicos_Compras.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(EditarOS.this);
        requestQueue.add(stringRequest);

    }*/

    public void validarCampos(View v){
        if(cliente.getText().length() == 0 || stts.getText().length() == 0 || previsao_entrega.getText().length() == 0 ||
                equipamento_recebido.getText().length() == 0 || n_serie.getText().length() == 0 || marca.getText().length() == 0 ||
                modelo.getText().length() == 0 || obs_equipamento.getText().length() == 0 || descricao_defeito.getText().length() == 0 ||
                descricao_servico.getText().length() == 0 || obs_interna.getText().length() == 0) {
            Toast.makeText(this, "Preencha os campos corretamente!",Toast.LENGTH_SHORT).show();
        }else{
        }
    }
}
