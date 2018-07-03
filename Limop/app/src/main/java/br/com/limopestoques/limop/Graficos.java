package br.com.limopestoques.limop;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;
import br.com.limopestoques.limop.CRUD.CRUD;

public class Graficos extends AppCompatActivity {

    EditText data_inicio, data_fim;
    RadioGroup tipoRelatorio;
    RadioButton semanal,mensal, anual;
    TextView tv1,tv2,tv3,tv4;
    Spinner ano,mes;
    Button btn;

    String TipoRelatorio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

        tipoRelatorio = findViewById(R.id.tipo);
        semanal       = findViewById(R.id.semanal2);
        mensal        = findViewById(R.id.mensal2);
        anual         = findViewById(R.id.anual2);
        data_inicio   = findViewById(R.id.Data_inicio);
        data_fim      = findViewById(R.id.Data_final);
        tv1           = findViewById(R.id.tv1);
        tv2           = findViewById(R.id.tv2);
        tv3           = findViewById(R.id.tv3);
        tv4           = findViewById(R.id.tv4);
        ano           = findViewById(R.id.anual);
        mes           = findViewById(R.id.mensal);
        btn           = findViewById(R.id.button);

        btn.setEnabled(false);

        //Mascaras
        MaskEditTextChangedListener maskDataI = new MaskEditTextChangedListener("##/##/####",data_inicio);
        data_inicio.addTextChangedListener(maskDataI);

        MaskEditTextChangedListener maskDataF = new MaskEditTextChangedListener("##/##/####",data_fim);
        data_fim.addTextChangedListener(maskDataF);

        tipoRelatorio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(tipoRelatorio.getCheckedRadioButtonId() == R.id.semanal2){
                    tv1.setVisibility(View.GONE);
                    mes.setVisibility(View.GONE);
                    tv2.setVisibility(View.GONE);
                    ano.setVisibility(View.GONE);
                    tv3.setVisibility(View.VISIBLE);
                    tv4.setVisibility(View.VISIBLE);
                    data_inicio.setVisibility(View.VISIBLE);
                    data_inicio.setText("");
                    data_fim.setText("");
                    data_fim.setVisibility(View.VISIBLE);
                    TipoRelatorio = "semanal";
                    btn.setEnabled(true);
                }else if(tipoRelatorio.getCheckedRadioButtonId() == R.id.mensal2){
                    tv2.setVisibility(View.GONE);
                    tv1.setVisibility(View.VISIBLE);
                    ano.setVisibility(View.GONE);
                    tv3.setVisibility(View.GONE);
                    tv4.setVisibility(View.GONE);
                    data_inicio.setVisibility(View.GONE);
                    data_inicio.setText("00/00/0000");
                    data_fim.setVisibility(View.GONE);
                    data_fim.setText("00/00/0000");
                    mes.setVisibility(View.VISIBLE);
                    TipoRelatorio = "mensal";
                    btn.setEnabled(true);
                }else{
                    tv1.setVisibility(View.GONE);
                    mes.setVisibility(View.GONE);
                    tv3.setVisibility(View.GONE);
                    tv4.setVisibility(View.GONE);
                    tv2.setVisibility(View.VISIBLE);
                    data_inicio.setVisibility(View.GONE);
                    data_inicio.setText("00/00/0000");
                    data_fim.setVisibility(View.GONE);
                    data_fim.setText("00/00/0000");
                    ano.setVisibility(View.VISIBLE);
                    TipoRelatorio = "anual";
                    btn.setEnabled(true);
                }
            }
        });

    }

    public void gerar(View v) {

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        ParsePosition pos = new ParsePosition(0);
        Date data = formato.parse(data_inicio.getText().toString(),pos);
        formato = new SimpleDateFormat("yyyy-MM-dd");
        String dataInicio = formato.format(data);

        SimpleDateFormat formato2 = new SimpleDateFormat("dd/MM/yyyy");
        ParsePosition pos2 = new ParsePosition(0);
        Date data2 = formato2.parse(data_fim.getText().toString(),pos2);
        formato2 = new SimpleDateFormat("yyyy-MM-dd");
        String dataFinal = formato2.format(data2);

        Integer Mes;
        Integer Ano;

        if(mes.getSelectedItemPosition() == 0){
            Mes = 1;
        }else if(mes.getSelectedItemPosition() == 1){
            Mes = 2;
        }else if(mes.getSelectedItemPosition() == 2){
            Mes = 3;
        }else if(mes.getSelectedItemPosition() == 3){
            Mes = 4;
        }else if(mes.getSelectedItemPosition() == 4){
            Mes = 5;
        }else if(mes.getSelectedItemPosition() == 5){
            Mes = 6;
        }else if(mes.getSelectedItemPosition() == 6){
            Mes = 7;
        }else if(mes.getSelectedItemPosition() == 7){
            Mes = 8;
        }else if(mes.getSelectedItemPosition() == 8){
            Mes = 9;
        }else if(mes.getSelectedItemPosition() == 9){
            Mes = 10;
        }else if(mes.getSelectedItemPosition() == 10){
            Mes = 11;
        }else{
            Mes = 12;
        }

        if(ano.getSelectedItemPosition() == 0){
            Ano = 2017;
        }else{
            Ano = 2018;
        }

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://limopestoques.com.br/Android/graficos/graficos.php?tipoRelatorio=" + TipoRelatorio+"&mes=" +Mes+"&ano="+Ano+"&data_inicial="+dataInicio+"&data_final="+dataFinal));
        startActivity(i);

    }
}


