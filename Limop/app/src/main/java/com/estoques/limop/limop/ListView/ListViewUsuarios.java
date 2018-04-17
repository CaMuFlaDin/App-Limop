package com.estoques.limop.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.estoques.limop.limop.Construtoras.UsuariosConst;
import com.estoques.limop.limop.R;

import java.util.List;

/**
 * Created by Aluno on 02/03/2018.
 */

public class ListViewUsuarios extends ArrayAdapter<UsuariosConst>{

    private List<UsuariosConst> usuariosList;

    private Context mCtx;

    public ListViewUsuarios(List<UsuariosConst> usuariosList, Context mCtx){
        super(mCtx, R.layout.list_view_user, usuariosList);
        this.usuariosList = usuariosList;
        this.mCtx = mCtx;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.list_view_user, null, true);

        TextView txtid = listViewItem.findViewById(R.id.txtID);
        TextView txtname = listViewItem.findViewById(R.id.txtName);
        TextView txttipo = listViewItem.findViewById(R.id.txtTipo);

        UsuariosConst usuariosConst = usuariosList.get(position);

        txtid.setText(usuariosConst.getName());
        txtname.setText(usuariosConst.getEmail());
        txttipo.setText(usuariosConst.getTipo());

        return listViewItem;
    }
}