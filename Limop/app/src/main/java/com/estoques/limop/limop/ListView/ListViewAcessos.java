package com.estoques.limop.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.estoques.limop.limop.Construtoras.AcessosConst;
import com.estoques.limop.limop.R;

import java.util.List;

/**
 * Created by User on 04/03/2018.
 */

public class ListViewAcessos extends ArrayAdapter<AcessosConst> {

    private List<AcessosConst> acessosList;

    private Context mCtx;

    public ListViewAcessos(List<AcessosConst> acessosList, Context mCtx){
        super(mCtx, R.layout.list_view_acessos, acessosList);
        this.acessosList = acessosList;
        this.mCtx = mCtx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.list_view_acessos, null, true);

        TextView txtid = listViewItem.findViewById(R.id.txtUser);
        TextView txtname = listViewItem.findViewById(R.id.txtQtd);
        TextView txttipo = listViewItem.findViewById(R.id.txtData);

        AcessosConst usuariosConst = acessosList.get(position);

        txtid.setText(usuariosConst.getUser());
        txtname.setText(usuariosConst.getQtd());
        txttipo.setText(usuariosConst.getData());

        return listViewItem;
    }
}
