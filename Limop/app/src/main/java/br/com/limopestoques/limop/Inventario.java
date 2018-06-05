package br.com.limopestoques.limop;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.Map;

import br.com.limopestoques.limop.CRUD.CRUD;
import br.com.limopestoques.limop.Construtoras.FornCompraConst;

public class Inventario extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ArrayList<FornCompraConst> fornecedores;
    Spinner fornecedor,categoria;
    EditText qtd;
    private String idFornecedor;

    static final int oqueeuquero = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        fornecedor                = findViewById(R.id.fornecedor);
        categoria                 = findViewById(R.id.categoria);
        qtd                       = findViewById(R.id.qtd);

        adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        fornecedores = new ArrayList<FornCompraConst>();
        carregarFornecedor();

        fornecedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idFornecedor = fornecedores.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void gerar(View v) {

        if(checkPermissions()){
            downloadRelatorio(categoria.getSelectedItem().toString(), idFornecedor, qtd.getText().toString());
        }

    }

    public void carregarFornecedor(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://limopestoques.com.br/Android/Json/jsonFornecedores.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray forncompraArray = obj.getJSONArray("fornecedores");

                            FornCompraConst forneCompra = new FornCompraConst("0", null, null, null);
                            fornecedores.add(forneCompra);
                            adapter.add("");
                            for (int i = 0; i < forncompraArray.length(); i++){
                                JSONObject forncompraObject = forncompraArray.getJSONObject(i);

                                forneCompra = new FornCompraConst(forncompraObject.getString("id_fornecedor"), forncompraObject.getString("nome_fornecedor"), forncompraObject.getString("tipo"), forncompraObject.getString("telefone_comercial"));

                                fornecedores.add(forneCompra);
                                adapter.add(forneCompra.getNome());
                            }
                            fornecedor.setAdapter(adapter);
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

        RequestQueue rq = Volley.newRequestQueue(this);
        rq.add(stringRequest);
    }
    public boolean checkPermissions(){
        int readPermission = ContextCompat.checkSelfPermission(Inventario.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(readPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            ActivityCompat.requestPermissions(Inventario.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, oqueeuquero);
        }
        return false;
    }

    public void downloadRelatorio(String categoria, String fornecedor, String qtd){
        String url = "https://www.limopestoques.com.br/Android/Json/Inventario.php?categoria=" + categoria+"&fornecedor=" +fornecedor+"&qtd="+qtd;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Inventário");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Inventário");

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
                    Toast.makeText(this, "É necessário garantir a permissão para download dos relatórios!", Toast.LENGTH_SHORT).show();
                }else{
                    downloadRelatorio(categoria.getSelectedItem().toString(), idFornecedor, qtd.getText().toString());
                }
                break;
        }
    }

}
