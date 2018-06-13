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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.limopestoques.limop.CRUD.CRUD;
import br.com.limopestoques.limop.Construtoras.ParcelasConst;
import br.com.limopestoques.limop.Construtoras.UsuariosConst;
import br.com.limopestoques.limop.ListView.ListViewParcelas;
import br.com.limopestoques.limop.ListView.ListViewUsuarios;
import br.com.limopestoques.limop.Sessao.Sessao;

public class ParcelasVencer extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Json/jsonParcelas.php";

    ListView listView;

    Sessao sessao;

    String tipo;

    List<ParcelasConst> parcelasList;
    List<ParcelasConst> parcelasQuery;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcelas_vencer);

        listView = (ListView)findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        parcelasList = new ArrayList<>();
        parcelasQuery = new ArrayList<>();

        sessao = new Sessao(ParcelasVencer.this);

        registerForContextMenu(listView);
        loadParcelasList();
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ParcelasVencer.this);
                builder.setCancelable(true);
                builder.setTitle("Status - Parcelas");
                builder.setMessage("Deseja realmente pagar esta parcela?");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ii) {
                        String id = parcelasQuery.get(i).getId();
                        //TODO: terminar
                    }
                }).setNegativeButton("Não", null);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.acoes);
        menu.add(0,v.getId(),0,"Editar Usuário");
        if(tipo.equals("Administrador")){
            menu.add(0,v.getId(),0,"Excluir Usuário");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Integer pos = info.position;
        ParcelasConst usuarios = parcelasQuery.get(pos);
        final String id = usuarios.getId();
        if(item.getTitle() == "Editar Usuário"){
            Intent irTela = new Intent(ParcelasVencer.this, EditarUsuario.class);
            irTela.putExtra("id",id);
            startActivity(irTela);
        }
        else if(item.getTitle() == "Excluir Usuário"){
            AlertDialog.Builder builder = new AlertDialog.Builder(ParcelasVencer.this);
            builder.setCancelable(true);
            builder.setTitle("Deseja excluir esse Usuário?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CRUD.excluir(JSON_URL, id.toString(), getApplicationContext());
                    listView.setAdapter(null);
                    loadParcelasList();
                }
            }).setNegativeButton("Não", null);
            builder.create().show();
        }
        return true;
    }

    private void loadParcelasList(){
        parcelasList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray usuarioArray = obj.getJSONArray("parcelas");

                            for (int i = 0; i < usuarioArray.length(); i++){
                                JSONObject usuarioObject = usuarioArray.getJSONObject(i);

                                ParcelasConst users = new ParcelasConst(usuarioObject.getString("id_venda"), usuarioObject.getString("nome_Cliente"),"R$ " + usuarioObject.getString("valor"), "Data de Vencimento: " + usuarioObject.getString("vencimento"));

                                parcelasList.add(users);
                                parcelasQuery.add(users);
                            }

                            ListViewParcelas adapter = new ListViewParcelas(parcelasList, getApplicationContext());

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
        parcelasQuery.clear();
        if (TextUtils.isEmpty(newText)) {
            parcelasQuery.addAll(parcelasList);
        } else {
            String queryText = newText.toLowerCase();
            for(ParcelasConst u : parcelasList){
                if(u.getVencimento().toLowerCase().contains(queryText) ||
                        u.getCliente().toLowerCase().contains(queryText) ||
                        u.getValor().toLowerCase().contains(queryText)){
                    parcelasQuery.add(u);
                }
            }
        }
        listView.setAdapter(new ListViewParcelas(parcelasQuery, ParcelasVencer.this));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        return false;
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(ParcelasVencer.this, Vendas.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
