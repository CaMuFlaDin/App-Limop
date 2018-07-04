package br.com.limopestoques.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import br.com.limopestoques.limop.Construtoras.FornCompraConst;
import br.com.limopestoques.limop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 04/03/2018.
 */

public class ListViewFornCompra extends ArrayAdapter<FornCompraConst> {

    //Listas
    private List<FornCompraConst> forncompraList;
    private List<FornCompraConst> orig;

    //Contexto
    private Context mCtx;

    public ListViewFornCompra(List<FornCompraConst> forncompraList, Context mCtx){
        super(mCtx, R.layout.list_view_fornecedores_compras, forncompraList);
        this.forncompraList = forncompraList;
        this.mCtx = mCtx;
    }


    @Override
    public int getCount() {
        return forncompraList.size();
    }

    //Filter para a pesquisa
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<FornCompraConst> results = new ArrayList<FornCompraConst>();
                if (orig == null) {
                    orig = forncompraList;
                }
                if (constraint != null) {
                    constraint = constraint.toString().toLowerCase();
                    if (orig != null && orig.size() > 0) {
                        for (final FornCompraConst g : orig) {
                            if ((g.getNome().toLowerCase().contains(constraint.toString())) ||
                                    (g.getTipo().toLowerCase().contains(constraint.toString())) ||
                                    g.getTel().toLowerCase().contains(constraint.toString())) {
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
                forncompraList = (ArrayList<FornCompraConst>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //Dados para a listagem
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
