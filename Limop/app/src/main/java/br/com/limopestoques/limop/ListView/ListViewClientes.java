package br.com.limopestoques.limop.ListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import br.com.limopestoques.limop.Construtoras.ClientesConst;
import br.com.limopestoques.limop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aluno on 05/03/2018.
 */

public class ListViewClientes extends ArrayAdapter<ClientesConst>{

    //Lista
    private List<ClientesConst> clientesList;

    //Contexto
    private Context mCtx;

    public ListViewClientes(List<ClientesConst> clientesList, Context mCtx){
        super(mCtx, R.layout.list_view_clientes, clientesList);
        this.clientesList = clientesList;
        this.mCtx = mCtx;
    }

    //Dados para a listagem
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
