package com.estoques.limop.limop;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.estoques.limop.limop.CRUD.CRUD;
import com.estoques.limop.limop.Construtoras.ServicosCompraConst;
import com.estoques.limop.limop.ListView.ListViewServicosCompra;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Servicos_Compras extends AppCompatActivity {

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Json/jsonServicos.php";

    ListView listView;

    List<ServicosCompraConst> servicocompraList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos__compras);

        listView = (ListView)findViewById(R.id.listView);
        servicocompraList = new ArrayList<>();

        registerForContextMenu(listView);
        loadServicoCompraList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.acoes);
        menu.add(0,v.getId(),0,"Editar Serviço");
        menu.add(0,v.getId(),0,"Excluir Serviço");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Integer pos = info.position;
        ServicosCompraConst servico = servicocompraList.get(pos);
        final String id = servico.getId();
        if(item.getTitle() == "Editar Serviço"){
            Intent irTela = new Intent(Servicos_Compras.this, EditarServico.class);
            irTela.putExtra("id", id);
            startActivity(irTela);
        }
        else if(item.getTitle() == "Excluir Serviço"){
            AlertDialog.Builder builder = new AlertDialog.Builder(Servicos_Compras.this);
            builder.setCancelable(true);
            builder.setTitle("Deseja excluir esse serviço?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CRUD.excluir(JSON_URL, id.toString(), getApplicationContext());
                    listView.setAdapter(null);
                    loadServicoCompraList();
                }
            }).setNegativeButton("Não", null);
            builder.create().show();
        }
        return true;
    }

    public void CadastrarServico(View v){
        Intent irTela = new Intent(Servicos_Compras.this, InsertServico.class);
        startActivity(irTela);
    }

    private void loadServicoCompraList(){
        servicocompraList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray servicocompraArray = obj.getJSONArray("servicos");

                            for (int i = 0; i < servicocompraArray.length(); i++){
                                JSONObject servicoObject = servicocompraArray.getJSONObject(i);

                                ServicosCompraConst servicoCompra = new ServicosCompraConst(servicoObject.getString("id_servico"),servicoObject.getString("nome_servico"), "Valor de Custo R$ "+servicoObject.getString("valor_custo"), "Valor de Venda R$ "+servicoObject.getString("valor_venda"));

                                servicocompraList.add(servicoCompra);
                            }

                            ListViewServicosCompra adapter = new ListViewServicosCompra(servicocompraList, getApplicationContext());

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

}
