package br.com.dsdm.curriculumvitae.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.dsdm.curriculumvitae.Model.ExperienciaProfissional;
import br.com.dsdm.curriculumvitae.R;

public class ListAdapterExperienciaProfissional extends ArrayAdapter<ExperienciaProfissional> {
    private Context context;
    private ArrayList<ExperienciaProfissional> experienciaProfissional;

    public ListAdapterExperienciaProfissional(Context context, ArrayList<ExperienciaProfissional> experienciaProfissional){
        super(context,0,experienciaProfissional);
        this.context = context;
        this.experienciaProfissional = experienciaProfissional;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ExperienciaProfissional experienciaProfissional = this.getItem(position);
        convertView = LayoutInflater.from(this.context).inflate(R.layout.list_experiencia_profissional, null);

        TextView tvEmpresa = (TextView) convertView.findViewById(R.id.tvExperiencia);
        tvEmpresa.setText(experienciaProfissional.getExperiencia());

        return convertView;
    }
}