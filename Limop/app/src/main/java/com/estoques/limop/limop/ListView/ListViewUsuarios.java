package com.estoques.limop.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.estoques.limop.limop.Construtoras.TranspCompraConst;
import com.estoques.limop.limop.Construtoras.UsuariosConst;
import com.estoques.limop.limop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aluno on 02/03/2018.
 */

public class ListViewUsuarios extends ArrayAdapter<UsuariosConst>{

    private List<UsuariosConst> usuariosList;
    private List<UsuariosConst> orig;

    private Context mCtx;

    public ListViewUsuarios(List<UsuariosConst> usuariosList, Context mCtx){
        super(mCtx, R.layout.list_view_user, usuariosList);
        this.usuariosList = usuariosList;
        this.mCtx = mCtx;
    }


    @Override
    public int getCount() {
        return usuariosList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<UsuariosConst> results = new ArrayList<UsuariosConst>();
                if (orig == null) {
                    orig = usuariosList;
                }
                if (constraint != null) {
                    constraint = constraint.toString().toLowerCase();
                    if (orig != null && orig.size() > 0) {
                        for (final UsuariosConst g : orig) {
                            if ((g.getName().toLowerCase().contains(constraint.toString())) ||
                                    (g.getEmail().toLowerCase().contains(constraint.toString())) ||
                                    g.getTipo().toLowerCase().contains(constraint.toString())) {
                                results.add(g);
                            }
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                usuariosList = (ArrayList<UsuariosConst>) results.values;
                notifyDataSetChanged();
            }
        };
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