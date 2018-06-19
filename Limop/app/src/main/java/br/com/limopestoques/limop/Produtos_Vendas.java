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
import br.com.limopestoques.limop.CRUD.CRUD;
import br.com.limopestoques.limop.Construtoras.ProdCompraConst;
import br.com.limopestoques.limop.Construtoras.ProdVendaConst;
import br.com.limopestoques.limop.ListView.ListViewClientes;
import br.com.limopestoques.limop.ListView.ListViewProdCompra;
import br.com.limopestoques.limop.ListView.ListViewProdVenda;
import br.com.limopestoques.limop.Sessao.Sessao;

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

    List<ProdVendaConst> prodvendaList;
    List<ProdVendaConst> prodQuery;

    SearchView searchView;

    Sessao sessao;

    String tipo;

    static final int oqueeuquero = 112;

    String ultimoid;

    String tipoVenda = "P";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos__vendas);

        listView = (ListView)findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        prodvendaList = new ArrayList<>();
        prodQuery = new ArrayList<>();

        registerForContextMenu(listView);
        loadProdvendaList();
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);

        sessao = new Sessao(Produtos_Vendas.this);

        tipo = sessao.getString("tipo");

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.acoes);
        menu.add(0,v.getId(),0,"Editar Venda");
        if(tipo.equals("Administrador")){
            menu.add(0,v.getId(),0,"Excluir Venda");
        }
        menu.add(0,v.getId(),0,"Gerar Contrato desta venda");
    }

    public boolean checkPermissions(){
        int readPermission = ContextCompat.checkSelfPermission(Produtos_Vendas.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(readPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            ActivityCompat.requestPermissions(Produtos_Vendas.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, oqueeuquero);
        }
        return false;
    }

    public void downloadRelatorio(String id){
        String url = "https://www.limopestoques.com.br/PDF/contrato.php?id=" + id+"&tipo=" +tipoVenda;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Contrato");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Contrato");

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Integer pos = info.position;
        ProdVendaConst vendas = prodQuery.get(pos);
        final String id = vendas.getId();
        if(item.getTitle() == "Editar Venda"){
            Intent irTela = new Intent(Produtos_Vendas.this, EditarProdVenda.class);
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
        else if(item.getTitle() == "Gerar Ordem de Serviço desta venda"){
            Intent irTela = new Intent(Produtos_Vendas.this, InsertOS.class);
            irTela.putExtra("id_venda",id);
            irTela.putExtra("tipo","P");
            startActivity(irTela);
        }
        else if(item.getTitle() == "Gerar Contrato desta venda"){
            if(checkPermissions()){
                ultimoid = id;
                downloadRelatorio(id);
            }
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

                                ProdVendaConst prodCompra = new ProdVendaConst(produtoObject.getString("id_venda"),produtoObject.getString("nome_produto"),"R$ "+produtoObject.getString("valor"), "Quantidade: "+produtoObject.getString("quantidade"),produtoObject.getString("fotos"),"Cliente: " + produtoObject.getString("nome_Cliente"));

                                prodvendaList.add(prodCompra);
                                prodQuery.add(prodCompra);
                            }

                            ListViewProdVenda adapter = new ListViewProdVenda(prodvendaList, getApplicationContext());

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
        prodQuery.clear();
        if (TextUtils.isEmpty(newText)) {
            prodQuery.addAll(prodvendaList);
        } else {
            String queryText = newText.toLowerCase();
            for(ProdVendaConst u : prodvendaList){
                if(u.getProd().toLowerCase().contains(queryText)){
                    prodQuery.add(u);
                }
            }
        }
        listView.setAdapter(new ListViewProdVenda(prodQuery, Produtos_Vendas.this));
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
