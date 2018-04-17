package com.estoques.limop.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.estoques.limop.limop.Construtoras.FornCompraConst;
import com.estoques.limop.limop.R;

import java.util.List;

/**
 * Created by User on 04/03/2018.
 */

public class ListViewFornCompra extends ArrayAdapter<FornCompraConst> {

    private List<FornCompraConst> forncompraList;

    private Context mCtx;

    public ListViewFornCompra(List<FornCompraConst> forncompraList, Context mCtx){
        super(mCtx, R.layout.list_view_fornecedores_compras, forncompraList);
        this.forncompraList = forncompraList;
        this.mCtx = mCtx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.list_view_fornecedores_compras, null, true);

        TextView txtid = listViewItem.findViewById(R.id.txtNome);
        TextView txtname = listViewItem.findViewById(R.id.txtTipo);
        TextView txttipo = listViewItem.findViewById(R.id.txtTel);

        FornCompraConst usuariosConst = forncompraList.get(position);

        txtid.setText(usuariosConst.getNome());
        txtname.setText(usuariosConst.getTipo());
        txttipo.setText(usuariosConst.getTel());

        return listViewItem;
    }
}
