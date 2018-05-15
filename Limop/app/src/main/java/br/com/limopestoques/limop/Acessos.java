package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import br.com.limopestoques.limop.Construtoras.AcessosConst;
import br.com.limopestoques.limop.Construtoras.ClientesConst;
import br.com.limopestoques.limop.ListView.ListViewAcessos;
import br.com.limopestoques.limop.ListView.ListViewClientes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Acessos extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Json/jsonAcessos.php";

    ListView listView;

    List<AcessosConst> acessosList;
    List<AcessosConst> acessosQuery;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acessos);

        listView = (ListView)findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        acessosList = new ArrayList<>();
        acessosQuery = new ArrayList<>();
        loadAcessosList();
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);
    }

    private void loadAcessosList(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray acessosArray = obj.getJSONArray("acessos");

                            for (int i = 0; i < acessosArray.length(); i++){
                                JSONObject acessosObject = acessosArray.getJSONObject(i);

                                AcessosConst users = new AcessosConst(acessosObject.getString("nome_usuario"), acessosObject.getString("qtd_acessos")+" Acessos", acessosObject.getString("data_acesso"));

                                acessosList.add(users);
                                acessosQuery.add(users);
                            }

                            ListViewAcessos adapter = new ListViewAcessos(acessosList, getApplicationContext());

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
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onQueryTextChange(String newText){
        acessosQuery.clear();
        if (TextUtils.isEmpty(newText)) {
            acessosQuery.addAll(acessosList);
        } else {
            String queryText = newText.toLowerCase();
            for(AcessosConst u : acessosList){
                if(u.getUser().toLowerCase().contains(queryText)){
                    acessosQuery.add(u);
                }
            }
        }
        listView.setAdapter(new ListViewAcessos(acessosQuery, Acessos.this));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        return false;
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(Acessos.this, Principal.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
