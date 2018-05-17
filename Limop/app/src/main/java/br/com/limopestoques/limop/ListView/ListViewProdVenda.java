package br.com.limopestoques.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import br.com.limopestoques.limop.Construtoras.ProdCompraConst;
import br.com.limopestoques.limop.Construtoras.ProdVendaConst;
import br.com.limopestoques.limop.R;

/**
 * Created by Aluno on 17/05/2018.
 */

public class ListViewProdVenda extends ArrayAdapter<ProdVendaConst> {

    private List<ProdVendaConst> prodcompraList;
    private List<ProdVendaConst> orig;

    private Context mCtx;

    public ListViewProdVenda(List<ProdVendaConst> prodcompraList, Context mCtx){
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
                final ArrayList<ProdVendaConst> results = new ArrayList<ProdVendaConst>();
                if (orig == null) {
                    orig = prodcompraList;
                }
                if (constraint != null) {
                    constraint = constraint.toString().toLowerCase();
                    if (orig != null && orig.size() > 0) {
                        for (final ProdVendaConst g : orig) {
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
                prodcompraList = (ArrayList<ProdVendaConst>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View listViewItem = inflater.inflate(R.layout.list_view_produtos_vendas, null, true);

        TextView txtprod = listViewItem.findViewById(R.id.txtProd);
        TextView txtvalor = listViewItem.findViewById(R.id.txtValor);
        TextView txtqtd = listViewItem.findViewById(R.id.txtQtd);
        TextView txtcliente = listViewItem.findViewById(R.id.txtCliente);
        NetworkImageView image = listViewItem.findViewById(R.id.img);

        ImageLoader il = VolleySingleton.getInstance(mCtx).getImageLoader();

        ProdVendaConst usuariosConst = prodcompraList.get(position);

        txtprod.setText(usuariosConst.getProd());
        txtvalor.setText(usuariosConst.getValor());
        txtqtd.setText(usuariosConst.getQtd());
        txtcliente.setText(usuariosConst.getNome_cliente());
        image.setImageUrl("https://limopestoques.com.br/Index_adm/produtos/imgs/"+usuariosConst.getImg(),il);

        return listViewItem;
    }
}
