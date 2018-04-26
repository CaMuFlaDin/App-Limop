package br.com.limopestoques.limop.CRUD;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aluno on 08/03/2018.
 */

public class CRUD {
    public CRUD(){}

    public static StringRequest editar(String url, Response.Listener listener, final Map<String,String> params, final Context context){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Erro", Toast.LENGTH_SHORT).show();
            }
        }){
            protected Map<String,String> getParams() throws com.android.volley.AuthFailureError{
                return params;
            }
        };

        return stringRequest;
    }

    public static void excluir(String url, final String id, final Context contexto){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject resposta = new JSONObject(response);
                    boolean deletado = resposta.getBoolean("resposta");
                    if(deletado){
                        Toast.makeText(contexto, "Excluído com sucesso!", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(contexto, "Falha na exclusão!", Toast.LENGTH_LONG).show();
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contexto, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            //Adiciona os parâmetros
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deleteId", id);

                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(contexto);
        rq.add(stringRequest);
    }
    public static void inserir(String url, Response.Listener<String> responseListener, final Map<String, String> params, final Context contexto){
        StringRequest sr = new StringRequest(Request.Method.POST, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(contexto, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError{
                return params;
            }
        };
        RequestQueue rq = Volley.newRequestQueue(contexto);
        rq.add(sr);
    }
}
