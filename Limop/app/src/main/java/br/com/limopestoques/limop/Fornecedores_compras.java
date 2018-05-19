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
import br.com.limopestoques.limop.CRUD.CRUD;
import br.com.limopestoques.limop.Construtoras.ClientesConst;
import br.com.limopestoques.limop.Construtoras.FornCompraConst;
import br.com.limopestoques.limop.ListView.ListViewClientes;
import br.com.limopestoques.limop.ListView.ListViewFornCompra;
import br.com.limopestoques.limop.Sessao.Sessao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fornecedores_compras extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Json/jsonFornecedores.php";

    ListView listView;

    List<FornCompraConst> forncompraList;
    List<FornCompraConst> fornQuery;

    SearchView searchView;

    Sessao sessao;

    String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fornecedores_compras);

        listView = (ListView)findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        forncompraList = new ArrayList<>();
        fornQuery = new ArrayList<>();

        registerForContextMenu(listView);
        loadFornComprasList();
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);

        sessao = new Sessao(Fornecedores_compras.this);

        tipo = sessao.getString("tipo");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.acoes);
        menu.add(0,v.getId(),0,"Editar Fornecedor");
        if(tipo.equals("Administrador")){
            menu.add(0,v.getId(),0,"Excluir Fornecedor");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Integer pos = info.position;
        FornCompraConst fornecedores = fornQuery.get(pos);
        final String id = fornecedores.getId();
        if(item.getTitle() == "Editar Fornecedor"){
            Intent irTela = new Intent(Fornecedores_compras.this, EditarFornecedor.class);
            irTela.putExtra("id",id);
            startActivity(irTela);
        }
        else if(item.getTitle() == "Excluir Fornecedor"){
            AlertDialog.Builder builder = new AlertDialog.Builder(Fornecedores_compras.this);
            builder.setCancelable(true);
            builder.setTitle("Deseja excluir esse fornecedor?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CRUD.excluir(JSON_URL, id.toString(), getApplicationContext());
                    listView.setAdapter(null);
                    loadFornComprasList();
                }
            }).setNegativeButton("Não", null);
            builder.create().show();
        }
        return true;
    }

    private void loadFornComprasList(){
        forncompraList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray forncompraArray = obj.getJSONArray("fornecedores");

                            for (int i = 0; i < forncompraArray.length(); i++){
                                JSONObject forncompraObject = forncompraArray.getJSONObject(i);

                                FornCompraConst forneCompra = new FornCompraConst(forncompraObject.getString("id_fornecedor"), forncompraObject.getString("nome_fornecedor"), forncompraObject.getString("tipo"), forncompraObject.getString("telefone_comercial"));

                                forncompraList.add(forneCompra);
                                fornQuery.add(forneCompra);
                            }

                            ListViewFornCompra adapter = new ListViewFornCompra(forncompraList, getApplicationContext());

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

    public void CadastrarFornecedor(View v){
        Intent irTela = new Intent(Fornecedores_compras.this, InsertFornecedor.class);
        startActivity(irTela);
    }

    @Override
    public boolean onQueryTextChange(String newText){
        fornQuery.clear();
        if (TextUtils.isEmpty(newText)) {
            fornQuery.addAll(forncompraList);
        } else {
            String queryText = newText.toLowerCase();
            for(FornCompraConst u : forncompraList){
                if(u.getNome().toLowerCase().contains(queryText) ||
                        u.getTipo().toLowerCase().contains(queryText) ||
                        u.getTel().toLowerCase().contains(queryText)){
                    fornQuery.add(u);
                }
            }
        }
        listView.setAdapter(new ListViewFornCompra(fornQuery, Fornecedores_compras.this));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        return false;
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(Fornecedores_compras.this, Compras.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
