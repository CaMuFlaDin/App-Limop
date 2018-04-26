package br.com.limopestoques.limop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class Graficos extends AppCompatActivity {

    WebView charts;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        url = "https://limopestoques.com.br/Android/graficos/graficos.php";
        charts = findViewById(R.id.webview);

        WebSettings graficos = charts.getSettings();
        graficos.setJavaScriptEnabled(true);
        graficos.setBuiltInZoomControls(true);

        carregarGraficos();
    }

    private void carregarGraficos(){
        try{
            charts.loadUrl(url);
        }catch (NullPointerException e){
           Toast.makeText(this, "Nenhum registro encontrado!", Toast.LENGTH_SHORT).show();
        }

    }
}
