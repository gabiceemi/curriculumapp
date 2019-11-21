package br.com.dsdm.curriculumvitae.Controller;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import br.com.dsdm.curriculumvitae.ListAdapter.ListAdapterIdiomas;
import br.com.dsdm.curriculumvitae.Model.Idiomas;
import br.com.dsdm.curriculumvitae.Model.Repositorio.RepositoryCurriculum;
import br.com.dsdm.curriculumvitae.R;

public class ctrlIdiomas extends Fragment {

    private EditText edt;
    private View rootView;
    private String idioma;
    private String antigaIdioma;
    private RepositoryCurriculum rp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_idiomas, container, false);

        this.rp = RepositoryCurriculum.getInstance(getActivity());
        this.rp.limpaRepositorio();
        this.rp.consultaBase();

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        salvarDados(1);
                        break;
                }
            }
        };

        FloatingActionButton floatingActionButtonProximo = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonProximoI);
        floatingActionButtonProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlInfoAdicionais()).commit();
            }
        });

        FloatingActionButton floatingActionButtonAnterior = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonAnteriorI);
        floatingActionButtonAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlFormacaoEducacional()).commit();
            }
        });

        FloatingActionButton floatingActionButtonAdd = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonAddI);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                edt = new EditText(getActivity());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Idioma:");
                alertDialog.setView(edt);
                alertDialog.setPositiveButton("Ok", dialogClickListener);
                alertDialog.show();
            }
        });

        apresentarDados();

        return rootView;

    }

    public void apresentarDados(){
        ArrayList idiomas = (ArrayList) this.rp.getIdiomas();

        ListView listView = (ListView) rootView.findViewById(R.id.listViewI);
        ListAdapterIdiomas adapterIdiomas = new ListAdapterIdiomas(this.getActivity().getBaseContext(), idiomas);
        listView.setAdapter(adapterIdiomas);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Idiomas idioma = (Idiomas) parent.getAdapter().getItem(position);
                edt = new EditText(getActivity());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Idioma:");
                edt.setText(idioma.getIdioma().toString());
                antigaIdioma = idioma.getIdioma().toString();
                alertDialog.setView(edt);
                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                salvarDados(2);
                                break;
                            case  DialogInterface.BUTTON_NEGATIVE:
                                salvarDados(3);
                                break;
                        }
                    }
                };
                alertDialog.setPositiveButton("Atualizar", dialogClickListener);
                alertDialog.setNegativeButton("Deletar", dialogClickListener);

                alertDialog.show();
            }
        });

    }

    public void salvarDados(int i) {
        this.idioma = edt.getText().toString();
        if (i==1) {
            insereBanco(idioma);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        } else if (i==2){
            atualizaBanco(idioma, antigaIdioma);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        } else if (i==3){
            removeBanco(idioma);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        }
    }

    public void atualizaBanco(String novoIdioma, String antigoIdioma){
        ContentValues informacoes = new ContentValues();
        informacoes.put("nomeIdioma", novoIdioma);
        rp.atualizaCurriculo(getActivity(), "Idioma", informacoes, "nomeIdioma = \""+ antigoIdioma +"\"");
    }

    public void insereBanco(String lingua){
        ContentValues informacoes = new ContentValues();
        informacoes.put("nomeIdioma", lingua);
        informacoes.put("idPessoa", 1);
        rp.adicionaCurriculo(getActivity(), "Idioma", informacoes);
    }

    public void removeBanco(String lingua){
        rp.removeCurriculo(getActivity(), "Idioma", "Idioma.nomeIdioma = + \""+lingua+"\"");
    }
}
