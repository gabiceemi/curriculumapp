package br.com.dsdm.curriculumvitae.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.dsdm.curriculumvitae.DataBase.DBHelper;

public class DAOCurriculum {

    private DBHelper db;
    public static DAOCurriculum instanciaDAO;

    public static synchronized DAOCurriculum getInstance(Context context) {
        if (instanciaDAO == null) {
            instanciaDAO = new DAOCurriculum(context);
        }
        return instanciaDAO;
    }

    private DAOCurriculum(Context context) {
        this.db = DBHelper.getInstance(context);
    }

    public void executaQuery (String SQL){

    }

    public List<Cursor> consultaCurriculo(){

        List<Cursor> listResultados = new ArrayList<>();
        Cursor curriculo, listExpProfi, listFormProf, listIdioma, listInfoAdc, listQualiProfi;

        String buscaPessoa = "SELECT Pessoa.nome, Pessoa.nacionalidade, Pessoa.estCivil, Pessoa.dtNasc, Pessoa.telefone, Pessoa.email, Pessoa.cidade, Pessoa.endereco, Pessoa.objetivo FROM pessoa";

        String buscaExpProfissional = "SELECT experienciaProfissional.descExpProfi FROM ExperienciaProfissional\n" +
                "INNER JOIN Pessoa\n" +
                "ON experienciaProfissional.idPessoa = Pessoa._ID";

        String buscaFormEduca = "SELECT formacaoEducacional.descFormEdu FROM formacaoEducacional\n" +
                "INNER JOIN Pessoa\n" +
                "ON formacaoEducacional.idPessoa = Pessoa._ID";

        String buscaIdioma = "SELECT idioma.nomeIdioma FROM idioma\n" +
                "INNER JOIN Pessoa\n" +
                "ON idioma.idPessoa = Pessoa._ID";

        String buscaInfoAdic = "SELECT informacaoAdicional.descInfoAdicional FROM informacaoAdicional\n" +
                "INNER JOIN Pessoa\n" +
                "ON informacaoAdicional.idPessoa = Pessoa._ID";

        String buscaQualiProfi = "SELECT qualificacaoProfissional.descQualiProfi FROM qualificacaoProfissional\n" +
                "INNER JOIN Pessoa\n" +
                "ON qualificacaoProfissional.idPessoa = Pessoa._ID";

        try {
            curriculo = this.db.getConexaoDataBase().rawQuery(buscaPessoa, null);
            listResultados.add(curriculo);

            listExpProfi = this.db.getConexaoDataBase().rawQuery(buscaExpProfissional, null);
            listResultados.add(listExpProfi);

            listFormProf = this.db.getConexaoDataBase().rawQuery(buscaFormEduca, null);
            listResultados.add(listFormProf);

            listIdioma = this.db.getConexaoDataBase().rawQuery(buscaIdioma, null);
            listResultados.add(listIdioma);

            listInfoAdc = this.db.getConexaoDataBase().rawQuery(buscaInfoAdic, null);
            listResultados.add(listInfoAdc);

            listQualiProfi = this.db.getConexaoDataBase().rawQuery(buscaQualiProfi, null);
            listResultados.add(listQualiProfi);

            return listResultados;

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void atualizaCurriculoDAO(String tabela,ContentValues valores, String clausulaWhere) {
        try {
            this.db.getConexaoDataBase().update(tabela, valores, clausulaWhere, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adicionaItemCurriculoDAO(String tabela, ContentValues valores){
        try {
            this.db.getConexaoDataBase().insert(tabela, null, valores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeItemCurriculoDAO(String tabela, String clausulaWhere){
        try {
            this.db.getConexaoDataBase().delete(tabela, clausulaWhere, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
