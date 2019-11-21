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

import br.com.dsdm.curriculumvitae.ListAdapter.ListAdapterQualificacoesProfissionais;
import br.com.dsdm.curriculumvitae.Model.QualificacoesProfissionais;
import br.com.dsdm.curriculumvitae.Model.Repositorio.RepositoryCurriculum;
import br.com.dsdm.curriculumvitae.R;

public class ctrlQualificacoesProfissionais extends Fragment {

    private EditText edt;
    private View rootView;
    private String qualificacao;
    private String antigaQualificacao;
    private RepositoryCurriculum rp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_qualificacoes, container, false);

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
                rootView.findViewById(R.id.floatingActionButtonProximoQP);
        floatingActionButtonProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlObjetivo()).commit();
            }
        });

        FloatingActionButton floatingActionButtonAnterior = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonAnteriorQP);
        floatingActionButtonAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlInfoAdicionais()).commit();
            }
        });

        FloatingActionButton floatingActionButtonAdd = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonAddQP);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                edt = new EditText(getActivity());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Qualificação Profissional:");
                alertDialog.setView(edt);
                alertDialog.setPositiveButton("Ok", dialogClickListener);
                alertDialog.show();
            }
        });

        apresentarDados();

        return rootView;

    }

    public void apresentarDados(){
        ArrayList qualificacoes = (ArrayList) this.rp.getQualificacoesProfissionais();

        ListView listView = (ListView) rootView.findViewById(R.id.listViewQP);
        ListAdapterQualificacoesProfissionais adapterQualificacoesProfissionais= new ListAdapterQualificacoesProfissionais(this.getActivity().getBaseContext(), qualificacoes);
        listView.setAdapter(adapterQualificacoesProfissionais);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QualificacoesProfissionais quali = (QualificacoesProfissionais) parent.getAdapter().getItem(position);
                edt = new EditText(getActivity());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Idioma:");
                edt.setText(quali.getQualificacao().toString());
                antigaQualificacao = quali.getQualificacao().toString();
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
        this.qualificacao = edt.getText().toString();
        if (i==1) {
            insereBanco(qualificacao);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        } else if (i==2){
            atualizaBanco(qualificacao, antigaQualificacao);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        } else if (i==3){
            removeBanco(qualificacao);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        }
    }

    public void atualizaBanco(String novaQuali, String antigaQuali){
        ContentValues informacoes = new ContentValues();
        informacoes.put("descQualiProfi", novaQuali);
        rp.atualizaCurriculo(getActivity(), "QualificacaoProfissional", informacoes, "descQualiProfi = \""+ antigaQuali +"\"");
    }

    public void insereBanco(String qualificacao){
        ContentValues informacoes = new ContentValues();
        informacoes.put("descQualiProfi", qualificacao);
        informacoes.put("idPessoa", 1);
        rp.adicionaCurriculo(getActivity(), "QualificacaoProfissional", informacoes);
    }

    public void removeBanco(String qualificacao){
        rp.removeCurriculo(getActivity(), "QualificacaoProfissional", "QualificacaoProfissional.descQualiProfi = + \""+qualificacao+"\"");
    }


}
