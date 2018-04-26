package br.com.limopestoques.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import br.com.limopestoques.limop.Construtoras.TranspCompraConst;
import br.com.limopestoques.limop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 04/03/2018.
 */

public class ListViewTranspCompra extends ArrayAdapter<TranspCompraConst> {

    private List<TranspCompraConst> transpcompraList;
    private List<TranspCompraConst> orig;

    private Context mCtx;

    public ListViewTranspCompra(List<TranspCompraConst> transpcompraList, Context mCtx){
        super(mCtx, R.layout.list_view_transportadoras_compras, transpcompraList);
        this.transpcompraList = transpcompraList;
        this.mCtx = mCtx;
    }

    @Override
    public int getCount() {
        return transpcompraList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<TranspCompraConst> results = new ArrayList<TranspCompraConst>();
                if (orig == null) {
                    orig = transpcompraList;
                }
                if (constraint != null) {
                    constraint = constraint.toString().toLowerCase();
                    if (orig != null && orig.size() > 0) {
                        for (final TranspCompraConst g : orig) {
                            if ((g.getNome().toLowerCase().contains(constraint.toString())) ||
                                    (g.getEmail().toLowerCase().contains(constraint.toString())) ||
                                    g.getValor().toLowerCase().contains(constraint.toString())) {
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
                transpcompraList = (ArrayList<TranspCompraConst>) results.values;
                notifyDataSetChanged();
            }
        };
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
