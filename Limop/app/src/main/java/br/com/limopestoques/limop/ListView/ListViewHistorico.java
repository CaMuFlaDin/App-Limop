package br.com.limopestoques.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.limopestoques.limop.Construtoras.HistoricoConst;
import br.com.limopestoques.limop.Construtoras.ServicosVendasConst;
import br.com.limopestoques.limop.R;

/**
 * Created by Aluno on 18/05/2018.
 */

public class ListViewHistorico extends ArrayAdapter<HistoricoConst> {

    //Lista
    private List<HistoricoConst> historicolist;

    //Contexto
    private Context mCtx;

    public ListViewHistorico(List<HistoricoConst> servicocompraList, Context mCtx){
        super(mCtx, R.layout.list_view_historico, servicocompraList);
        this.historicolist = servicocompraList;
        this.mCtx = mCtx;
    }


    @Override
    public int getCount() {
        return historicolist.size();
    }

    //Dados para a listagem
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.list_view_historico, null, true);

        TextView txtData = listViewItem.findViewById(R.id.txtData);
        TextView txtInter = listViewItem.findViewById(R.id.txtInter);
        TextView txtQtd = listViewItem.findViewById(R.id.txtQtd);
        TextView txtValor = listViewItem.findViewById(R.id.txtValor);
        TextView txtTipo = listViewItem.findViewById(R.id.txtTipo);
        View view = listViewItem.findViewById(R.id.view);

        HistoricoConst historico = historicolist.get(position);


        txtData.setText(historico.getData());
        txtInter.setText(historico.getInter());
        txtQtd.setText(historico.getQtd());
        txtValor.setText(historico.getValor());
        txtTipo.setText(historico.getTipo());

        if(historico.getTipo().equals("Entrada")){
            view.setBackgroundColor(mCtx.getResources().getColor(R.color.verdelhoso));
        }else{
            view.setBackgroundColor(mCtx.getResources().getColor(R.color.vermelhao));
        }

        return listViewItem;
    }
}
