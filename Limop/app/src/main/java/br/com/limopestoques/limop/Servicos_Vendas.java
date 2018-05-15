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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.limopestoques.limop.CRUD.CRUD;
import br.com.limopestoques.limop.Construtoras.ProdCompraConst;
import br.com.limopestoques.limop.Construtoras.ServicosCompraConst;
import br.com.limopestoques.limop.Construtoras.ServicosVendasConst;
import br.com.limopestoques.limop.ListView.ListViewProdCompra;
import br.com.limopestoques.limop.ListView.ListViewServicosCompra;
import br.com.limopestoques.limop.ListView.ListViewServicosVendas;

public class Servicos_Vendas extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Json/jsonServVendas.php";

    ListView listView;

    List<ServicosVendasConst> servicosvendaList;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos__vendas);

        listView = (ListView)findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        servicosvendaList = new ArrayList<>();

        registerForContextMenu(listView);
        loadServvendaList();
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
        ServicosVendasConst vendas = servicosvendaList.get(pos);
        final String id = vendas.getId();
        if(item.getTitle() == "Editar Venda"){
            Intent irTela = new Intent(Servicos_Vendas.this, EditarServVenda.class);
            irTela.putExtra("id",id);
            startActivity(irTela);
        }
        else if(item.getTitle() == "Excluir Venda"){
            AlertDialog.Builder builder = new AlertDialog.Builder(Servicos_Vendas.this);
            builder.setCancelable(true);
            builder.setTitle("Deseja excluir essa Venda?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CRUD.excluir(JSON_URL, id.toString(), getApplicationContext());
                    listView.setAdapter(null);
                    loadServvendaList();
                }
            }).setNegativeButton("Não", null);
            builder.create().show();
        }
        return true;
    }

    private void loadServvendaList(){
        servicosvendaList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray servvendaArray = obj.getJSONArray("vendas");

                            for (int i = 0; i < servvendaArray.length(); i++){
                                JSONObject servicoObject = servvendaArray.getJSONObject(i);

                                ServicosVendasConst servVenda = new ServicosVendasConst(servicoObject.getString("id_venda"),servicoObject.getString("nomeServico"),"R$ "+servicoObject.getString("valor"), "Quantidade: "+servicoObject.getString("quantidade"));

                                servicosvendaList.add(servVenda);
                            }

                            ListViewServicosVendas adapter = new ListViewServicosVendas(servicosvendaList, getApplicationContext());

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
        Intent irTela = new Intent(Servicos_Vendas.this, InsertServVenda.class);
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
        Intent irTela = new Intent(Servicos_Vendas.this, Vendas.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}

