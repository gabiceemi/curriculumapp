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

import br.com.dsdm.curriculumvitae.ListAdapter.ListAdapterExperienciaProfissional;
import br.com.dsdm.curriculumvitae.Model.ExperienciaProfissional;
import br.com.dsdm.curriculumvitae.Model.Repositorio.RepositoryCurriculum;
import br.com.dsdm.curriculumvitae.R;

public class ctrlExperienciaProfissional extends Fragment {

    private EditText edt;
    private View rootView;
    private String experiencia;
    private String antigaExperiencia;
    private RepositoryCurriculum rp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_experiencias, container, false);

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
                rootView.findViewById(R.id.floatingActionButtonProximoEP);
        floatingActionButtonProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlFormacaoEducacional()).commit();
            }
        });

        FloatingActionButton floatingActionButtonAnterior = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonAnteriorEP);
        floatingActionButtonAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlDadosPessoais()).commit();
            }
        });

        FloatingActionButton floatingActionButtonAdd = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonAddEP);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                edt = new EditText(getActivity());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Experiência Profissional:");
                alertDialog.setView(edt);
                alertDialog.setPositiveButton("Salvar", dialogClickListener);
                alertDialog.show();
            }
        });

        apresentarDados();

        return rootView;

    }

    public void apresentarDados(){
        final ArrayList experiencias = (ArrayList) this.rp.getExperienciaProfissional();

        ListView listView = (ListView) rootView.findViewById(R.id.listViewEP);
        ListAdapterExperienciaProfissional adapterExperienciaProfissional = new ListAdapterExperienciaProfissional(this.getActivity().getBaseContext(), experiencias);
        listView.setAdapter(adapterExperienciaProfissional);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExperienciaProfissional exp = (ExperienciaProfissional) parent.getAdapter().getItem(position);
                edt = new EditText(getActivity());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Experiência Profissional:");
                edt.setText(exp.getExperiencia().toString());
                antigaExperiencia = exp.getExperiencia().toString();
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
        this.experiencia = edt.getText().toString();
        if (i==1) {
            insereBanco(experiencia);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        } else if (i==2){
            atualizaBanco(experiencia, antigaExperiencia);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        } else if (i==3){
            removeBanco(experiencia);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        }
    }

    public void atualizaBanco(String novaExperiencia, String antigaExperiencia){
        ContentValues informacoes = new ContentValues();
        informacoes.put("descExpProfi", novaExperiencia);
        rp.atualizaCurriculo(getActivity(), "ExperienciaProfissional", informacoes, "descExpProfi = \""+ antigaExperiencia +"\"");
    }

    public void insereBanco(String experiencia){
        ContentValues informacoes = new ContentValues();
        informacoes.put("descExpProfi", experiencia);
        informacoes.put("idPessoa", 1);
        rp.adicionaCurriculo(getActivity(), "ExperienciaProfissional", informacoes);
    }

    public void removeBanco(String experiencia){
        rp.removeCurriculo(getActivity(), "ExperienciaProfissional", "ExperienciaProfissional.descExpProfi = + \""+experiencia+"\"");
    }

}
