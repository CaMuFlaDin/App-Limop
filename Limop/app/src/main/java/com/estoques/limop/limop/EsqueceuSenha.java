package com.estoques.limop.limop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EsqueceuSenha extends AppCompatActivity {

    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);
        email = (EditText)findViewById(R.id.email_esq);
    }

    public void campoVazioSenha(View v){
        if(email.getText().length() == 0) {
            Toast.makeText(this, R.string.camposVazios,Toast.LENGTH_SHORT).show();
        }
    }
}
