package br.com.dsdm.curriculumvitae.Controller;


import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import br.com.dsdm.curriculumvitae.Model.Repositorio.RepositoryCurriculum;
import br.com.dsdm.curriculumvitae.R;

import static android.app.Activity.RESULT_OK;

public class ctrlDadosPessoais extends Fragment {

    private EditText nome, nacionalidade, estadoCivil, nascimento, telefone, email, endereco, cidade;
    private String textNome, textNacionalidade, textEstadoCivil, textNascimento, textTelefone, textEmail, textEndereco, textCidade;
    private View rootView;
    private ImageView imagem;
    public static Bitmap thumbnail;
    private RepositoryCurriculum rp;
    private final int GALERIA_IMAGENS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dados_pessoais, container, false);

        imagem = (ImageView) rootView.findViewById(R.id.ivImagem);
        FloatingActionButton galeria = (FloatingActionButton) rootView.findViewById(R.id.btGaleria);
        galeria.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                           startActivityForResult(intent, GALERIA_IMAGENS);
                                       }
        });

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                rootView.findViewById(R.id.floatingActionButtonProximoDP);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarDados();
                android.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlExperienciaProfissional()).commit();
            }
        });

        this.rp = RepositoryCurriculum.getInstance(getActivity());
        this.rp.limpaRepositorio();
        this.rp.consultaBase();

        capturarDados();

        nome.setText(this.rp.getPessoa().getNome().toString());
        nacionalidade.setText(this.rp.getPessoa().getNacionalidade().toString());
        estadoCivil.setText(this.rp.getPessoa().getEstadoCivil().toString());
        nascimento.setText(this.rp.getPessoa().getNascimento().toString());
        telefone.setText(this.rp.getPessoa().getTelefone().toString());
        email.setText(this.rp.getPessoa().getEmail().toString());
        endereco.setText(this.rp.getPessoa().getEndereco().toString());
        cidade.setText(this.rp.getPessoa().getCidade().toString());

        return rootView;

    }

    public void capturarDados() {
        nome = (EditText) rootView.findViewById(R.id.tvNome);
        nacionalidade = (EditText) rootView.findViewById(R.id.tvNacionalidade);
        estadoCivil = (EditText) rootView.findViewById(R.id.tvEstadoCivil);
        nascimento = (EditText) rootView.findViewById(R.id.tvNascimento);
        telefone = (EditText) rootView.findViewById(R.id.tvTelefone);
        email = (EditText) rootView.findViewById(R.id.tvEmail);
        endereco = (EditText) rootView.findViewById(R.id.tvEndereco);
        cidade = (EditText) rootView.findViewById(R.id.tvCidade);
    }

    public void salvarDados() {
        capturarDados();
        textNome = nome.getText().toString();
        textNacionalidade = nacionalidade.getText().toString();
        textEstadoCivil = estadoCivil.getText().toString();
        textNascimento = nascimento.getText().toString();
        textTelefone = telefone.getText().toString();
        textEmail = email.getText().toString();
        textEndereco = endereco.getText().toString();
        textCidade = cidade.getText().toString();
        atualizaRegistro(textNome, textNacionalidade, textEstadoCivil, textNascimento, textTelefone, textEmail, textCidade);
    }

    public void atualizaRegistro(String nome, String nacionalidade, String estadoCivil, String dtNascimento, String telefone, String email, String cidade){
        ContentValues informacoes = new ContentValues();
        informacoes.put("nome",nome);
        informacoes.put("nacionalidade", nacionalidade);
        informacoes.put("estCivil", estadoCivil);
        informacoes.put("dtNasc", dtNascimento);
        informacoes.put("telefone", telefone);
        informacoes.put("email", email);
        informacoes.put("cidade", cidade);
        rp.atualizaCurriculo(getActivity(), "Pessoa", informacoes, "_ID=1");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == GALERIA_IMAGENS){
            Uri selectedImagem = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImagem,filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            thumbnail = (BitmapFactory.decodeFile(picturePath));
            imagem.setImageBitmap(thumbnail);
        }
    }
}
