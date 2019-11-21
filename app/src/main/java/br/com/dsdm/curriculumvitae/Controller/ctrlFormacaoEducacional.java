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

import br.com.dsdm.curriculumvitae.ListAdapter.ListAdapterFormacaoEducacional;
import br.com.dsdm.curriculumvitae.Model.FormacaoEducacional;
import br.com.dsdm.curriculumvitae.Model.Repositorio.RepositoryCurriculum;
import br.com.dsdm.curriculumvitae.R;

public class ctrlFormacaoEducacional extends Fragment {

    private EditText edt;
    private View rootView;
    private String formacao;
    private String antigaFormacao;
    private RepositoryCurriculum rp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_formacao, container, false);

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
                rootView.findViewById(R.id.floatingActionButtonProximoFE);
        floatingActionButtonProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlIdiomas()).commit();
            }
        });

        FloatingActionButton floatingActionButtonAnterior = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonAnteriorFE);
        floatingActionButtonAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlExperienciaProfissional()).commit();
            }
        });

        FloatingActionButton floatingActionButtonAdd = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonAddFE);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                edt = new EditText(getActivity());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Formação Educacional:");
                alertDialog.setView(edt);
                alertDialog.setPositiveButton("Ok", dialogClickListener);
                alertDialog.show();
            }
        });

        apresentarDados();

        return rootView;

    }

    public void apresentarDados(){
        ArrayList formacoes = (ArrayList) this.rp.getFormacaoEducacional();

        ListView listView = (ListView) rootView.findViewById(R.id.listViewFE);
        ListAdapterFormacaoEducacional adapterFormacaoEducacional= new ListAdapterFormacaoEducacional(this.getActivity().getBaseContext(), formacoes);
        listView.setAdapter(adapterFormacaoEducacional);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FormacaoEducacional form = (FormacaoEducacional) parent.getAdapter().getItem(position);
                edt = new EditText(getActivity());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Formação Educacional:");
                edt.setText(form.getFormacao().toString());
                antigaFormacao = form.getFormacao().toString();
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
        this.formacao = edt.getText().toString();
        if (i==1) {
            insereBanco(formacao);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        } else if (i==2){
            atualizaBanco(formacao, antigaFormacao);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        } else if (i==3){
            removeBanco(formacao);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        }
    }

    public void atualizaBanco(String novaFormacao, String antigaFormacao){
        ContentValues informacoes = new ContentValues();
        informacoes.put("descFormEdu", novaFormacao);
        rp.atualizaCurriculo(getActivity(), "FormacaoEducacional", informacoes, "descFormEdu = \""+ antigaFormacao +"\"");
    }

    public void insereBanco(String formacao){
        ContentValues informacoes = new ContentValues();
        informacoes.put("descFormEdu", formacao);
        informacoes.put("idPessoa", 1);
        rp.adicionaCurriculo(getActivity(), "FormacaoEducacional", informacoes);
    }

    public void removeBanco(String formacao){
        rp.removeCurriculo(getActivity(), "FormacaoEducacional", "FormacaoEducacional.descFormEdu = + \""+formacao+"\"");
    }


}
