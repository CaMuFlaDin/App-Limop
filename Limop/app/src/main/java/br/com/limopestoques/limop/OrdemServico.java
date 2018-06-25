package br.com.limopestoques.limop;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.limopestoques.limop.CRUD.CRUD;
import br.com.limopestoques.limop.Construtoras.OrdemServicoConst;
import br.com.limopestoques.limop.ListView.ListViewOrdemServico;
import br.com.limopestoques.limop.Sessao.Sessao;

public class OrdemServico extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Json/jsonOS.php";

    ListView listView;

    List<OrdemServicoConst> ordemservicoList;
    List<OrdemServicoConst> ordemservicoQuery;

    SearchView searchView;

    Sessao sessao;

    String tipo;

    static final int oqueeuquero = 112;

    String ultimoid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordem_servico);

        listView = findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        ordemservicoList = new ArrayList<>();
        ordemservicoQuery = new ArrayList<>();
        registerForContextMenu(listView);
        loadOrdemServicoList();
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);

        sessao = new Sessao(OrdemServico.this);

        tipo = sessao.getString("tipo");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.acoes);
        menu.add(0,v.getId(),0,"Download PDF");
        menu.add(0,v.getId(),0,"Editar Ordem de Serviço");
        if(tipo.equals("Administrador")){
            menu.add(0,v.getId(),0,"Excluir Ordem de Serviço");
        }
    }

    public boolean checkPermissions(){
        int readPermission = ContextCompat.checkSelfPermission(OrdemServico.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(readPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            ActivityCompat.requestPermissions(OrdemServico.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, oqueeuquero);
        }
        return false;
    }

    public void downloadRelatorio(String id){
        String url = "https://www.limopestoques.com.br/PDF/os.php?id=" + id;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Ordem de Serviço");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Ordem de Serviço");

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case oqueeuquero:
                if(!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    Toast.makeText(this, getString(R.string.erropermissao), Toast.LENGTH_SHORT).show();
                }else{
                    downloadRelatorio(ultimoid);
                }
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Integer pos = info.position;
        OrdemServicoConst os = ordemservicoQuery.get(pos);
        final String id = os.getId();
        if(item.getTitle() == "Download PDF"){
            if(checkPermissions()){
                ultimoid = id;
                downloadRelatorio(id);
            }
        }
        else if(item.getTitle() == "Editar Ordem de Serviço"){
            Intent irTela = new Intent(OrdemServico.this, EditarOS.class);
            irTela.putExtra("id_os",id);
            startActivity(irTela);
        }
        else if(item.getTitle() == "Excluir Ordem de Serviço"){
            AlertDialog.Builder builder = new AlertDialog.Builder(OrdemServico.this);
            builder.setCancelable(true);
            builder.setTitle("Deseja excluir essa Ordem de Serviço?");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CRUD.excluir(JSON_URL, id.toString(), getApplicationContext());
                    listView.setAdapter(null);
                    loadOrdemServicoList();
                }
            }).setNegativeButton("Não", null);
            builder.create().show();
        }
        return true;
    }

    public void insertOS(View v){
        Intent i = new Intent(OrdemServico.this, InsertOS.class);
        startActivity(i);
    }

    private void loadOrdemServicoList(){
        ordemservicoList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray ordemservicoArray = obj.getJSONArray("os");

                            for (int i = 0; i < ordemservicoArray.length(); i++){
                                JSONObject ordemdeservicoObject = ordemservicoArray.getJSONObject(i);

                                OrdemServicoConst os = new OrdemServicoConst(ordemdeservicoObject.getString("id_os"),ordemdeservicoObject.getString("nome_cliente"),ordemdeservicoObject.getString("nome"), "Número do pedido: " + ordemdeservicoObject.getString("id_os"));

                                ordemservicoList.add(os);
                                ordemservicoQuery.add(os);
                            }

                            ListViewOrdemServico adapter = new ListViewOrdemServico(ordemservicoList, getApplicationContext());

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
        ordemservicoQuery.clear();
        if (TextUtils.isEmpty(newText)) {
            ordemservicoQuery.addAll(ordemservicoList);
        } else {
            String queryText = newText.toLowerCase();
            for(OrdemServicoConst u : ordemservicoList){
                if(u.getNumPedido().toLowerCase().contains(queryText) ||
                        u.getCliente().toLowerCase().contains(queryText) ||
                        u.getEquipamento().toLowerCase().contains(queryText)){
                    ordemservicoQuery.add(u);
                }
            }
        }
        listView.setAdapter(new ListViewOrdemServico(ordemservicoQuery, OrdemServico.this));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        return false;
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(OrdemServico.this, Principal.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
