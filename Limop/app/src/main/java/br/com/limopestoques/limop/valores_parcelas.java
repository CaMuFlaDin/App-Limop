package br.com.limopestoques.limop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class valores_parcelas extends AppCompatActivity {


    TextView valoratrasado, contatrasado, valorreceber, contreceber, valorrecebido,contrecebido, valortotal, conttotal;

    Double valorAtrasado;
    Double valorReceber;
    Double valorRecebido;
    Double valorTotal;
    Integer contAtrasado;
    Integer contReceber;
    Integer contRecebido;
    Integer contTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valores_parcelas);

        valoratrasado = findViewById(R.id.textViewName1);
        contatrasado = findViewById(R.id.textViewVersion1);
        valorreceber = findViewById(R.id.textViewName2);
        contreceber = findViewById(R.id.textViewVersion2);
        valorrecebido = findViewById(R.id.textViewName3);
        contrecebido = findViewById(R.id.textViewVersion3);
        valortotal = findViewById(R.id.textViewName4);
        conttotal = findViewById(R.id.textViewVersion4);

        try{
            Intent i = getIntent();
            this.valorAtrasado = i.getDoubleExtra("valorAtrasado", 0.0);
            this.valorReceber = i.getDoubleExtra("valorReceber",0.0);
            this.valorRecebido = i.getDoubleExtra("valorRecebido",0.0);
            this.contAtrasado = i.getIntExtra("contAtrasado",0);
            this.contReceber = i.getIntExtra("contReceber", 0);
            this.contRecebido = i.getIntExtra("contRecebido", 0);
        }catch(NullPointerException e){
            e.printStackTrace();
        }

        contTotal = contAtrasado + contReceber + contRecebido;

        contatrasado.setText("Em atraso("+contAtrasado+")");
        contreceber.setText("A Receber("+contReceber+")");
        contrecebido.setText("Recebido("+contRecebido+")");
        conttotal.setText("Total("+contTotal+")");

        valorTotal = valorAtrasado + valorReceber + valorRecebido;

        valoratrasado.setText("R$ "+String.format("%.2f", valorAtrasado));
        valorreceber.setText("R$ "+String.format("%.2f", valorReceber));
        valorrecebido.setText("R$ "+String.format("%.2f", valorRecebido));
        valortotal.setText("R$ "+String.format("%.2f", valorTotal));

    }
}
