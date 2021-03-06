package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
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
import java.util.List;
import java.util.Map;

import br.com.limopestoques.limop.Construtoras.FornCompraConst;
import br.com.limopestoques.limop.Construtoras.HistoricoConst;
import br.com.limopestoques.limop.ListView.ListViewFornCompra;
import br.com.limopestoques.limop.ListView.ListViewHistorico;

public class HistoricoMovimentacao extends AppCompatActivity {

    private String id;

    ListView listView;

    //Json
    private static final String JSON_URL = "https://limopestoques.com.br/Android/Json/jsonHistorico.php";

    List<HistoricoConst> historicoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_movimentacao);

        listView = (ListView)findViewById(R.id.listView);
        historicoList = new ArrayList<>();

        registerForContextMenu(listView);
        listView.setTextFilterEnabled(true);

        try{
            Intent i = getIntent();
            this.id = i.getStringExtra("id");
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        carregarEntradas();
        carregarSaidas();


    }

    public void carregarEntradas(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            //Nome Json
                            JSONArray forncompraArray = obj.getJSONArray("entradas");

                            for (int i = 0; i < forncompraArray.length(); i++){
                                JSONObject forncompraObject = forncompraArray.getJSONObject(i);

                                //Recuperar dados
                                HistoricoConst produto = new HistoricoConst(forncompraObject.getString("data"), "Fornecedor:"+forncompraObject.getString("nome_fornecedor"), "Quantidade movimentada:"+forncompraObject.getString("qtd"), "Valor:"+forncompraObject.getString("valor_venda"),"Entrada");

                                historicoList.add(produto);
                            }

                            ListViewHistorico adapter = new ListViewHistorico(historicoList, getApplicationContext());

                            listView.setAdapter(adapter);

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
                params.put("id_produto", id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void carregarSaidas(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray forncompraArray = obj.getJSONArray("saidas");

                            for (int i = 0; i < forncompraArray.length(); i++){
                                JSONObject forncompraObject = forncompraArray.getJSONObject(i);

                                HistoricoConst produto = new HistoricoConst(forncompraObject.getString("data_venda"), "Cliente:"+forncompraObject.getString("nome_cliente"), "Quantidade movimentada:"+forncompraObject.getString("quantidade"), "Valor:"+forncompraObject.getString("valor"),"Saída");

                                historicoList.add(produto);
                            }

                            ListViewHistorico adapter = new ListViewHistorico(historicoList, getApplicationContext());

                            listView.setAdapter(adapter);

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
                params.put("id_produto", id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(HistoricoMovimentacao.this, SituacaoEstoque.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }

    }

