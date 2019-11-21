package br.com.dsdm.curriculumvitae.DataBase;

import android.provider.BaseColumns;

public class DBContract {

    public static abstract class Pessoa implements BaseColumns {
        public static final String NOME_TABELA = "Pessoa";
        public static final String COLUNA_NOME = "nome";
        public static final String COLUNA_NACIONALIDADE = "nacionalidade";
        public static final String COLUNA_ESTADOCIVIL = "estCivil";
        public static final String COLUNA_DATANASCIMENTO = "dtNasc";
        public static final String COLUNA_TELEFONE = "telefone";
        public static final String COLUNA_EMAIL = "email";
        public static final String COLUNA_CIDADE = "email";
        public static final String COLUNA_ENDERECO = "endereco";
        public static final String COLUNA_OBJETIVO = "objetivo";
    }

    public static abstract class ExperienciaProfissional implements BaseColumns {
        public static final String NOME_TABELA = "ExperienciaProfissional";
        public static final String COLUNA_DESC = "descExpProfi";
        public static final String COLUNA_FK = "idPessoa";
    }

    public static abstract class FormacaoEducacional implements BaseColumns {
        public static final String NOME_TABELA = "FormacaoEducacional";
        public static final String COLUNA_DESC = "descFormEdu";
        public static final String COLUNA_FK = "idPessoa";
    }

    public static abstract class Idioma implements BaseColumns {
        public static final String NOME_TABELA = "Idioma";
        public static final String COLUNA_DESC = "nomeIdioma";
        public static final String COLUNA_FK = "idPessoa";
    }

    public static abstract class InformacaoAdicional implements BaseColumns {
        public static final String NOME_TABELA = "InformacaoAdicional";
        public static final String COLUNA_DESC = "descInfoAdicional";
        public static final String COLUNA_FK = "idPessoa";
    }

    public static abstract class QualificacaoProfissional implements BaseColumns {
        public static final String NOME_TABELA = "QualificacaoProfissional";
        public static final String COLUNA_DESC = "descQualiProfi";
        public static final String COLUNA_FK = "idPessoa";
    }

}
