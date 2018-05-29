package br.com.limopestoques.limop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Relatorios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);
    }

    public void DemonsFin(View v){
        Toast.makeText(this, "Em breve!", Toast.LENGTH_SHORT).show();
    }
    public void PedidosSolicitados(View v){
        Toast.makeText(this, "Em breve!", Toast.LENGTH_SHORT).show();
    }
    public void ItensVendidos(View v){
        Toast.makeText(this, "Calma ae!", Toast.LENGTH_SHORT).show();
    }
}
