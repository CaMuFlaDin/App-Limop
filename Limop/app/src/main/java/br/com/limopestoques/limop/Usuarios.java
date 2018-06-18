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
import br.com.limopestoques.limop.Construtoras.UsuariosConst;
import br.com.limopestoques.limop.ListView.ListViewClientes;
import br.com.limopestoques.limop.ListView.ListViewUsuarios;
import br.com.limopestoques.limop.Sessao.Sessao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Usuarios extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Json/jsonUser.php";

    ListView listView;

    Sessao sessao;

    String tipo;
    String idUser;

    List<UsuariosConst> usuariosList;
    List<UsuariosConst> usuariosQuery;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        listView = (ListView)findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        usuariosList = new ArrayList<>();
        usuariosQuery = new ArrayList<>();

        sessao = new Sessao(Usuarios.this);

        tipo = sessao.getString("tipo");
        idUser = sessao.getString("id_usuario");

        registerForContextMenu(listView);
        loadUsuarioList();
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);
    }

    public void insertUsuario(View v){
        Intent irTela = new Intent(Usuarios.this, InsertUsuario.class);
        startActivity(irTela);
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
        UsuariosConst usuarios = usuariosQuery.get(pos);
        final String id = usuarios.getId();
        if(item.getTitle() == "Editar Usuário"){
            Intent irTela = new Intent(Usuarios.this, EditarUsuario.class);
            irTela.putExtra("id",id);
            startActivity(irTela);
        }
        else if(item.getTitle() == "Excluir Usuário"){
            AlertDialog.Builder builder = new AlertDialog.Builder(Usuarios.this);
            builder.setCancelable(true);
            builder.setTitle("Deseja excluir esse Usuário?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CRUD.excluir(JSON_URL, id.toString(), getApplicationContext());
                    listView.setAdapter(null);
                    loadUsuarioList();
                }
            }).setNegativeButton("Não", null);
            builder.create().show();
        }
        return true;
    }

    private void loadUsuarioList(){
        usuariosList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray usuarioArray = obj.getJSONArray("usuarios");

                            for (int i = 0; i < usuarioArray.length(); i++){
                                JSONObject usuarioObject = usuarioArray.getJSONObject(i);

                                UsuariosConst users = new UsuariosConst(usuarioObject.getString("id_usuario"),usuarioObject.getString("nome_usuario"), usuarioObject.getString("email"),usuarioObject.getString("tipo"),usuarioObject.getString("foto"));

                                usuariosList.add(users);
                                usuariosQuery.add(users);
                            }

                            ListViewUsuarios adapter = new ListViewUsuarios(usuariosList, getApplicationContext());

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
                params.put("idUser", idUser);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onQueryTextChange(String newText){
        usuariosQuery.clear();
        if (TextUtils.isEmpty(newText)) {
            usuariosQuery.addAll(usuariosList);
        } else {
            String queryText = newText.toLowerCase();
            for(UsuariosConst u : usuariosList){
                if(u.getName().toLowerCase().contains(queryText) ||
                        u.getEmail().toLowerCase().contains(queryText) ||
                        u.getTipo().toLowerCase().contains(queryText)){
                    usuariosQuery.add(u);
                }
            }
        }
        listView.setAdapter(new ListViewUsuarios(usuariosQuery, Usuarios.this));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        return false;
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(Usuarios.this, Principal.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}