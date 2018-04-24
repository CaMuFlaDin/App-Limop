package com.estoques.limop.limop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.estoques.limop.limop.Sessao.Sessao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText senha;

    Sessao sessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);

        sessao = new Sessao(MainActivity.this);

        if(sessao.getBoolean("login")){
            Intent irTela = new Intent(MainActivity.this, Principal.class);
            startActivity(irTela);
        }
    }

    public boolean verificarCampo(){
        if(email.getText().length() == 0 || senha.getText().length() == 0){

            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.layout),
                    R.string.camposVazios, Snackbar.LENGTH_SHORT);
            mySnackbar.show();
            return false;
        }else{
            return true;
        }
    }

    public void Login(View v){
        final String email = this.email.getText().toString().trim();
        final String senha = this.senha.getText().toString().trim();

        StringRequest sr = new StringRequest(Request.Method.POST, "https://limopestoques.com.br/Android/login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    boolean valido = jo.getBoolean("validar");
                    if (valido) {
                        JSONObject quei = jo.getJSONObject("resposta");

                        String id_usuario = quei.getString("id_usuario");
                        String email = quei.getString("email");
                        String foto = quei.getString("foto");
                        String tipo = quei.getString("tipo");

                        sessao.setBoolean("login", true);
                        sessao.setString("id_usuario", id_usuario);

                        Intent irTela = new Intent(MainActivity.this, Principal.class);
                        startActivity(irTela);

                    }else{
                        Snackbar.make(findViewById(R.id.layout), "Usuário não encontrado", Snackbar.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            protected Map<String,String> getParams() throws com.android.volley.AuthFailureError{
                Map<String,String> params = new HashMap<String,String>();
                params.put("email",email);
                params.put("senha",senha);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(sr);

    }

    public void Contato(View v) {
        Intent irTela = new Intent(MainActivity.this, Contato.class);
        startActivity(irTela);
    }

    public void EsqSenha(View v){
        Intent irTela = new Intent(MainActivity.this, EsqueceuSenha.class);
        startActivity(irTela);
    }

    // TODO FAZER ONBACK

}
