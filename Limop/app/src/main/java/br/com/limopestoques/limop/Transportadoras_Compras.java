package br.com.limopestoques.limop;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
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
import br.com.limopestoques.limop.CRUD.CRUD;
import br.com.limopestoques.limop.Construtoras.TranspCompraConst;
import br.com.limopestoques.limop.ListView.ListViewClientes;
import br.com.limopestoques.limop.ListView.ListViewTranspCompra;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Transportadoras_Compras extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Json/jsonTransportadoras.php";

    ListView listView;

    List<TranspCompraConst> transpcompraList;
    List<TranspCompraConst> transpQuery;

    SearchView searchView;
    Class tela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportadoras__compras);

        Intent recuperar = getIntent();
        try{
            String anterior = recuperar.getStringExtra("tela");
            tela = Class.forName(anterior);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        listView = (ListView)findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        transpcompraList = new ArrayList<>();
        transpQuery = new ArrayList<>();
        registerForContextMenu(listView);
        loadTranspCompraList();
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);
    }

    public void insertTransportadora(View v){
        Intent irTela = new Intent(Transportadoras_Compras.this, InsertTransportadora.class);
        startActivity(irTela);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.acoes);
        menu.add(0,v.getId(),0,"Editar Transportadora");
        menu.add(0,v.getId(),0,"Excluir Transportadora");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Integer pos = info.position;
        TranspCompraConst transp = transpQuery.get(pos);
        final String id = transp.getId();
        if(item.getTitle() == "Editar Transportadora"){
            Intent irTela = new Intent(Transportadoras_Compras.this, EditarTransportadora.class);
            irTela.putExtra("id",id);
            startActivity(irTela);
        }
        else if(item.getTitle() == "Excluir Transportadora"){
            AlertDialog.Builder builder = new AlertDialog.Builder(Transportadoras_Compras.this);
            builder.setCancelable(true);
            builder.setTitle("Deseja excluir esse transportadora?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CRUD.excluir(JSON_URL, id.toString(), getApplicationContext());
                    listView.setAdapter(null);
                    loadTranspCompraList();
                }
            }).setNegativeButton("Não", null);
            builder.create().show();
        }
        return true;
    }

    private void loadTranspCompraList(){
        transpcompraList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray transpcompraArray = obj.getJSONArray("transportadoras");

                            for (int i = 0; i < transpcompraArray.length(); i++){
                                JSONObject transpObject = transpcompraArray.getJSONObject(i);

                                TranspCompraConst transp = new TranspCompraConst(transpObject.getString("id_transportadora"),transpObject.getString("nome_transportadora"), transpObject.getString("email"), "R$ "+transpObject.getString("valor_frete"));

                                transpcompraList.add(transp);
                                transpQuery.add(transp);
                            }

                            ListViewTranspCompra adapter = new ListViewTranspCompra(transpcompraList, getApplicationContext());

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

    @Override
    public boolean onQueryTextChange(String newText){
        transpQuery.clear();
        if (TextUtils.isEmpty(newText)) {
            transpQuery.addAll(transpcompraList);
        } else {
            String queryText = newText.toLowerCase();
            for(TranspCompraConst u : transpcompraList){
                if(u.getNome().toLowerCase().contains(queryText) ||
                        u.getEmail().toLowerCase().contains(queryText)){
                    transpQuery.add(u);
                }
            }
        }
        listView.setAdapter(new ListViewTranspCompra(transpQuery, Transportadoras_Compras.this));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        return false;
    }


    public void onBackPressed(){
        super.onBackPressed();
    }

}
