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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import br.com.limopestoques.limop.CRUD.CRUD;

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

public class InsertUsuario extends AppCompatActivity {

    EditText nome,email,senha,confirmar_senha,data_nascimento;
    Spinner  tipo;
    RadioGroup sexoGroup;
    Button   button;
    String sexo,senhaa;
    ImageView iv;
    private Bitmap img;
    String imagemUsuario = null;

    private static final int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_usuario);

        nome              = (EditText)findViewById(R.id.nome);
        email             = (EditText)findViewById(R.id.email);
        senha             = (EditText)findViewById(R.id.senha);
        confirmar_senha   = (EditText)findViewById(R.id.confirmar_senha);
        data_nascimento   = (EditText)findViewById(R.id.data_nascimento);
        tipo              = (Spinner)findViewById(R.id.tipo);
        sexoGroup         = (RadioGroup)findViewById(R.id.sexo);
        iv                = findViewById(R.id.img);
        button            = (Button)findViewById(R.id.button);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                startActivityForResult(photoPicker, GALLERY_REQUEST);
            }
        });

        //Mascaras
        MaskEditTextChangedListener maskData = new MaskEditTextChangedListener("##/##/####",data_nascimento);
        data_nascimento.addTextChangedListener(maskData);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode, resultCode, data);

        if(resultCode == RESULT_OK && reqCode == GALLERY_REQUEST){
            try{
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                img = BitmapFactory.decodeStream(imageStream);
                iv.setImageBitmap(img);
                imagemUsuario = getStringImage(img);
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

    public void validarCampos(View v){

        if(nome.getText().length() == 0 || email.getText().length() == 0 || senha.getText().length() == 0 || confirmar_senha.getText().length() == 0 || data_nascimento.getText().length() == 0 || sexoGroup.getCheckedRadioButtonId() < 0 || imagemUsuario == null){
            Toast.makeText(this, getString(R.string.preenchaoscamposcorretamente), Toast.LENGTH_SHORT).show();
        }else{
            insertUsuario();
        }
    }

    public void insertUsuario() {
        Map<String, String> params = new HashMap<String, String>();

        if(sexoGroup.getCheckedRadioButtonId() == R.id.masc){
            sexo = "Masculino";
        }else{
            sexo = "Feminino";
        }

        if(senha.getText().toString().equals(confirmar_senha.getText().toString())){
            if(senha.getText().length() < 7){
                Toast.makeText(this, getString(R.string.errosenha), Toast.LENGTH_SHORT).show();
            }else{
                senhaa = senha.getText().toString();
            }

        }else{
            Toast.makeText(this, getString(R.string.errosenha2), Toast.LENGTH_SHORT).show();
        }

        //Formatar data
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        ParsePosition pos = new ParsePosition(0);
        Date data = formato.parse(data_nascimento.getText().toString(),pos);
        formato = new SimpleDateFormat("yyyy-MM-dd");
        String date = formato.format(data);

        //Enviar dados para o Insert
        params.put("img", imagemUsuario);
        params.put("nome", nome.getText().toString().trim());
        params.put("email", email.getText().toString().trim());
        params.put("senha", senhaa);
        params.put("sexo", sexo);
        params.put("nascimento", date);
        params.put("tipo", tipo.getSelectedItem().toString());

        CRUD.inserir("https://limopestoques.com.br/Android/Insert/InsertUsuario.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jo = new JSONObject(response);
                    String resposta = jo.getString("resposta");
                    Toast.makeText(InsertUsuario.this, resposta, Toast.LENGTH_SHORT).show();
                    Intent irTela = new Intent(InsertUsuario.this, Usuarios.class);
                    startActivity(irTela);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },params,getApplicationContext());

    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent irTela = new Intent(InsertUsuario.this, Usuarios.class);
        irTela.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(irTela);
    }

}
