package br.com.limopestoques.limop;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    public void PedidosSolicitados(View v){
       Intent i = new Intent(Relatorios.this, RelatorioPedidosSolicitados.class);
       startActivity(i);
    }
    public void ItensVendidos(View v){
        Intent i = new Intent(Relatorios.this, RelatorioItensVendidos.class);
        startActivity(i);
    }
}
