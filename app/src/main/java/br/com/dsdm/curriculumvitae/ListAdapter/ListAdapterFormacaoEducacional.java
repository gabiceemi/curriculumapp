package br.com.dsdm.curriculumvitae.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.dsdm.curriculumvitae.Model.FormacaoEducacional;
import br.com.dsdm.curriculumvitae.R;

public class ListAdapterFormacaoEducacional extends ArrayAdapter<FormacaoEducacional> {
    private Context context;
    private ArrayList<FormacaoEducacional> formacaoEducacional;

    public ListAdapterFormacaoEducacional(Context context, ArrayList<FormacaoEducacional> formacaoEducacional){
        super(context,0,formacaoEducacional);
        this.context = context;
        this.formacaoEducacional = formacaoEducacional;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        FormacaoEducacional formacaoEducacional = this.getItem(position);
        convertView = LayoutInflater.from(this.context).inflate(R.layout.list_formacao, null);

        TextView tvCurso = (TextView) convertView.findViewById(R.id.tvFormacao);
        tvCurso.setText(formacaoEducacional.getFormacao());

        return convertView;
    }
}
