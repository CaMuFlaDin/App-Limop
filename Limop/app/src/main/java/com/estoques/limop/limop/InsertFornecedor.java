package com.estoques.limop.limop;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.estoques.limop.limop.CRUD.CRUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InsertFornecedor extends AppCompatActivity {

    EditText nome,cnpj,razao,inscricao,cpf,rg,email,tel_comercial,tel_celular,cep,cidade,
            estado,bairro,rua,numero,complemento,obs;
    Spinner  tipo;
    Button   button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_fornecedor);

        nome              = (EditText)findViewById(R.id.nome);
        cnpj              = (EditText)findViewById(R.id.cnpj);
        razao             = (EditText)findViewById(R.id.razao);
        inscricao         = (EditText)findViewById(R.id.inscricao);
        cpf               = (EditText)findViewById(R.id.cpf);
        rg                = (EditText)findViewById(R.id.rg);
        email             = (EditText)findViewById(R.id.email);
        tel_celular       = (EditText)findViewById(R.id.tel_celular);
        tel_comercial     = (EditText)findViewById(R.id.tel_comercial);
        cep               = (EditText)findViewById(R.id.cep);
        estado            = (EditText)findViewById(R.id.estado);
        cidade            = (EditText)findViewById(R.id.cidade);
        bairro            = (EditText)findViewById(R.id.bairro);
        rua               = (EditText)findViewById(R.id.rua);
        numero            = (EditText)findViewById(R.id.numero);
        complemento       = (EditText)findViewById(R.id.complemento);
        obs               = (EditText)findViewById(R.id.obs);
        tipo              = (Spinner)findViewById(R.id.tipo);
        button            = (Button)findViewById(R.id.button);

        /*tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    cnpj.setFocusable(false);
                    inscricao.setFocusable(false);
                    razao.setFocusable(false);
                    cnpj.setText("");
                    inscricao.setText("");
                    razao.setText("");

                    cpf.setFocusable(true);
                    rg.setFocusable(true);
                }else{
                    cnpj.setFocusable(true);
                    inscricao.setFocusable(true);
                    razao.setFocusable(true);

                    cpf.setText("");
                    rg.setText("");

                    cpf.setFocusable(false);
                    rg.setFocusable(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
    }

    public void insertFornecedor(View v) {
        Map<String, String> params = new HashMap<String, String>();

        params.put("nome", nome.getText().toString().trim());
        params.put("cnpj", cnpj.getText().toString().trim());
        params.put("razao", razao.getText().toString().trim());
        params.put("inscricao", inscricao.getText().toString().trim());
        params.put("cpf", cpf.getText().toString().trim());
        params.put("rg", rg.getText().toString().trim());
        params.put("email", email.getText().toString().trim());
        params.put("tel_comercial", tel_comercial.getText().toString().trim());
        params.put("tel_celular", tel_celular.getText().toString().trim());
        params.put("cep", cep.getText().toString().trim());
        params.put("estado", estado.getText().toString().trim());
        params.put("cidade", cidade.getText().toString().trim());
        params.put("bairro", bairro.getText().toString().trim());
        params.put("rua", rua.getText().toString().trim());
        params.put("numero", numero.getText().toString().trim());
        params.put("complemento", complemento.getText().toString().trim());
        params.put("obs", obs.getText().toString().trim());
        params.put("tipo", tipo.getSelectedItem().toString());

        CRUD.inserir("https://limopestoques.com.br/Android/Insert/InsertFornecedor.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(InsertFornecedor.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(InsertFornecedor.this, Fornecedores_compras.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());

    }

}
