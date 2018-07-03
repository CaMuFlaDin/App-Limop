package br.com.limopestoques.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import br.com.limopestoques.limop.Construtoras.AcessosConst;
import br.com.limopestoques.limop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 04/03/2018.
 */

public class ListViewAcessos extends ArrayAdapter<AcessosConst> {

    //Listas
    private List<AcessosConst> acessosList;
    private List<AcessosConst> orig;

    //Contexto
    private Context mCtx;

    public ListViewAcessos(List<AcessosConst> acessosList, Context mCtx){
            super(mCtx, R.layout.list_view_acessos, acessosList);
            this.acessosList = acessosList;
            this.mCtx = mCtx;
    }


    @Override
    public int getCount() {
        return acessosList.size();
    }

    //Filter para pesquisa
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<AcessosConst> results = new ArrayList<AcessosConst>();
                if (orig == null) {
                    orig = acessosList;
                }
                if (constraint != null) {
                    constraint = constraint.toString().toLowerCase();
                    if (orig != null && orig.size() > 0) {
                        for (final AcessosConst g : orig) {
                            if ((g.getUser().toLowerCase().contains(constraint.toString())) ||
                                    (g.getQtd().toLowerCase().contains(constraint.toString())) ||
                                    g.getData().toLowerCase().contains(constraint.toString())) {
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
                acessosList = (ArrayList<AcessosConst>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //Dados para a listagem
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
