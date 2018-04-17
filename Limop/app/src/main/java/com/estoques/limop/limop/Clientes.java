package com.estoques.limop.limop;

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
import com.estoques.limop.limop.CRUD.CRUD;
import com.estoques.limop.limop.Construtoras.ClientesConst;
import com.estoques.limop.limop.Construtoras.FornCompraConst;
import com.estoques.limop.limop.ListView.ListViewClientes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Clientes extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Json/jsonClientes.php";

    ListView listView;

    List<ClientesConst> clientesList;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        listView = (ListView)findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        clientesList = new ArrayList<>();

        registerForContextMenu(listView);
        loadClientesList();
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);
    }

    public void insertCliente(View v){
        Intent irTela = new Intent(Clientes.this, InsertCliente.class);
        startActivity(irTela);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.acoes);
        menu.add(0,v.getId(),0,"Editar Cliente");
        menu.add(0,v.getId(),0,"Excluir Cliente");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Integer pos = info.position;
        ClientesConst clientes = clientesList.get(pos);
        final String id = clientes.getId();
        if(item.getTitle() == "Editar Cliente"){
            Intent irTela = new Intent(Clientes.this, EditarCliente.class);
            irTela.putExtra("id", id);
            startActivity(irTela);
        }
        else if(item.getTitle() == "Excluir Cliente"){
            AlertDialog.Builder builder = new AlertDialog.Builder(Clientes.this);
            builder.setCancelable(true);
            builder.setTitle("Deseja excluir esse Cliente?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CRUD.excluir(JSON_URL, id.toString(), getApplicationContext());
                    listView.setAdapter(null);
                    loadClientesList();
                }
            }).setNegativeButton("Não", null);
            builder.create().show();
        }
        return true;
    }

    private void loadClientesList(){
        clientesList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray clientesArray = obj.getJSONArray("clientes");

                            for (int i = 0; i < clientesArray.length(); i++){
                                JSONObject clienteObject = clientesArray.getJSONObject(i);

                                ClientesConst clientes = new ClientesConst(clienteObject.getString("id_cliente"), clienteObject.getString("nome_cliente"), clienteObject.getString("tipo"), clienteObject.getString("email"));

                                clientesList.add(clientes);
                            }

                            ListViewClientes adapter = new ListViewClientes(clientesList, getApplicationContext());

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
        if (TextUtils.isEmpty(newText)) {
            listView.clearTextFilter();
        } else {
            listView.setFilterText(newText);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        return false;
    }
}
