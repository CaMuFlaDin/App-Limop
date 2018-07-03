package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import br.com.limopestoques.limop.CRUD.CRUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditarServico extends AppCompatActivity {

    private String id;

    //Json
    private static final String JSON_URL = "https://limopestoques.com.br/Android/Update/updateServico.php";

    EditText nome;
    EditText valor_custo;
    EditText valor_venda;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_servico);

        nome        = findViewById(R.id.nome);
        valor_custo = findViewById(R.id.valor_custo);
        valor_venda = findViewById(R.id.valor_venda);
        btn         = findViewById(R.id.button);

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

                            //Nome Json
                            JSONArray servicocompraArray = obj.getJSONArray("servicos");
                            JSONObject jo = servicocompraArray.getJSONObject(0);

                            //Inserir dados no EditText
                            nome.setText(jo.getString("nome_servico"));
                            valor_custo.setText(jo.getString("valor_custo"));
                            valor_venda.setText(jo.getString("valor_venda"));

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
                params.put("id_servico", id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void validarCampos(View v){
        if(nome.getText().length() == 0 || valor_custo.getText().length() == 0 || valor_venda.getText().length() == 0) {
            Toast.makeText(this, getString(R.string.preenchaoscamposcorretamente),Toast.LENGTH_SHORT).show();
        }else{
            updateServico();
        }
    }

    public void updateServico() {
        Map<String, String> params = new HashMap<String, String>();

        //Enviar dados para o Update
        params.put("update", "update");
        params.put("id_servico", id);
        params.put("nome", nome.getText().toString().trim());
        params.put("valor_custo", valor_custo.getText().toString().trim());
        params.put("valor_venda", valor_venda.getText().toString().trim());


        StringRequest stringRequest = CRUD.editar("https://limopestoques.com.br/Android/Update/updateServico.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(EditarServico.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(EditarServico.this, Servicos_Compras.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(EditarServico.this);
        requestQueue.add(stringRequest);

    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(EditarServico.this, Servicos_Compras.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
