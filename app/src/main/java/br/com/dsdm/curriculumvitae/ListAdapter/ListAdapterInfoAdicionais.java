package br.com.dsdm.curriculumvitae.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.dsdm.curriculumvitae.Model.InfoAdicionais;
import br.com.dsdm.curriculumvitae.R;

public class ListAdapterInfoAdicionais extends ArrayAdapter<InfoAdicionais> {

    private Context context;
    private ArrayList<InfoAdicionais> infoAdicionais;

    public ListAdapterInfoAdicionais(Context context, ArrayList<InfoAdicionais> infoAdicionais){
        super(context,0,infoAdicionais);
        this.context = context;
        this.infoAdicionais = infoAdicionais;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        InfoAdicionais infoAdicionais = this.getItem(position);
        convertView = LayoutInflater.from(this.context).inflate(R.layout.list_info_adicionais, null);

        TextView tvCurso = (TextView) convertView.findViewById(R.id.tvInfoAdicional);
        tvCurso.setText(infoAdicionais.getInfoAdicional());

        return convertView;
    }
}
