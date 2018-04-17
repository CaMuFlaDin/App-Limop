package com.estoques.limop.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.estoques.limop.limop.Construtoras.AcessosConst;
import com.estoques.limop.limop.Construtoras.ClientesConst;
import com.estoques.limop.limop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aluno on 05/03/2018.
 */

public class ListViewClientes extends ArrayAdapter<ClientesConst>{

    private List<ClientesConst> clientesList;
    private List<ClientesConst> orig;

    private Context mCtx;

    public ListViewClientes(List<ClientesConst> clientesList, Context mCtx){
        super(mCtx, R.layout.list_view_clientes, clientesList);
        this.clientesList = clientesList;
        this.mCtx = mCtx;
    }

    @Override
    public int getCount() {
        return clientesList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ClientesConst> results = new ArrayList<ClientesConst>();
                if (orig == null) {
                    orig = clientesList;
                }
                if (constraint != null) {
                    constraint = constraint.toString().toLowerCase();
                    if (orig != null && orig.size() > 0) {
                        for (final ClientesConst g : orig) {
                            if ((g.getNome().toLowerCase().contains(constraint.toString())) ||
                                    (g.getTipo().toLowerCase().contains(constraint.toString())) ||
                                    g.getEmail().toLowerCase().contains(constraint.toString())) {
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
                clientesList = (ArrayList<ClientesConst>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.list_view_clientes, null, true);

        TextView txtid = listViewItem.findViewById(R.id.txtNome);
        TextView txtname = listViewItem.findViewById(R.id.txtTipo);
        TextView txttipo = listViewItem.findViewById(R.id.txtEmail);

        ClientesConst usuariosConst = clientesList.get(position);

        txtid.setText(usuariosConst.getNome());
        txtname.setText(usuariosConst.getTipo());
        txttipo.setText(usuariosConst.getEmail());

        return listViewItem;
    }
}
