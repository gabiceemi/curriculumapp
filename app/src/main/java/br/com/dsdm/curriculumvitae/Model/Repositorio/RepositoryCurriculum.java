package br.com.dsdm.curriculumvitae.Model.Repositorio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.dsdm.curriculumvitae.DAO.DAOCurriculum;
import br.com.dsdm.curriculumvitae.Model.Pessoa;
import br.com.dsdm.curriculumvitae.Model.ExperienciaProfissional;
import br.com.dsdm.curriculumvitae.Model.FormacaoEducacional;
import br.com.dsdm.curriculumvitae.Model.Idiomas;
import br.com.dsdm.curriculumvitae.Model.InfoAdicionais;
import br.com.dsdm.curriculumvitae.Model.Objetivo;
import br.com.dsdm.curriculumvitae.Model.QualificacoesProfissionais;

public class RepositoryCurriculum {

    private Pessoa pessoa;
    private List<ExperienciaProfissional> experienciaProfissional;
    private List<FormacaoEducacional> formacaoEducacional;
    private List<Idiomas> idiomas;
    private List<InfoAdicionais> infoAdicionais;
    private List<QualificacoesProfissionais> qualificacoesProfissionais;
    private Objetivo objetivo;

    private DAOCurriculum daoCurriculum;

    public static RepositoryCurriculum instanciaRepositorio;

    public static synchronized RepositoryCurriculum getInstance(Context context) {
        if (instanciaRepositorio == null) {
            instanciaRepositorio = new RepositoryCurriculum(context);
        }
        return instanciaRepositorio;
    }

    private RepositoryCurriculum(Context context) { this.daoCurriculum = DAOCurriculum.getInstance(context); }

    public void consultaBase(){
        List<Cursor> resultadoCurriculoCursores = this.daoCurriculum.consultaCurriculo();

        Cursor pessoa = resultadoCurriculoCursores.get(0);
        Cursor expProfi = resultadoCurriculoCursores.get(1);
        Cursor formEduc = resultadoCurriculoCursores.get(2);
        Cursor idiomas = resultadoCurriculoCursores.get(3);
        Cursor infoAdc = resultadoCurriculoCursores.get(4);
        Cursor qualiProfi = resultadoCurriculoCursores.get(5);

        try {
            if (pessoa.moveToFirst()){
                do {
                    this.pessoa = new Pessoa();
                    this.objetivo = new Objetivo();
                    this.pessoa.setNome(pessoa.getString(0));
                    this.pessoa.setNacionalidade(pessoa.getString(1));
                    this.pessoa.setEstadoCivil(pessoa.getString(2));
                    this.pessoa.setNascimento(pessoa.getString(3));
                    this.pessoa.setTelefone(pessoa.getString(4));
                    this.pessoa.setEmail(pessoa.getString(5));
                    this.pessoa.setCidade(pessoa.getString(6));
                    this.pessoa.setEndereco(pessoa.getString(7));

                    this.objetivo.setObjetivo(pessoa.getString(8));
                } while (pessoa.moveToNext());
                    pessoa.close();
            }

            this.experienciaProfissional = new ArrayList<ExperienciaProfissional>();
            if (expProfi.moveToFirst()){
                do {
                    ExperienciaProfissional objExpProfi = new ExperienciaProfissional();
                    objExpProfi.setExperiencia(expProfi.getString(0));
                    this.experienciaProfissional.add(objExpProfi);
                } while (expProfi.moveToNext());
                expProfi.close();
            }

            this.formacaoEducacional = new ArrayList<FormacaoEducacional>();
            if (formEduc.moveToFirst()){
                do {
                    FormacaoEducacional objFormEduc = new FormacaoEducacional();
                    objFormEduc.setFormacao(formEduc.getString(0));
                    this.formacaoEducacional.add(objFormEduc);
                } while (formEduc.moveToNext());
                formEduc.close();
            }

            this.idiomas = new ArrayList<Idiomas>();
            if (idiomas.moveToFirst()){
                do {
                    Idiomas objIdioma = new Idiomas();
                    objIdioma.setIdioma(idiomas.getString(0));
                    this.idiomas.add(objIdioma);
                } while (idiomas.moveToNext());
                idiomas.close();
            }

            this.infoAdicionais = new ArrayList<InfoAdicionais>();
            if (infoAdc.moveToFirst()){
                do {
                    InfoAdicionais objInfoAdc = new InfoAdicionais();
                    objInfoAdc.setInfoAdicional(infoAdc.getString(0));
                    this.infoAdicionais.add(objInfoAdc);
                } while (infoAdc.moveToNext());
                infoAdc.close();
            }

            this.qualificacoesProfissionais = new ArrayList<QualificacoesProfissionais>();
            if (qualiProfi.moveToFirst()){
                do {
                    QualificacoesProfissionais objQualiProfi = new QualificacoesProfissionais();
                    objQualiProfi.setQualificacao(qualiProfi.getString(0));
                    this.qualificacoesProfissionais.add(objQualiProfi);
                } while (qualiProfi.moveToNext());
                qualiProfi.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    public Pessoa getPessoa() {
        return pessoa;
    }
    public List<ExperienciaProfissional> getExperienciaProfissional() { return experienciaProfissional; }
    public List<FormacaoEducacional> getFormacaoEducacional() { return formacaoEducacional; }
    public List<Idiomas> getIdiomas() {
        return idiomas;
    }
    public List<InfoAdicionais> getInfoAdicionais() {
        return infoAdicionais;
    }
    public List<QualificacoesProfissionais> getQualificacoesProfissionais() { return qualificacoesProfissionais; }
    public Objetivo getObjetivo() {
        return objetivo;
    }

    public void limpaRepositorio (){
        this.pessoa = null;
        this.experienciaProfissional = null;
        this.formacaoEducacional = null;
        this.idiomas = null;
        this.infoAdicionais = null;
        this.qualificacoesProfissionais = null;
        this.objetivo = null;
    }

    public void atualizaCurriculo(Context context, String tabela, ContentValues valores, String clausulaWhere){
        this.daoCurriculum = DAOCurriculum.getInstance(context);
        this.daoCurriculum.atualizaCurriculoDAO(tabela , valores, clausulaWhere);
    }

    public void adicionaCurriculo(Context context, String tabela, ContentValues valores){
        this.daoCurriculum = DAOCurriculum.getInstance(context);
        this.daoCurriculum.adicionaItemCurriculoDAO(tabela , valores);
    }

    public void removeCurriculo(Context context, String tabela, String clausulaWhere){
        this.daoCurriculum = DAOCurriculum.getInstance(context);
        this.daoCurriculum.removeItemCurriculoDAO(tabela , clausulaWhere);
    }



}
