package com.estoques.limop.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.estoques.limop.limop.Construtoras.ProdCompraConst;
import com.estoques.limop.limop.Construtoras.TranspCompraConst;
import com.estoques.limop.limop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 04/03/2018.
 */

public class ListViewProdCompra extends ArrayAdapter<ProdCompraConst>{

    private List<ProdCompraConst> prodcompraList;
    private List<ProdCompraConst> orig;

    private Context mCtx;

    public ListViewProdCompra(List<ProdCompraConst> prodcompraList, Context mCtx){
        super(mCtx, R.layout.list_view_produtos_compras, prodcompraList);
        this.prodcompraList = prodcompraList;
        this.mCtx = mCtx;
    }


    @Override
    public int getCount() {
        return prodcompraList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ProdCompraConst> results = new ArrayList<ProdCompraConst>();
                if (orig == null) {
                    orig = prodcompraList;
                }
                if (constraint != null) {
                    constraint = constraint.toString().toLowerCase();
                    if (orig != null && orig.size() > 0) {
                        for (final ProdCompraConst g : orig) {
                            if ((g.getProd().toLowerCase().contains(constraint.toString())) ||
                                    (g.getQtd().toLowerCase().contains(constraint.toString())) ||
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
                prodcompraList = (ArrayList<ProdCompraConst>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.list_view_produtos_compras, null, true);

        TextView txtprod = listViewItem.findViewById(R.id.txtProd);
        TextView txtvalor = listViewItem.findViewById(R.id.txtValor);
        TextView txtqtd = listViewItem.findViewById(R.id.txtQtd);

        ProdCompraConst usuariosConst = prodcompraList.get(position);

        txtprod.setText(usuariosConst.getProd());
        txtvalor.setText(usuariosConst.getValor());
        txtqtd.setText(usuariosConst.getQtd());

        return listViewItem;
    }
}
