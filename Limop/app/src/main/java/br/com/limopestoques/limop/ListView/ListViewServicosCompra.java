package br.com.limopestoques.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import br.com.limopestoques.limop.Construtoras.ServicosCompraConst;
import br.com.limopestoques.limop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 04/03/2018.
 */

public class ListViewServicosCompra extends ArrayAdapter<ServicosCompraConst> {

    //Listas
    private List<ServicosCompraConst> servicocompraList;
    private List<ServicosCompraConst> orig;

    //Contexto
    private Context mCtx;

    public ListViewServicosCompra(List<ServicosCompraConst> servicocompraList, Context mCtx){
        super(mCtx, R.layout.list_view_servicos_compras, servicocompraList);
        this.servicocompraList = servicocompraList;
        this.mCtx = mCtx;
    }


    @Override
    public int getCount() {
        return servicocompraList.size();
    }

    //Filter para a pesquisa
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ServicosCompraConst> results = new ArrayList<ServicosCompraConst>();
                if (orig == null) {
                    orig = servicocompraList;
                }
                if (constraint != null) {
                    constraint = constraint.toString().toLowerCase();
                    if (orig != null && orig.size() > 0) {
                        for (final ServicosCompraConst g : orig) {
                            if ((g.getNome().toLowerCase().contains(constraint.toString())) ||
                                    (g.getCusto().toLowerCase().contains(constraint.toString())) ||
                                    g.getVenda().toLowerCase().contains(constraint.toString())) {
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
                servicocompraList = (ArrayList<ServicosCompraConst>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //Dados para a listagem
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.list_view_servicos_compras, null, true);

        TextView txtid = listViewItem.findViewById(R.id.txtNome);
        TextView txtname = listViewItem.findViewById(R.id.txtCusto);
        TextView txttipo = listViewItem.findViewById(R.id.txtVenda);

        ServicosCompraConst usuariosConst = servicocompraList.get(position);

        txtid.setText(usuariosConst.getNome());
        txtname.setText(usuariosConst.getCusto());
        txttipo.setText(usuariosConst.getVenda());

        return listViewItem;
    }
}
