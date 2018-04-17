package com.estoques.limop.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.estoques.limop.limop.Construtoras.TranspCompraConst;
import com.estoques.limop.limop.R;

import java.util.List;

/**
 * Created by User on 04/03/2018.
 */

public class ListViewTranspCompra extends ArrayAdapter<TranspCompraConst> {

    private List<TranspCompraConst> transpcompraList;

    private Context mCtx;

    public ListViewTranspCompra(List<TranspCompraConst> transpcompraList, Context mCtx){
        super(mCtx, R.layout.list_view_transportadoras_compras, transpcompraList);
        this.transpcompraList = transpcompraList;
        this.mCtx = mCtx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.list_view_transportadoras_compras, null, true);

        TextView txtid = listViewItem.findViewById(R.id.txtNome);
        TextView txtname = listViewItem.findViewById(R.id.txtEmail);
        TextView txttipo = listViewItem.findViewById(R.id.txtValor);

        TranspCompraConst usuariosConst = transpcompraList.get(position);

        txtid.setText(usuariosConst.getNome());
        txtname.setText(usuariosConst.getEmail());
        txttipo.setText(usuariosConst.getValor());

        return listViewItem;
    }
}
