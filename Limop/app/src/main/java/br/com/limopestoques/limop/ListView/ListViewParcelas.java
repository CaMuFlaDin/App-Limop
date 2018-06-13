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

import br.com.limopestoques.limop.Construtoras.ParcelasConst;
import br.com.limopestoques.limop.R;

/**
 * Created by Aluno on 11/06/2018.
 */

public class ListViewParcelas extends ArrayAdapter<ParcelasConst> {

    private List<ParcelasConst> parcelasList;
    private List<ParcelasConst> orig;

    private Context mCtx;

    public ListViewParcelas(List<ParcelasConst> parcelasList, Context mCtx){
        super(mCtx, R.layout.list_view_parcelas, parcelasList);
        this.parcelasList = parcelasList;
        this.mCtx = mCtx;
    }

    @Override
    public int getCount() {
        return parcelasList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<ParcelasConst> results = new ArrayList<ParcelasConst>();
                if (orig == null) {
                    orig = parcelasList;
                }
                if (constraint != null) {
                    constraint = constraint.toString().toLowerCase();
                    if (orig != null && orig.size() > 0) {
                        for (final ParcelasConst g : orig) {
                            if ((g.getVencimento().toLowerCase().contains(constraint.toString())) ||
                                    (g.getCliente().toLowerCase().contains(constraint.toString())) ||
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
                parcelasList = (ArrayList<ParcelasConst>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.list_view_parcelas, null, true);

        TextView txtCliente = listViewItem.findViewById(R.id.txtCliente);
        TextView txtValor = listViewItem.findViewById(R.id.txtValor);
        TextView txtVencimento = listViewItem.findViewById(R.id.txtVencimento);

        ParcelasConst parcelasConst = parcelasList.get(position);

        txtCliente.setText(parcelasConst.getCliente());
        txtValor.setText(parcelasConst.getValor());
        txtVencimento.setText(parcelasConst.getVencimento());

        /*if(parcelasConst.get().equals("Recebido")){
            view.setBackgroundColor(mCtx.getResources().getColor(R.color.verdelhoso));
        }else if(parcelasConst.get().equals("Atrasado")){
            view.setBackgroundColor(mCtx.getResources().getColor(R.color.vermelhao));
        }else {
            view.setBackgroundColor(mCtx.getResources().getColor(R.color.amarelao));
        }*/

        //TODO: View para cores em parcelas *

        return listViewItem;
    }

}
