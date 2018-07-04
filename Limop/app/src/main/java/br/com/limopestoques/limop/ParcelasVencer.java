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
import android.widget.TextView;
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
import java.util.Calendar;
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

    TextView Link;

    Double recebidoo = 0.0;
    Double receber  = 0.0;
    Double atrasado = 0.0;
    Integer cont_recebido = 0;
    Integer cont_receber = 0;
    Integer cont_atrasado = 0;
    Integer cont_total = 0;
    Double Totalzao = 0.0;

    List<ParcelasConst> parcelasList;
    List<ParcelasConst> parcelasQuery;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parcelas_vencer);

        listView = (ListView)findViewById(R.id.listView);
        Link = findViewById(R.id.link);
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
                String recebido = parcelasQuery.get(i).getRecebido();
                if(recebido.equals("0")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ParcelasVencer.this);
                    builder.setCancelable(true);
                    builder.setTitle("Deseja realmente efetivar essa venda?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int ii) {
                            final String id_venda = parcelasQuery.get(i).getId();
                            final String recebido = parcelasQuery.get(i).getRecebido();
                            final String id_produto = parcelasQuery.get(i).getId_produto();
                            final String quantidade = parcelasQuery.get(i).getQuantidade();

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String ServerResponse) {
                                            System.out.println(ServerResponse);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError volleyError) {
                                            Toast.makeText(ParcelasVencer.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() {

                                    Map<String, String> params = new HashMap<>();

                                    //Update efetivar venda
                                    params.put("update", "update");
                                    params.put("id_venda", id_venda);
                                    params.put("id_produto", id_produto);
                                    params.put("quantidade", quantidade);

                                    return params;
                                }

                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(ParcelasVencer.this);
                            requestQueue.add(stringRequest);
                            //parcelasList.clear();
                            //loadParcelasList();
                        }
                    }).setNegativeButton("Não", null);
                    builder.create().show();
                }
            }
        });
    }

    public void cards(View v){
        Intent i = new Intent(ParcelasVencer.this, valores_parcelas.class);
        i.putExtra("valorAtrasado", atrasado);
        i.putExtra("valorReceber", receber);
        i.putExtra("valorRecebido", recebidoo);
        i.putExtra("contAtrasado", cont_atrasado);
        i.putExtra("contReceber", cont_receber);
        i.putExtra("contRecebido", cont_recebido);
        startActivity(i);
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

                            Date hoje = Calendar.getInstance().getTime();

                            for (int i = 0; i < usuarioArray.length(); i++){
                                JSONObject usuarioObject = usuarioArray.getJSONObject(i);

                                //Recuperar dados
                                ParcelasConst users = new ParcelasConst(usuarioObject.getString("id_venda"), usuarioObject.getString("nome_Cliente"),"R$ " + usuarioObject.getString("valor"), usuarioObject.getString("vencimento"), usuarioObject.getString("quantidade"), usuarioObject.getString("id_produto"), usuarioObject.getString("recebido"));

                                //Formatar data
                                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                                ParsePosition pos = new ParsePosition(0);
                                Date vencimento = formato.parse(usuarioObject.getString("vencimento"),pos);

                                Integer qtd = usuarioObject.getInt("quantidade");
                                Double valor = usuarioObject.getDouble("valor");
                                Double valorTotal = (double) qtd * valor;
                                int situacaoInt = usuarioObject.getInt("recebido");

                                if(situacaoInt == 1){
                                    cont_recebido++;
                                    recebidoo += valorTotal;
                                }else if(vencimento.before(hoje)){
                                    cont_atrasado++;
                                    atrasado += valorTotal;
                                }else{
                                    cont_receber++;
                                    receber += valorTotal;
                                }
                                Totalzao += valorTotal;
                                cont_total = cont_atrasado + cont_receber + cont_recebido;
                                parcelasList.add(users);
                                parcelasQuery.add(users);

                            }
                            //Atrasado.setText("Atrasado: "+String.format("%.2f", atrasado)+"("+cont_atrasado+")");
                            //Receber.setText("Receber: "+String.format("%.2f", receber)+"("+cont_receber+")");
                            //Recebido.setText("Recebido: "+String.format("%.2f", recebidoo)+"("+cont_recebido+")");
                            //Total.setText("Total: "+String.format("%.2f", Totalzao)+"("+cont_total+")");

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
