package br.com.limopestoques.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.limopestoques.limop.Construtoras.ServicosCompraConst;
import br.com.limopestoques.limop.Construtoras.ServicosVendasConst;
import br.com.limopestoques.limop.R;

/**
 * Created by Aluno on 14/05/2018.
 */

public class ListViewServicosVendas extends ArrayAdapter<ServicosVendasConst> {

    //Listas
    private List<ServicosVendasConst> servicocompraList;
    private List<ServicosVendasConst> orig;

    //Contexto
    private Context mCtx;

    public ListViewServicosVendas(List<ServicosVendasConst> servicocompraList, Context mCtx){
        super(mCtx, R.layout.list_view_servicos_vendas, servicocompraList);
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
                final ArrayList<ServicosVendasConst> results = new ArrayList<ServicosVendasConst>();
                if (orig == null) {
                    orig = servicocompraList;
                }
                if (constraint != null) {
                    constraint = constraint.toString().toLowerCase();
                    if (orig != null && orig.size() > 0) {
                        for (final ServicosVendasConst g : orig) {
                            if ((g.getProd().toLowerCase().contains(constraint.toString())) ||
                                    (g.getValor().toLowerCase().contains(constraint.toString())) ||
                                    g.getQtd().toLowerCase().contains(constraint.toString())) {
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
                servicocompraList = (ArrayList<ServicosVendasConst>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //Dados para a listagem
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.list_view_servicos_vendas, null, true);

        TextView txtid = listViewItem.findViewById(R.id.txtNome);
        TextView txtname = listViewItem.findViewById(R.id.txtCusto);
        TextView txttipo = listViewItem.findViewById(R.id.txtVenda);
        TextView txtCliente = listViewItem.findViewById(R.id.txtCliente);

        ServicosVendasConst usuariosConst = servicocompraList.get(position);

        txtid.setText(usuariosConst.getProd());
        txtname.setText(usuariosConst.getValor());
        txttipo.setText(usuariosConst.getQtd());
        txtCliente.setText(usuariosConst.getNome_cliente());

        return listViewItem;
    }

}
