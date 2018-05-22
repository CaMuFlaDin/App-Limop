package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import br.com.limopestoques.limop.CRUD.CRUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import br.com.limopestoques.limop.Sessao.Sessao;

public class InsertTransportadora extends AppCompatActivity {

    EditText nome,cnpj,razao,inscricao,cpf,rg,email,tel_comercial,tel_celular,cep,cidade,
            estado,bairro,rua,numero,complemento,obs,valor_frete;
    Spinner tipo;
    Button button;
    Sessao sessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_transportadora);

        nome              = (EditText)findViewById(R.id.nome);
        cnpj              = (EditText)findViewById(R.id.cnpj);
        razao             = (EditText)findViewById(R.id.razao);
        inscricao         = (EditText)findViewById(R.id.inscricao);
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
        valor_frete       = (EditText)findViewById(R.id.valor_frete);
        tipo              = (Spinner)findViewById(R.id.tipo);
        button            = (Button)findViewById(R.id.button);

        sessao = new Sessao(InsertTransportadora.this);

        MaskEditTextChangedListener maskCEP = new MaskEditTextChangedListener("##.###-###",cep);
        MaskEditTextChangedListener maskCNPJ = new MaskEditTextChangedListener("##.###.###/####-##",cnpj);
        MaskEditTextChangedListener maskINSC = new MaskEditTextChangedListener("###.###.###.###",inscricao);
        MaskEditTextChangedListener maskTELCE = new MaskEditTextChangedListener("(##)#####-####",tel_celular);
        MaskEditTextChangedListener maskTELCO = new MaskEditTextChangedListener("(##)####-####",tel_comercial);

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
                                if(!objeto.getBoolean("erro")){
                                    String enderecoO = objeto.getString("logradouro"), cidadeO = objeto.getString("localidade"),
                                            estadoO = objeto.getString("uf"),bairroO = objeto.getString("bairro");

                                    rua.setText(enderecoO);
                                    cidade.setText(cidadeO);
                                    estado.setText(estadoO);
                                    bairro.setText(bairroO);
                                }else{
                                    Toast.makeText(InsertTransportadora.this, "CEP Inválido!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(InsertTransportadora.this, "CEP Inválido!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    RequestQueue rq = Volley.newRequestQueue(InsertTransportadora.this);
                    rq.add(sr);
                }
            }
        });

    }

    public void validarCampos(View v){
        if(nome.getText().length() == 0 || cnpj.getText().length() == 0 || razao.getText().length() == 0 || inscricao.getText().length() == 0 || email.getText().length() == 0 || tel_celular.getText().length() == 0 || tel_comercial.getText().length() == 0
                || cep.getText().length() == 0 || estado.getText().length() == 0 || cidade.getText().length() == 0 || bairro.getText().length() == 0 || rua.getText().length() == 0
                || numero.getText().length() == 0 || valor_frete.getText().length() == 0){
            Toast.makeText(this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();
        }else{
           insertTransportadora();
        }
    }

    public void insertTransportadora() {
        Map<String, String> params = new HashMap<String, String>();

        String id_usuario = sessao.getString("id_usuario");

        params.put("nome", nome.getText().toString().trim());
        params.put("cnpj", cnpj.getText().toString().trim());
        params.put("razao", razao.getText().toString().trim());
        params.put("inscricao", inscricao.getText().toString().trim());
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
        params.put("valor_frete", valor_frete.getText().toString().trim());
        params.put("id_usuario", id_usuario);

        CRUD.inserir("https://limopestoques.com.br/Android/Insert/InsertTransportadora.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(InsertTransportadora.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(InsertTransportadora.this, Transportadoras_Compras.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());

    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(InsertTransportadora.this, Transportadoras_Compras.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }

}
