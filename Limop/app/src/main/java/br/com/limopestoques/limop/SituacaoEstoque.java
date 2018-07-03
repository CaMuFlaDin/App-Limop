package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
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

import br.com.limopestoques.limop.Construtoras.ProdCompraConst;
import br.com.limopestoques.limop.Construtoras.ProdVendaConst;
import br.com.limopestoques.limop.ListView.ListViewProdCompra;
import br.com.limopestoques.limop.ListView.ListViewSituacao;

public class SituacaoEstoque extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Json/jsonProd.php";

    ListView listView;

    List<ProdCompraConst> prodcompraList;
    List<ProdCompraConst> prodQuery;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacao_estoque);

        listView = (ListView)findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        prodcompraList = new ArrayList<>();
        prodQuery = new ArrayList<>();

        registerForContextMenu(listView);
        loadProdCompraList();
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProdCompraConst vendas = prodQuery.get(i);
                final String id = vendas.getId();
                Intent irTela = new Intent(SituacaoEstoque.this, HistoricoMovimentacao.class);
                irTela.putExtra("id", id);
                startActivity(irTela);
            }
        });
    }

    private void loadProdCompraList(){
        prodcompraList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            //Nome Json
                            JSONArray prodcompraArray = obj.getJSONArray("produtos");

                            for (int i = 0; i < prodcompraArray.length(); i++){
                                JSONObject produtoObject = prodcompraArray.getJSONObject(i);

                                //Recuperar dados
                                ProdCompraConst prodCompra = new ProdCompraConst(produtoObject.getString("id_produto"),produtoObject.getString("nome"),"R$ "+produtoObject.getString("valor_venda"), produtoObject.getString("disponivel_estoque")+" Disponíveis no estoque",produtoObject.getString("fotos"));

                                prodcompraList.add(prodCompra);
                                prodQuery.add(prodCompra);
                            }

                            ListViewProdCompra adapter = new ListViewProdCompra(prodcompraList, getApplicationContext());

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

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void Inventario(View v){
        Intent i = new Intent(SituacaoEstoque.this, Inventario.class);
        startActivity(i);
    }

    @Override
    public boolean onQueryTextChange(String newText){
        prodQuery.clear();
        if (TextUtils.isEmpty(newText)) {
            prodQuery.addAll(prodcompraList);
        } else {
            String queryText = newText.toLowerCase();
            for(ProdCompraConst u : prodcompraList){
                if(u.getProd().toLowerCase().contains(queryText)){
                    prodQuery.add(u);
                }
            }
        }
        listView.setAdapter(new ListViewSituacao(prodQuery, SituacaoEstoque.this));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        return false;
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(SituacaoEstoque.this, Principal.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}

