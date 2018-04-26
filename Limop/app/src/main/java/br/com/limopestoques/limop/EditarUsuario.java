package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class EditarUsuario extends AppCompatActivity {

    private String id;

    private static final String JSON_URL = "https://limopestoques.com.br/Android/Update/updateUsuario.php";

    EditText nome;
    Spinner tipo;
    EditText foto;
    EditText email;
    EditText data_nascimento;
    RadioButton masculino;
    RadioButton feminino;
    RadioGroup sexo;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        nome            = findViewById(R.id.nome);
        tipo            = findViewById(R.id.tipo);
        foto            = findViewById(R.id.foto);
        email           = findViewById(R.id.email);
        data_nascimento = findViewById(R.id.data_nascimento);
        masculino       = findViewById(R.id.masc);
        feminino        = findViewById(R.id.fem);
        sexo            = findViewById(R.id.sexo);
        btn             = findViewById(R.id.button);

        try{
            Intent i = getIntent();
            this.id = i.getStringExtra("id");
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        carregar();
    }
    public void carregar(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            JSONArray servicocompraArray = obj.getJSONArray("usuario");
                            JSONObject jo = servicocompraArray.getJSONObject(0);
                            String sexoUser;

                            nome.setText(jo.getString("nome_usuario"));
                            String tipoUser = jo.getString("tipo");
                            //foto.setText(jo.getString("foto"));
                            email.setText(jo.getString("email"));
                            sexoUser = jo.getString("sexo");
                            data_nascimento.setText(jo.getString("data_nascimento"));

                            if(tipoUser.equals("Administrador")){
                                tipo.setSelection(0);
                            }else{
                                tipo.setSelection(1);
                            }

                            if(sexoUser.equals("Masculino")){
                                masculino.setChecked(true);
                            }else{
                                feminino.setChecked(true);
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
                String sexoPessoa;
                if(sexo.getCheckedRadioButtonId() == R.id.masc){
                    sexoPessoa = "Masculino";
                }else{
                    sexoPessoa = "Feminino";
                }

                Map<String, String> params = new HashMap<String, String>();

                params.put("select", "select");
                params.put("id_usuario", id);
                params.put("nome", nome.getText().toString().trim());
                params.put("tipo", tipo.getSelectedItem().toString());
                //params.put("foto", foto.getText().toString().trim());
                params.put("email",email.getText().toString().trim());
                params.put("sexo", sexoPessoa);
                params.put("data_nascimento", data_nascimento.getText().toString().trim());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void updateUsuario(View v) {
        String sexoPessoa;
        if(sexo.getCheckedRadioButtonId() == R.id.masc){
            sexoPessoa = "Masculino";
        }else{
            sexoPessoa = "Feminino";
        }

        Map<String, String> params = new HashMap<String, String>();

        params.put("update", "update");
        params.put("id_usuario", id);
        params.put("nome", nome.getText().toString().trim());
        params.put("tipo", tipo.getSelectedItem().toString());
        //params.put("foto", foto.getText().toString().trim());
        params.put("email",email.getText().toString().trim());
        params.put("sexo", sexoPessoa);
        params.put("data_nascimento", data_nascimento.getText().toString().trim());


        StringRequest stringRequest = CRUD.editar("https://limopestoques.com.br/Android/Update/updateUsuario.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(EditarUsuario.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(EditarUsuario.this, Usuarios.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(EditarUsuario.this, response, Toast.LENGTH_SHORT).show();
            }
        },params,getApplicationContext());
        RequestQueue requestQueue = Volley.newRequestQueue(EditarUsuario.this);
        requestQueue.add(stringRequest);

    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(EditarUsuario.this, Usuarios.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }
}
