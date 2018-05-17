package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import br.com.limopestoques.limop.CRUD.CRUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class EditarFornecedor extends AppCompatActivity {


    private String id;

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Update/updateFornecedor.php";

    EditText nome;
    Spinner  tipo;
    EditText cnpj;
    EditText razao;
    EditText inscricao;
    EditText cpf;
    EditText rg;
    EditText email;
    EditText tel_comercial;
    EditText tel_celular;
    EditText cep;
    EditText estado;
    EditText cidade;
    EditText bairro;
    EditText rua;
    EditText numero;
    EditText complemento;
    EditText obs;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_fornecedor);

        nome           = findViewById(R.id.nome);
        tipo           = findViewById(R.id.tipo);
        cnpj           = findViewById(R.id.cnpj);
        razao          = findViewById(R.id.razao);
        inscricao      = findViewById(R.id.inscricao);
        cpf            = findViewById(R.id.cpf);
        rg             = findViewById(R.id.rg);
        email          = findViewById(R.id.email);
        tel_comercial  = findViewById(R.id.tel_comercial);
        tel_celular    = findViewById(R.id.tel_celular);
        cep            = findViewById(R.id.cep);
        estado         = findViewById(R.id.estado);
        cidade         = findViewById(R.id.cidade);
        bairro         = findViewById(R.id.bairro);
        rua            = findViewById(R.id.rua);
        numero         = findViewById(R.id.numero);
        complemento    = findViewById(R.id.complemento);
        obs            = findViewById(R.id.obs);
        btn            = findViewById(R.id.button);

        MaskEditTextChangedListener maskCPF = new MaskEditTextChangedListener("###.###.###-##",cpf);
        MaskEditTextChangedListener maskCEP = new MaskEditTextChangedListener("##.###-###",cep);
        MaskEditTextChangedListener maskCNPJ = new MaskEditTextChangedListener("##.###.###/####-##",cnpj);
        MaskEditTextChangedListener maskINSC = new MaskEditTextChangedListener("###.###.###.###",inscricao);
        MaskEditTextChangedListener maskTELCE = new MaskEditTextChangedListener("(##)#####-####",tel_celular);
        MaskEditTextChangedListener maskTELCO = new MaskEditTextChangedListener("(##)####-####",tel_comercial);

        cpf.addTextChangedListener(maskCPF);
        cep.addTextChangedListener(maskCEP);
        cnpj.addTextChangedListener(maskCNPJ);
        inscricao.addTextChangedListener(maskINSC);
        tel_celular.addTextChangedListener(maskTELCE);
        tel_comercial.addTextChangedListener(maskTELCO);

        cep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String sendCep = cep.getText().toString();
                    sendCep = sendCep.replace(".", "");
                    sendCep = sendCep.replace("-", "");
                    String url = "https://viacep.com.br/ws/"+sendCep+"/json/unicode/";
                    StringRequest sr = new StringRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject objeto = new JSONObject(response);
                                String enderecoO = objeto.getString("logradouro"), cidadeO = objeto.getString("localidade"),
                                        estadoO = objeto.getString("uf"),bairroO = objeto.getString("bairro");

                                rua.setText(enderecoO);
                                cidade.setText(cidadeO);
                                estado.setText(estadoO);
                                bairro.setText(bairroO);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(EditarFornecedor.this, "CEP Inválido!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue rq = Volley.newRequestQueue(EditarFornecedor.this);
                    rq.add(sr);
                }
            }
        });

        try{
            Intent i = getIntent();
            this.id = i.getStringExtra("id");
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    cpf.setVisibility(View.VISIBLE);
                    rg.setVisibility(View.VISIBLE);

                    cnpj.setText("");
                    inscricao.setText("");
                    razao.setText("");

                    cnpj.setVisibility(View.GONE);
                    inscricao.setVisibility(View.GONE);
                    razao.setVisibility(View.GONE);

                }else{
                    cpf.setVisibility(View.GONE);
                    rg.setVisibility(View.GONE);

                    rg.setText("");
                    cpf.setText("");

                    cnpj.setVisibility(View.VISIBLE);
                    inscricao.setVisibility(View.VISIBLE);
                    razao.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        carregar();
    }
    public void carregar(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray servicocompraArray = obj.getJSONArray("fornecedor");
                            JSONObject jo = servicocompraArray.getJSONObject(0);

                            nome.setText(jo.getString("nome_fornecedor"));
                            String tipoPessoa = jo.getString("tipo");
                            cnpj.setText(jo.getString("cnpj"));
                            razao.setText(jo.getString("razao_social"));
                            inscricao.setText(jo.getString("inscricao_estadual"));
                            cpf.setText(jo.getString("cpf"));
                            rg.setText(jo.getString("rg"));
                            email.setText(jo.getString("email"));
                            tel_comercial.setText(jo.getString("telefone_comercial"));
                            tel_celular.setText(jo.getString("telefone_celular"));
                            cep.setText(jo.getString("cep"));
                            estado.setText(jo.getString("estado"));
                            cidade.setText(jo.getString("cidade"));
                            bairro.setText(jo.getString("bairro"));
                            rua.setText(jo.getString("rua"));
                            numero.setText(jo.getString("numero"));
                            complemento.setText(jo.getString("complemento"));
                            obs.setText(jo.getString("observacoes"));

                            if(tipoPessoa.equals("Física")){
                                tipo.setSelection(0);
                            }else{
                                tipo.setSelection(1);
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
                params.put("id_fornecedor", id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void verificarCampos(View v){
        if(nome.getText().length() == 0 || email.getText().length() == 0 || tel_celular.getText().length() == 0 || tel_comercial.getText().length() == 0
                || cep.getText().length() == 0 || estado.getText().length() == 0 || cidade.getText().length() == 0 || bairro.getText().length() == 0 || rua.getText().length() == 0
                || numero.getText().length() == 0){
            Toast.makeText(this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();
        }else{
            if(tipo.getSelectedItemPosition() == 0){
                if(cpf.getText().length() != 0 && rg.getText().length() != 0){
                    updateFornecedor();
                }else{
                    Toast.makeText(this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();
                }
            }else{
                if(cnpj.getText().length() != 0 && inscricao.getText().length() != 0 && razao.getText().length() != 0){
                    updateFornecedor();
                }else{
                    Toast.makeText(this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void updateFornecedor() {
        Map<String, String> params = new HashMap<String, String>();

        params.put("update", "update");
        params.put("id_fornecedor", id);
        params.put("tipo",tipo.getSelectedItem().toString());
        params.put("nome_fornecedor", nome.getText().toString().trim());
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

        StringRequest stringRequest = CRUD.editar("https://limopestoques.com.br/Android/Update/updateFornecedor.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(EditarFornecedor.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(EditarFornecedor.this, Fornecedores_compras.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(EditarFornecedor.this, response, Toast.LENGTH_SHORT).show();
            }
        },params,getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(EditarFornecedor.this);
        requestQueue.add(stringRequest);

    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(EditarFornecedor.this, Fornecedores_compras.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}

