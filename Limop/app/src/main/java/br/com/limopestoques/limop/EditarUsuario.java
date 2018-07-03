package br.com.limopestoques.limop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import br.com.limopestoques.limop.CRUD.CRUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditarUsuario extends AppCompatActivity {

    private String id;

    //Json
    private static final String JSON_URL = "https://limopestoques.com.br/Android/Update/updateUsuario.php";

    EditText nome;
    Spinner tipo;
    EditText email;
    NetworkImageView niv;
    EditText data_nascimento;
    RadioButton masculino;
    RadioButton feminino;
    RadioGroup sexo;

    String imagem;

    Button btn;

    private static final int GALLERY_REQUEST = 1;
    private Bitmap img = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        nome            = findViewById(R.id.nome);
        tipo            = findViewById(R.id.tipo);
        email           = findViewById(R.id.email);
        data_nascimento = findViewById(R.id.data_nascimento);
        masculino       = findViewById(R.id.masc);
        feminino        = findViewById(R.id.fem);
        sexo            = findViewById(R.id.sexo);
        niv             = findViewById(R.id.img);
        btn             = findViewById(R.id.button);

        try{
            Intent i = getIntent();
            this.id = i.getStringExtra("id");
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        carregar();

        niv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                startActivityForResult(photoPicker, GALLERY_REQUEST);
            }
        });

        MaskEditTextChangedListener maskData = new MaskEditTextChangedListener("##/##/####",data_nascimento);
        data_nascimento.addTextChangedListener(maskData);
    }

    public void validarCampos(View v){
     if(nome.getText().length() == 0 || email.getText().length() == 0 || data_nascimento.getText().length() == 0 || sexo.getCheckedRadioButtonId() < 0){
        Toast.makeText(this, getString(R.string.preenchaoscamposcorretamente), Toast.LENGTH_SHORT).show();
    }else{
        updateUsuario();
    }
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
                Toast.makeText(this, getString(R.string.erroimagem), Toast.LENGTH_SHORT).show();
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

    public void carregar(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);

                            //Nome Json
                            JSONArray servicocompraArray = obj.getJSONArray("usuario");
                            JSONObject jo = servicocompraArray.getJSONObject(0);
                            String sexoUser;

                            //Inserir dados no EditText
                            nome.setText(jo.getString("nome_usuario"));
                            String tipoUser = jo.getString("tipo");
                            email.setText(jo.getString("email"));
                            sexoUser = jo.getString("sexo");

                            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                            ParsePosition pos = new ParsePosition(0);
                            Date data = formato.parse(jo.getString("data_nascimento"),pos);
                            formato = new SimpleDateFormat("dd/MM/yyyy");
                            String date = formato.format(data);

                            data_nascimento.setText(date);



                            imagem = jo.getString("foto");

                            ImageLoader il = Singleton.getInstance(EditarUsuario.this).getImageLoader();
                            niv.setImageUrl("https://limopestoques.com.br/Index_adm/usuarios/imgs/"+ imagem,il);

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
                params.put("email",email.getText().toString().trim());
                params.put("sexo", sexoPessoa);
                params.put("data_nascimento", data_nascimento.getText().toString().trim());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void updateUsuario() {
        String sexoPessoa;
        if(sexo.getCheckedRadioButtonId() == R.id.masc){
            sexoPessoa = "Masculino";
        }else{
            sexoPessoa = "Feminino";
        }

        Map<String, String> params = new HashMap<String, String>();

        if(img == null){
            params.put("img", imagem);
        }else{
            String imagemUsuario = getStringImage(img);
            params.put("img", imagemUsuario);
            params.put("novaImg", "nada");
        }

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        ParsePosition pos = new ParsePosition(0);
        Date data = formato.parse(data_nascimento.getText().toString(),pos);
        formato = new SimpleDateFormat("yyyy-MM-dd");
        String date = formato.format(data);

        params.put("update", "update");
        params.put("id_usuario", id);
        params.put("nome", nome.getText().toString().trim());
        params.put("tipo", tipo.getSelectedItem().toString());
        params.put("email",email.getText().toString().trim());
        params.put("sexo", sexoPessoa);
        params.put("data_nascimento", date);


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
