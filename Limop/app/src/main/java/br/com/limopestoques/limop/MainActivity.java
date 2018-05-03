package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import br.com.limopestoques.limop.CRUD.CRUD;
import br.com.limopestoques.limop.Sessao.Sessao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText senha;

    CallbackManager callbackManager;
    LoginButton loginButton;

    Sessao sessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);

        loginButton = findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("email"));

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try{
                                    String email = object.getString("email");
                                    loginFB(email);
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        sessao = new Sessao(MainActivity.this);

        if(sessao.getBoolean("login")){
            Intent irTela = new Intent(MainActivity.this, Principal.class);
            startActivity(irTela);
        }
    }

    public void loginFB(String email){
        Map<String,String> params = new HashMap<String,String>();
        params.put("emailLogin", "emailLogin");
        params.put("email", email);

        StringRequest sr = CRUD.customRequest("https://limopestoques.com.br/Android/login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject obj = new JSONObject(response);
                    boolean validar = obj.getBoolean("validar");
                    if(validar) {
                        JSONObject objeto = obj.getJSONObject("resposta");
                        String id_usuario = objeto.getString("id_usuario");
                        String email = objeto.getString("email");
                        String foto = objeto.getString("foto");
                        String tipo = objeto.getString("tipo");

                        sessao.setBoolean("login", true);
                        sessao.setString("id_usuario", id_usuario);
                        
                        Intent irTela = new Intent(MainActivity.this, Principal.class);
                        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(irTela);
                    }else{
                        Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, MainActivity.this, params);
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);
        rq.add(sr);
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
                params.put("loginPadrao", "loginPadrao");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
