package br.com.limopestoques.limop;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import br.com.limopestoques.limop.CRUD.CRUD;
import br.com.limopestoques.limop.Construtoras.ClientesConst;
import br.com.limopestoques.limop.Construtoras.ProdCompraConst;
import br.com.limopestoques.limop.ListView.ListViewClientes;
import br.com.limopestoques.limop.ListView.ListViewProdCompra;
import br.com.limopestoques.limop.Sessao.Sessao;

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

    Sessao sessao;

    String tipo;

    List<ProdCompraConst> prodcompraList;
    List<ProdCompraConst> prodQuery;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos__compras);

        listView = (ListView)findViewById(R.id.listView);
        searchView = findViewById(R.id.sv);
        prodcompraList = new ArrayList<>();
        prodQuery = new ArrayList<>();

        registerForContextMenu(listView);
        loadProdCompraList();
        listView.setTextFilterEnabled(true);
        searchView.setOnQueryTextListener(this);

        sessao = new Sessao(Produtos_Compras.this);

        tipo = sessao.getString("tipo");
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle(R.string.acoes);
        menu.add(0,v.getId(),0,"Editar Produto");
        if(tipo.equals("Administrador")){
            menu.add(0,v.getId(),0,"Excluir Produto");
            menu.add(0,v.getId(),0,"Repor este Produto");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Integer pos = info.position;
        ProdCompraConst fornecedores = prodQuery.get(pos);
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
        else if(item.getTitle() == "Repor este Produto"){
            modal(id);
        }
        return true;
    }

    public void modal(String id){
        final String idProdutinho;
        idProdutinho= id;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        final View view = getLayoutInflater().inflate(R.layout.modal_produto, null);
        final TextView titulo, qtdAtual;
        final EditText qtdAdd;
        titulo        = view.findViewById(R.id.titulo);
        qtdAtual      = view.findViewById(R.id.qtdAtual);
        qtdAdd        = view.findViewById(R.id.qtd);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray servicocompraArray = obj.getJSONArray("qtd");
                            JSONObject jo = servicocompraArray.getJSONObject(0);

                            titulo.setText(titulo.getText()+" "+jo.getString("nome"));
                            qtdAtual.setText(qtdAtual.getText()+" "+jo.getString("disponivel_estoque"));

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
                params.put("inf", "inf");
                params.put("id", idProdutinho);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        //titulo.setText(resposta.getAluno());
        //campoResposta.setText(resposta.getResposta());
        builder.setView(view);
        builder.setPositiveButton(getResources().getString(R.string.adicionar), null)
                .setNegativeButton(getResources().getString(R.string.cancelar), null);

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button confirmar = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                confirmar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("updateQtd", "updateQtd");
                        params.put("idProduto", idProdutinho);
                        params.put("qtdAdd", qtdAdd.getText().toString());

                        CRUD.inserir("https://limopestoques.com.br/Android/updateQtdBanco.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response){
                                try{
                                    JSONObject jo = new JSONObject(response);
                                    String resposta = jo.getString("resposta");
                                    dialog.dismiss();
                                    Toast.makeText(Produtos_Compras.this, resposta, Toast.LENGTH_SHORT).show();
                                    if(resposta.equals("Adicionado com sucesso!")){
                                        loadProdCompraList();
                                    }
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },params,getApplicationContext());
                    }
                });
            }
        });
        dialog.show();
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

                                ProdCompraConst prodCompra = new ProdCompraConst(produtoObject.getString("id_produto"),produtoObject.getString("nome"),"R$ "+produtoObject.getString("valor_venda"), produtoObject.getString("disponivel_estoque")+" Disponíveis no estoque",produtoObject.getString("fotos"));

                                prodcompraList.add(prodCompra);
                                prodQuery.add(prodCompra);
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
        prodQuery.clear();
        if (TextUtils.isEmpty(newText)) {
            prodQuery.addAll(prodcompraList);
        } else {
            String queryText = newText.toLowerCase();
            for(ProdCompraConst u : prodcompraList){
                if(u.getProd().toLowerCase().contains(queryText)){
                    prodQuery.add(u);
                }
            }
        }
        listView.setAdapter(new ListViewProdCompra(prodQuery, Produtos_Compras.this));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        return false;
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(Produtos_Compras.this, Principal.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
