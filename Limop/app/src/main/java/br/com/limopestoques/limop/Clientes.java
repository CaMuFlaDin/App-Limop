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
import br.com.limopestoques.limop.Construtoras.ClientesConst;
import br.com.limopestoques.limop.Construtoras.UsuariosConst;
import br.com.limopestoques.limop.ListView.ListViewClientes;

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
    List<ClientesConst> clientesQuery;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        listView = (ListView)findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        clientesList = new ArrayList<ClientesConst>();
        clientesQuery = new ArrayList<ClientesConst>();

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
        ClientesConst clientes = clientesQuery.get(pos);
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
                                clientesQuery.add(clientes);
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
        clientesQuery.clear();
        if (TextUtils.isEmpty(newText)) {
            clientesQuery.addAll(clientesList);
        } else {
            String queryText = newText.toLowerCase();
            for(ClientesConst u : clientesList){
                if(u.getNome().toLowerCase().contains(queryText) ||
                        u.getEmail().toLowerCase().contains(queryText)){
                    clientesQuery.add(u);
                }
            }
        }
        listView.setAdapter(new ListViewClientes(clientesQuery, Clientes.this));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        return false;
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(Clientes.this, Vendas.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
