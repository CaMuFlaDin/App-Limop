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

import br.com.limopestoques.limop.Construtoras.OrdemServicoConst;
import br.com.limopestoques.limop.R;

/**
 * Created by Aluno on 18/05/2018.
 */

public class ListViewOrdemServico extends ArrayAdapter<OrdemServicoConst>{

    //Listas
    private List<OrdemServicoConst> ordemservicoList;
    private List<OrdemServicoConst> orig;

    //Contexto
    private Context mCtx;

    public ListViewOrdemServico(List<OrdemServicoConst> ordemservicoList, Context mCtx){
        super(mCtx, R.layout.list_view_ordem_servico, ordemservicoList);
        this.ordemservicoList = ordemservicoList;
        this.mCtx = mCtx;
    }


    @Override
    public int getCount() {
        return ordemservicoList.size();
    }

    //Filter para a pesquisa
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<OrdemServicoConst> results = new ArrayList<OrdemServicoConst>();
                if (orig == null) {
                    orig = ordemservicoList;
                }
                if (constraint != null) {
                    constraint = constraint.toString().toLowerCase();
                    if (orig != null && orig.size() > 0) {
                        for (final OrdemServicoConst g : orig) {
                            if ((g.getNumPedido().toLowerCase().contains(constraint.toString())) ||
                                    (g.getEquipamento().toLowerCase().contains(constraint.toString())) ||
                                    g.getCliente().toLowerCase().contains(constraint.toString())) {
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
                ordemservicoList = (ArrayList<OrdemServicoConst>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    //Dados para a listagem
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.list_view_ordem_servico, null, true);

        TextView txtCliente         = listViewItem.findViewById(R.id.txtCliente);
        TextView txtEquipamento     = listViewItem.findViewById(R.id.txtEquipamento);
        TextView txtNumPedido       = listViewItem.findViewById(R.id.txtNumpedido);

        OrdemServicoConst ordemservicoConst = ordemservicoList.get(position);

        txtCliente.setText(ordemservicoConst.getCliente());
        txtEquipamento.setText(ordemservicoConst.getEquipamento());
        txtNumPedido.setText(ordemservicoConst.getNumPedido());

        return listViewItem;
    }

}
