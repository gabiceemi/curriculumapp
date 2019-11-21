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

import br.com.dsdm.curriculumvitae.ListAdapter.ListAdapterInfoAdicionais;
import br.com.dsdm.curriculumvitae.Model.InfoAdicionais;
import br.com.dsdm.curriculumvitae.Model.Repositorio.RepositoryCurriculum;
import br.com.dsdm.curriculumvitae.R;

public class ctrlInfoAdicionais extends Fragment {

    private EditText edt;
    private View rootView;
    private String infoAdicional;
    private String antigaInfoAdicional;
    private RepositoryCurriculum rp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_info_adicionais, container, false);

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
                rootView.findViewById(R.id.floatingActionButtonProximoIA);
        floatingActionButtonProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlQualificacoesProfissionais()).commit();
            }
        });

        FloatingActionButton floatingActionButtonAnterior = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonAnteriorIA);
        floatingActionButtonAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlIdiomas()).commit();
            }
        });

        FloatingActionButton floatingActionButtonAdd = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonAddIA);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                edt = new EditText(getActivity());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Informações Adicionais:");
                alertDialog.setView(edt);
                alertDialog.setPositiveButton("Ok", dialogClickListener);
                alertDialog.show();
            }
        });

        apresentarDados();

        return rootView;

    }

    public void apresentarDados(){
        ArrayList infoAdicionais = (ArrayList) this.rp.getInfoAdicionais();

        ListView listView = (ListView) rootView.findViewById(R.id.listViewIA);
        ListAdapterInfoAdicionais adapterInfoAdicionais= new ListAdapterInfoAdicionais(this.getActivity().getBaseContext(), infoAdicionais);
        listView.setAdapter(adapterInfoAdicionais);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InfoAdicionais infoAdc = (InfoAdicionais) parent.getAdapter().getItem(position);
                edt = new EditText(getActivity());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage("Informação adicional:");
                edt.setText(infoAdc.getInfoAdicional().toString());
                antigaInfoAdicional = infoAdc.getInfoAdicional().toString();
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
        this.infoAdicional = edt.getText().toString();
        if (i==1) {
            insereBanco(infoAdicional);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        } else if (i==2){
            atualizaBanco(infoAdicional, antigaInfoAdicional);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        } else if (i==3){
            removeBanco(infoAdicional);
            this.rp.limpaRepositorio();
            this.rp.consultaBase();
            apresentarDados();
        }
    }

    public void atualizaBanco(String novaInfoAdc, String antigaInfoAdc){
        ContentValues informacoes = new ContentValues();
        informacoes.put("descInfoAdicional", novaInfoAdc);
        rp.atualizaCurriculo(getActivity(), "InformacaoAdicional", informacoes, "descInfoAdicional = \""+ antigaInfoAdc +"\"");
    }

    public void insereBanco(String novaInfoAdc){
        ContentValues informacoes = new ContentValues();
        informacoes.put("descInfoAdicional", novaInfoAdc);
        informacoes.put("idPessoa", 1);
        rp.adicionaCurriculo(getActivity(), "InformacaoAdicional", informacoes);
    }

    public void removeBanco(String infoAdic){
        rp.removeCurriculo(getActivity(), "InformacaoAdicional", "InformacaoAdicional.descInfoAdicional = + \""+infoAdic+"\"");
    }
}
