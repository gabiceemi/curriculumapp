package br.com.dsdm.curriculumvitae.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private static final String NOME_BASE = "AplicativoCurriculum.db";
    private static final int VERSAO_BASE = 1;

    public static DBHelper instanciaBD;

    public static synchronized DBHelper getInstance(Context context) {
        if (instanciaBD == null) {
            instanciaBD = new DBHelper(context.getApplicationContext());
        }
        return instanciaBD;
    }

    private DBHelper(Context context) { super(context, NOME_BASE, null, VERSAO_BASE); }


    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuilder CreateTablePESSOA = new StringBuilder();
        CreateTablePESSOA.append("CREATE TABLE `Pessoa` (\n" +
                "\t`_ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`nome`\tTEXT,\n" +
                "\t`nacionalidade`\tTEXT,\n" +
                "\t`estCivil`\tTEXT,\n" +
                "\t`dtNasc`\tTEXT,\n" +
                "\t`telefone`\tTEXT,\n" +
                "\t`email`\tTEXT,\n" +
                "\t`cidade`\tTEXT,\n" +
                "\t`endereco`\tTEXT,\n" +
                "\t`objetivo`\tTEXT\n" +
                ");");

        StringBuilder CreateTableIDIOMA = new StringBuilder();
        CreateTableIDIOMA.append("CREATE TABLE `Idioma` (\n" +
                "\t`_ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`nomeIdioma`\tINTEGER,\n" +
                "\t`idPessoa`\tINTEGER,\n" +
                "\tFOREIGN KEY(`idPessoa`) REFERENCES Pessoa ( _ID )\n" +
                ");");

        StringBuilder CreateTableFORMACAO_EDUCACIONAL = new StringBuilder();
        CreateTableFORMACAO_EDUCACIONAL.append("CREATE TABLE `FormacaoEducacional` (\n" +
                "\t`_ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`descFormEdu`\tTEXT,\n" +
                "\t`idPessoa`\tINTEGER,\n" +
                "\tFOREIGN KEY(`idPessoa`) REFERENCES Pessoa ( _ID )\n" +
                ");");

        StringBuilder CreateTableEXPERIENCIA_PROFISSIONAL = new StringBuilder();
        CreateTableEXPERIENCIA_PROFISSIONAL.append("CREATE TABLE `ExperienciaProfissional` (\n" +
                "\t`_ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`descExpProfi`\tTEXT,\n" +
                "\t`idPessoa`\tINTEGER,\n" +
                "\tFOREIGN KEY(`idPessoa`) REFERENCES Pessoa ( _ID )\n" +
                ");");

        StringBuilder CreateTableQUALIFICACAO_PROFISSIONAL = new StringBuilder();
        CreateTableQUALIFICACAO_PROFISSIONAL.append("CREATE TABLE `QualificacaoProfissional` (\n" +
                "\t`_ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`descQualiProfi`\tTEXT,\n" +
                "\t`idPessoa`\tINTEGER,\n" +
                "\tFOREIGN KEY(`idPessoa`) REFERENCES Pessoa ( _ID )\n" +
                ");");

        StringBuilder CreateTableINFORMACAO_ADICIONAL = new StringBuilder();
        CreateTableINFORMACAO_ADICIONAL.append("CREATE TABLE `InformacaoAdicional` (\n" +
                "\t`_ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "\t`descInfoAdicional`\tTEXT,\n" +
                "\t`idPessoa`\tINTEGER,\n" +
                "\tFOREIGN KEY(`idPessoa`) REFERENCES Pessoa ( _ID )\n" +
                ");");

        db.execSQL(CreateTablePESSOA.toString());
        Log.e("TABLE PESSOA", CreateTablePESSOA.toString());
        db.execSQL(CreateTableIDIOMA.toString());
        Log.e("TABLE IDIOMA", CreateTableIDIOMA.toString());
        db.execSQL(CreateTableFORMACAO_EDUCACIONAL.toString());
        Log.e("TABLE FORMACAO EDUC", CreateTableFORMACAO_EDUCACIONAL.toString());
        db.execSQL(CreateTableEXPERIENCIA_PROFISSIONAL.toString());
        Log.e("TABLE EXP PROFI", CreateTableEXPERIENCIA_PROFISSIONAL.toString());
        db.execSQL(CreateTableQUALIFICACAO_PROFISSIONAL.toString());
        Log.e("TABLE QUALI PROFI", CreateTableQUALIFICACAO_PROFISSIONAL.toString());
        db.execSQL(CreateTableINFORMACAO_ADICIONAL.toString());
        Log.e("TABLE INFO ADICIONAL", CreateTableINFORMACAO_ADICIONAL.toString());

        String insertPessoa = "INSERT INTO `Pessoa` VALUES (1,'Gabriela','Brasileira','Viúva','69/69/1969','(69) 6969-6969','gabicemi@gememail.com','Sao Joaquim - SC', 'Rua Demétrio Ribeiro, 69','Ser ryca, poderosah e ver todas as inimigah terem vida longa pra que vejam cada dia mais minha vitória, amém.');";
        String insertQualiProfi = "INSERT INTO `QualificacaoProfissional` " +
                "VALUES (1,'Assiste video aula no youtube',1), " +
                "(2,'Sabe usar o google',1), " +
                "(3,'Encontra muitas respostas da vida no Stack Overflow',1);";
        String insertInfoAdic = "INSERT INTO `InformacaoAdicional` " +
                "VALUES (1,'Ninguém quer saber.',1), " +
                "(2,'Serião, ninguém quer saber',1);";
        String insertIdiomas = "INSERT INTO `Idioma` VALUES (1,'hue-br',1) , (2,'huehuehue-br',1);";
        String insertFormEduc = "INSERT INTO `FormacaoEducacional` " +
                "VALUES (1,'A vida',1), " +
                "(2,'A MORTE',1), " +
                "(3,'Os rps',1);";
        String insertExpProfi = " INSERT INTO `ExperienciaProfissional` " +
                "VALUES (1,'Garoto de programa',1), " +
                "(2,'Preparador de café',1), " +
                "(3,'Trouxa',1);";

        Log.e("INSERT PESSOA", insertPessoa.toString());
        db.execSQL(insertPessoa.toString());
        Log.e("INSERT QUALIF PROFI", insertQualiProfi.toString());
        db.execSQL(insertQualiProfi.toString());
        Log.e("INSERT INFO ADIC", insertInfoAdic.toString());
        db.execSQL(insertInfoAdic.toString());
        Log.e("INSERT IDIOMAS", insertIdiomas.toString());
        db.execSQL(insertIdiomas.toString());
        Log.e("INSERT FORM EDUC", insertFormEduc.toString());
        db.execSQL(insertFormEduc.toString());
        Log.e("INSERT EXP PROFI", insertExpProfi.toString());
        db.execSQL(insertExpProfi.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public SQLiteDatabase getConexaoDataBase(){
        return this.getWritableDatabase();
    }

    public void closeConexaoDataBase(){
        this.close();
    }


}
