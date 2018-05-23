package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class InsertOS extends AppCompatActivity {

    private String id;

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Insert/InsertOS.php";

    EditText cliente, stts, data_inicio, previsao_entrega, equipamento_recebido,
            n_serie, marca, modelo, obs_equipamento, descricao_defeito,
            descricao_servico, obs_interna;

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

        try{
            Intent i = getIntent();
            this.id = i.getStringExtra("id_os");
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

                            JSONArray osArray = obj.getJSONArray("os");
                            JSONObject jo = osArray.getJSONObject(0);

                            cliente.setText(jo.getString("cliente"));
                            stts.setText(jo.getString("status"));
                            equipamento_recebido.setText(jo.getString("eqp_recebido"));

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
}
