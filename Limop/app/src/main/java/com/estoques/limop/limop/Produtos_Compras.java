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
import com.estoques.limop.limop.Construtoras.FornCompraConst;
import com.estoques.limop.limop.Construtoras.ProdCompraConst;
import com.estoques.limop.limop.ListView.ListViewProdCompra;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Produtos_Compras extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Json/jsonProd.php";

    ListView listView;

    List<ProdCompraConst> prodcompraList;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos__compras);

        listView = (ListView)findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        prodcompraList = new ArrayList<>();

        registerForContextMenu(listView);
        loadProdCompraList();
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.acoes);
        menu.add(0,v.getId(),0,"Editar Produto");
        menu.add(0,v.getId(),0,"Excluir Produto");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Integer pos = info.position;
        ProdCompraConst fornecedores = prodcompraList.get(pos);
        final String id = fornecedores.getId();
        if(item.getTitle() == "Editar Produto"){
            Intent irTela = new Intent(Produtos_Compras.this, EditarProduto.class);
            irTela.putExtra("id",id);
            startActivity(irTela);
        }
        else if(item.getTitle() == "Excluir Produto"){
            AlertDialog.Builder builder = new AlertDialog.Builder(Produtos_Compras.this);
            builder.setCancelable(true);
            builder.setTitle("Deseja excluir esse Produto?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CRUD.excluir(JSON_URL, id.toString(), getApplicationContext());
                    listView.setAdapter(null);
                    loadProdCompraList();
                }
            }).setNegativeButton("Não", null);
            builder.create().show();
        }
        return true;
    }

    private void loadProdCompraList(){
        prodcompraList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray prodcompraArray = obj.getJSONArray("produtos");

                            for (int i = 0; i < prodcompraArray.length(); i++){
                                JSONObject produtoObject = prodcompraArray.getJSONObject(i);

                                ProdCompraConst prodCompra = new ProdCompraConst(produtoObject.getString("id_produto"),produtoObject.getString("nome"),"R$ "+produtoObject.getString("valor_venda"), produtoObject.getString("disponivel_estoque")+" Disponíveis no estoque");

                                prodcompraList.add(prodCompra);
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

    public void CadastrarProduto(View v){
        Intent irTela = new Intent(Produtos_Compras.this, InsertProduto.class);
        startActivity(irTela);
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

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(Produtos_Compras.this, Compras.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
