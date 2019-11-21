package br.com.dsdm.curriculumvitae.Model;

import java.util.ArrayList;

public class ExperienciaProfissional {

    private String experiencia;
    private ArrayList experiencias;

    public ExperienciaProfissional(String experiencia){
        this.experiencia = experiencia;
    }

    public ExperienciaProfissional(){

    }

    public String getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(String experiencia) {
        this.experiencia = experiencia;
    }


    public ArrayList getExperiencias() {
        return experiencias;
    }

    public void setExperiencias(ArrayList experiencias) {
        this.experiencias = experiencias;
    }
}
