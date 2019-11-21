package br.com.dsdm.curriculumvitae.Controller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.dsdm.curriculumvitae.DataBase.DBHelper;
import br.com.dsdm.curriculumvitae.Model.ExperienciaProfissional;
import br.com.dsdm.curriculumvitae.Model.FormacaoEducacional;
import br.com.dsdm.curriculumvitae.Model.Idiomas;
import br.com.dsdm.curriculumvitae.Model.InfoAdicionais;
import br.com.dsdm.curriculumvitae.Model.QualificacoesProfissionais;
import br.com.dsdm.curriculumvitae.Model.Repositorio.RepositoryCurriculum;
import br.com.dsdm.curriculumvitae.PDF.GeradorPDF;
import br.com.dsdm.curriculumvitae.R;

public class ctrlMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DBHelper db;
    private RepositoryCurriculum rp;
    private ExpandableListView listView;
    private br.com.dsdm.curriculumvitae.ListAdapter.ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.db = DBHelper.getInstance(this);
        this.db.getConexaoDataBase();
        this.rp = RepositoryCurriculum.getInstance(this);
        this.rp.limpaRepositorio();
        this.rp.consultaBase();

        listView = (ExpandableListView)findViewById(R.id.lvExp);
        apresentarDados();
        listAdapter = new br.com.dsdm.curriculumvitae.ListAdapter.ExpandableListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter);


    }

    public void apresentarDados(){
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("Dados Pessoais");
        listDataHeader.add("Experiência Profissional");
        listDataHeader.add("Formação Educacional");
        listDataHeader.add("Idiomas");
        listDataHeader.add("Informações Adicionais");
        listDataHeader.add("Qualificações Profissionais");
        listDataHeader.add("Objetivo");

        List<String> dadosPessoais = new ArrayList<>();
        dadosPessoais.add(rp.getPessoa().getNome());
        dadosPessoais.add(rp.getPessoa().getNacionalidade());
        dadosPessoais.add(rp.getPessoa().getEstadoCivil());
        dadosPessoais.add(rp.getPessoa().getNascimento());
        dadosPessoais.add(rp.getPessoa().getTelefone());
        dadosPessoais.add(rp.getPessoa().getEmail());
        dadosPessoais.add(rp.getPessoa().getEndereco());
        dadosPessoais.add(rp.getPessoa().getCidade());

        List<String> experiencias = new ArrayList<>();
        List<ExperienciaProfissional> listaEP = rp.getExperienciaProfissional();
        for (ExperienciaProfissional q: listaEP) {
            experiencias.add(q.getExperiencia());
        }

        List<String> formacoes = new ArrayList<>();
        List<FormacaoEducacional> listaFE = rp.getFormacaoEducacional();
        for (FormacaoEducacional q: listaFE) {
            formacoes.add(q.getFormacao());
        }

        List<String> idiomas = new ArrayList<>();
        List<Idiomas> listaI = rp.getIdiomas();
        for (Idiomas q: listaI) {
            idiomas.add(q.getIdioma());
        }

        List<String> infoAdicionais = new ArrayList<>();
        List<InfoAdicionais> listaIA = rp.getInfoAdicionais();
        for (InfoAdicionais q: listaIA) {
            infoAdicionais.add(q.getInfoAdicional());
        }

        List<String> qualificacoes = new ArrayList<>();
        List<QualificacoesProfissionais> listaQP = rp.getQualificacoesProfissionais();
        for (QualificacoesProfissionais q: listaQP) {
            qualificacoes.add(q.getQualificacao());
        }

        List<String> objetivo = new ArrayList<>();
        objetivo.add(rp.getObjetivo().getObjetivo());

        listHash.put(listDataHeader.get(0),dadosPessoais);
        listHash.put(listDataHeader.get(1),experiencias);
        listHash.put(listDataHeader.get(2),formacoes);
        listHash.put(listDataHeader.get(3),idiomas);
        listHash.put(listDataHeader.get(4),infoAdicionais);
        listHash.put(listDataHeader.get(5),qualificacoes);
        listHash.put(listDataHeader.get(6),objetivo);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_add) {
            fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlDadosPessoais()).commit();
        } else if (id == R.id.nav_visualizar) {
            try {
                verificaPermissaoPdf();
                visualizarPdf();
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_compartilhar) {
            try {
                compartilharPDF();
            } catch (FileNotFoundException | DocumentException e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_sobre) {
            fragmentManager.beginTransaction().replace(R.id.conteudo_fragmento, new ctrlSobre()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        verificaPermissaoPdf();
                    } catch (FileNotFoundException | DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "Permissão de escrita negada (WRITE_EXTERNAL)", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected void verificaPermissaoPdf() throws FileNotFoundException,DocumentException{
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }
            }
        } else {
            criarArquivoPdf();
        }
    }

    private void criarArquivoPdf() throws FileNotFoundException, DocumentException {
        try {
            GeradorPDF pdf = new GeradorPDF();
            this.pdfFile = pdf.montaPDF(this);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void compartilharPDF() throws FileNotFoundException, DocumentException {
        criarArquivoPdf();
        Uri arquivoPDF = Uri.fromFile(pdfFile);
        Intent _intent = new Intent();
        _intent.setAction(Intent.ACTION_SEND);
        _intent.putExtra(Intent.EXTRA_STREAM,  arquivoPDF);
        _intent.putExtra(Intent.EXTRA_TEXT,  "Curriculum Vitae");
        _intent.putExtra(Intent.EXTRA_TITLE,   "Meu Currículo");
        _intent.setType("application/pdf");
        startActivity(Intent.createChooser(_intent, "COMPARTILHAR CURRÍCULO"));
    }

    protected void visualizarPdf() {
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri arquivoPDF = Uri.fromFile(pdfFile);
            intent.setDataAndType(arquivoPDF, "application/pdf");
            startActivity(intent);
        }else{
            Toast.makeText(this,"Você precisa baixar um leitor de PDF para poder visualizar o arquivo!",Toast.LENGTH_SHORT).show();
        }
    }
}
