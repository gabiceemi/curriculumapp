package br.com.dsdm.curriculumvitae.Controller;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import br.com.dsdm.curriculumvitae.Model.Repositorio.RepositoryCurriculum;
import br.com.dsdm.curriculumvitae.R;

public class ctrlObjetivo extends Fragment {

    private EditText objetivo;
    private String textObjetivo;
    private View rootView;
    private RepositoryCurriculum rp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_objetivo, container, false);

        FloatingActionButton floatingActionButtonAnterior = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonAnteriorO);
        floatingActionButtonAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDados();
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlQualificacoesProfissionais()).commit();
            }
        });

        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent retornaTelaPrincipal = new Intent(getActivity(), ctrlMainActivity.class);
                        startActivity(retornaTelaPrincipal);
                        break;
                }
            }
        };

        FloatingActionButton floatingActionButtonCheck = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonCheckO);
        floatingActionButtonCheck.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                salvarDados();
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                builder.setMessage("Seu currículo foi salvo, agora você pode gerar um pdf dele e compartilhar!").show();
                builder.setPositiveButton("Ok", dialogClickListener);
                builder.show();
            }
        });

        this.rp = RepositoryCurriculum.getInstance(getActivity());
        this.rp.limpaRepositorio();
        this.rp.consultaBase();

        capturarDados();

        objetivo.setText(this.rp.getObjetivo().getObjetivo());

        return rootView;

    }

    public void capturarDados(){
        objetivo = (EditText) rootView.findViewById(R.id.tvObjetivo);
    }

    public void salvarDados(){
        capturarDados();
        textObjetivo = objetivo.getText().toString();
        atualizaRegistro(textObjetivo);
    }

    public void atualizaRegistro(String obj){
        ContentValues informacoes = new ContentValues();
        informacoes.put("objetivo", obj);
        rp.atualizaCurriculo(getActivity(), "Pessoa", informacoes, "_ID=1");
    }
}
