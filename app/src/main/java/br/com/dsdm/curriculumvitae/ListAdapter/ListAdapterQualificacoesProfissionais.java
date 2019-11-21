package br.com.dsdm.curriculumvitae.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.dsdm.curriculumvitae.Model.QualificacoesProfissionais;
import br.com.dsdm.curriculumvitae.R;

public class ListAdapterQualificacoesProfissionais extends ArrayAdapter<QualificacoesProfissionais> {

    private Context context;
    private ArrayList<QualificacoesProfissionais> qualificacoes;

    public ListAdapterQualificacoesProfissionais(Context context, ArrayList<QualificacoesProfissionais> qualificacoes){
        super(context,0, qualificacoes);
        this.context = context;
        this.qualificacoes = qualificacoes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        QualificacoesProfissionais qualificacoes = this.getItem(position);
        convertView = LayoutInflater.from(this.context).inflate(R.layout.list_qualificacoes_profissionais, null);

        TextView tvAtividade = (TextView) convertView.findViewById(R.id.tvQualificacao);
        tvAtividade.setText(qualificacoes.getQualificacao());

        return convertView;
    }
}
