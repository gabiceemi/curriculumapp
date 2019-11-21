package br.com.dsdm.curriculumvitae.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.dsdm.curriculumvitae.Model.Idiomas;
import br.com.dsdm.curriculumvitae.R;

public class ListAdapterIdiomas  extends ArrayAdapter<Idiomas> {

    private Context context;
    private ArrayList<Idiomas> idiomas;

    public ListAdapterIdiomas(Context context, ArrayList<Idiomas> idiomas){
        super(context,0, idiomas);
        this.context = context;
        this.idiomas = idiomas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Idiomas idiomas = this.getItem(position);
        convertView = LayoutInflater.from(this.context).inflate(R.layout.list_idiomas, null);

        TextView tvLingua = (TextView) convertView.findViewById(R.id.tvIdioma);
        tvLingua.setText(idiomas.getIdioma());

        return convertView;
    }
}
