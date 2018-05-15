package br.com.limopestoques.limop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import br.com.limopestoques.limop.CRUD.CRUD;
import br.com.limopestoques.limop.Construtoras.FornCompraConst;
import br.com.limopestoques.limop.Construtoras.UsuariosConst;
import br.com.limopestoques.limop.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditarProduto extends AppCompatActivity {

    private String idFornecedor;

    EditText disponivel_estoque,min_estoque,max_estoque,peso_liquido,peso_bruto,nome_produto,valor_venda,valor_custo;
    Spinner fornecedor,categoriaProd;
    Button button;
    NetworkImageView niv;

    String imagem;


    private static final int GALLERY_REQUEST = 1;
    private Bitmap img = null;

    private String id;

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Update/updateProduto.php";

    ArrayAdapter<String> adapter;
    ArrayList<FornCompraConst> fornecedores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_produto);

        nome_produto              = findViewById(R.id.nome);
        disponivel_estoque        = findViewById(R.id.disponivel_estoque);
        min_estoque               = findViewById(R.id.min_estoque);
        max_estoque               = findViewById(R.id.max_estoque);
        peso_liquido              = findViewById(R.id.peso_liquido);
        peso_bruto                = findViewById(R.id.peso_bruto);
        fornecedor                = findViewById(R.id.fornecedor);
        valor_venda               = findViewById(R.id.valor_venda);
        valor_custo               = findViewById(R.id.valor_custo);
        categoriaProd             = findViewById(R.id.categoria);
        niv                       = findViewById(R.id.img);

        try{
            Intent i = getIntent();
            this.id = i.getStringExtra("id");
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        niv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                startActivityForResult(photoPicker, GALLERY_REQUEST);
            }
        });

        carregar();

        adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        fornecedores = new ArrayList<FornCompraConst>();

        carregarFornecedor();
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode, resultCode, data);

        if(resultCode == RESULT_OK && reqCode == GALLERY_REQUEST){
            try{
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                img = BitmapFactory.decodeStream(imageStream);
                niv.setImageBitmap(img);
            }catch(FileNotFoundException e){
                e.printStackTrace();
                Toast.makeText(this, "Erro ao receber a imagem: Imagem não encontrada!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public String getStringImage(Bitmap imagem){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] b = outputStream.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);

        return temp;
    }

    public void validarCampos(View v){
        if(nome_produto.length() == 0 || valor_custo.length() == 0 || valor_venda.length() == 0 || disponivel_estoque.length() == 0 || min_estoque.length() == 0 || max_estoque.length() == 0 ||
                peso_liquido.length() == 0 || peso_bruto.length() == 0){
            Toast.makeText(this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();
        }else{
            updateProduto();
        }
    }

    public void carregar(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray servicocompraArray = obj.getJSONArray("produtos");
                            JSONObject jo = servicocompraArray.getJSONObject(0);

                            nome_produto.setText(jo.getString("nome"));
                            disponivel_estoque.setText(jo.getString("disponivel_estoque"));
                            min_estoque.setText(jo.getString("min_estoque"));
                            max_estoque.setText(jo.getString("max_estoque"));
                            peso_liquido.setText(jo.getString("peso_liquido"));
                            peso_bruto.setText(jo.getString("peso_bruto"));
                            valor_custo.setText(jo.getString("valor_custo"));
                            valor_venda.setText(jo.getString("valor_venda"));
                            String categoria = jo.getString("categoria");
                            imagem = jo.getString("fotos");

                            ImageLoader il = Singleton.getInstance(EditarProduto.this).getImageLoader();
                            niv.setImageUrl("https://limopestoques.com.br/Index_adm/produtos/imgs/"+ imagem,il);

                            if(categoria.equals("Acabado")){
                                categoriaProd.setSelection(0);
                            }else if(categoria.equals("Mercadoria para Revenda")){
                                categoriaProd.setSelection(1);
                            }else if(categoria.equals("Matéria-Prima")){
                                categoriaProd.setSelection(2);
                            }else if(categoria.equals("Embalagem")){
                                categoriaProd.setSelection(3);
                            }else if(categoria.equals("Produto em Processo")){
                                categoriaProd.setSelection(4);
                            }else{
                                categoriaProd.setSelection(5);
                            }

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
                params.put("id_produto", id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void updateProduto() {
        Map<String, String> params = new HashMap<String, String>();

        if(img == null){
            params.put("imgProd", imagem);
        }else{
            String imagemProduto = getStringImage(img);
            params.put("imgProd", imagemProduto);
            params.put("novaImg", "nada");
        }
        params.put("update", "update");
        params.put("id_produto", id);
        params.put("nome", nome_produto.getText().toString().trim());
        params.put("tipo", categoriaProd.getSelectedItem().toString());
        params.put("disponivel_estoque", disponivel_estoque.getText().toString().trim());
        params.put("min_estoque", min_estoque.getText().toString().trim());
        params.put("max_estoque", max_estoque.getText().toString().trim());
        params.put("peso_liquido", peso_liquido.getText().toString().trim());
        params.put("peso_bruto", peso_bruto.getText().toString().trim());
        params.put("valor_venda", valor_venda.getText().toString().trim());
        params.put("valor_custo", valor_custo.getText().toString().trim());
        idFornecedor = fornecedores.get(fornecedor.getSelectedItemPosition()).getId();
        params.put("id_fornecedor", idFornecedor);


        StringRequest stringRequest = CRUD.editar("https://limopestoques.com.br/Android/Update/updateProduto.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(EditarProduto.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(EditarProduto.this, Produtos_Compras.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(EditarProduto.this);
        requestQueue.add(stringRequest);
    }
    public void carregarFornecedor(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://limopestoques.com.br/Android/Json/jsonFornecedores.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray forncompraArray = obj.getJSONArray("fornecedores");

                            for (int i = 0; i < forncompraArray.length(); i++){
                                JSONObject forncompraObject = forncompraArray.getJSONObject(i);

                                FornCompraConst forneCompra = new FornCompraConst(forncompraObject.getString("id_fornecedor"), forncompraObject.getString("nome_fornecedor"), forncompraObject.getString("tipo"), forncompraObject.getString("telefone_comercial"));

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

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(EditarProduto.this, Produtos_Compras.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
