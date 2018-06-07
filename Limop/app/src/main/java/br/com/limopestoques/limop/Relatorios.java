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

    static final int oqueeuquero = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);
    }

    public void DemonsFin(View v){
        Toast.makeText(this, "Em breve!", Toast.LENGTH_SHORT).show();
    }
    public void PedidosSolicitados(View v){
       Intent i = new Intent(Relatorios.this, RelatorioPedidosSolicitados.class);
       startActivity(i);
    }
    public void ItensVendidos(View v){
        if(checkPermissions()){
            downloadRelatorio();
        }
    }

    public boolean checkPermissions(){
        int readPermission = ContextCompat.checkSelfPermission(Relatorios.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(readPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }else{
            ActivityCompat.requestPermissions(Relatorios.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, oqueeuquero);
        }
        return false;
    }

    public void downloadRelatorio(){
        String url = "https://www.limopestoques.com.br/Android/Relatorios/itensVendidos.php";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Relatório Itens mais Vendidos");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Relatório Itens mais Vendidos");

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
        }
    }
}
