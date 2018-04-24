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
import com.estoques.limop.limop.Construtoras.ProdCompraConst;
import com.estoques.limop.limop.ListView.ListViewProdCompra;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Produtos_Vendas extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Json/jsonProdVendas.php";

    ListView listView;

    List<ProdCompraConst> prodvendaList;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos__vendas);

        listView = (ListView)findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        prodvendaList = new ArrayList<>();

        registerForContextMenu(listView);
        loadProdvendaList();
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.acoes);
        menu.add(0,v.getId(),0,"Editar Venda");
        menu.add(0,v.getId(),0,"Excluir Venda");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Integer pos = info.position;
        ProdCompraConst vendas = prodvendaList.get(pos);
        final String id = vendas.getId();
        if(item.getTitle() == "Editar Venda"){
            Intent irTela = new Intent(Produtos_Vendas.this, EditarProduto.class);
            irTela.putExtra("id",id);
            startActivity(irTela);
        }
        else if(item.getTitle() == "Excluir Venda"){
            AlertDialog.Builder builder = new AlertDialog.Builder(Produtos_Vendas.this);
            builder.setCancelable(true);
            builder.setTitle("Deseja excluir essa Venda?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CRUD.excluir(JSON_URL, id.toString(), getApplicationContext());
                    listView.setAdapter(null);
                    loadProdvendaList();
                }
            }).setNegativeButton("Não", null);
            builder.create().show();
        }
        return true;
    }

    private void loadProdvendaList(){
        prodvendaList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray prodvendaArray = obj.getJSONArray("vendas");

                            for (int i = 0; i < prodvendaArray.length(); i++){
                                JSONObject produtoObject = prodvendaArray.getJSONObject(i);

                                ProdCompraConst prodCompra = new ProdCompraConst(produtoObject.getString("id_venda"),produtoObject.getString("nome_produto"),"R$ "+produtoObject.getString("valor"), "Quantidade: "+produtoObject.getString("quantidade"));

                                prodvendaList.add(prodCompra);
                            }

                            ListViewProdCompra adapter = new ListViewProdCompra(prodvendaList, getApplicationContext());

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

    public void CadastrarVenda(View v){
        Intent irTela = new Intent(Produtos_Vendas.this, InsertProdVenda.class);
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
        Intent irTela = new Intent(Produtos_Vendas.this, Vendas.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
