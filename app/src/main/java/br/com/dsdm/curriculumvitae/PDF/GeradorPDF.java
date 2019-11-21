package br.com.dsdm.curriculumvitae.PDF;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import br.com.dsdm.curriculumvitae.Model.ExperienciaProfissional;
import br.com.dsdm.curriculumvitae.Model.FormacaoEducacional;
import br.com.dsdm.curriculumvitae.Model.Idiomas;
import br.com.dsdm.curriculumvitae.Model.InfoAdicionais;
import br.com.dsdm.curriculumvitae.Model.Objetivo;
import br.com.dsdm.curriculumvitae.Model.QualificacoesProfissionais;
import br.com.dsdm.curriculumvitae.Model.Repositorio.RepositoryCurriculum;
import br.com.dsdm.curriculumvitae.R;

import br.com.dsdm.curriculumvitae.Controller.ctrlDadosPessoais;

public class GeradorPDF {
    private static final String TAG = "GeradorPDF";
    private RepositoryCurriculum rp;

    public File montaPDF (Context context) throws FileNotFoundException, DocumentException{
        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Curriculum");
        this.rp = RepositoryCurriculum.getInstance(context);
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "NOVO DIRETÓRIO PARA PDF CRIADO");
        }
        ArrayList<ExperienciaProfissional> experienciaProfissionals = (ArrayList<ExperienciaProfissional>) rp.getExperienciaProfissional();
        ArrayList<FormacaoEducacional> formacaoEducacionals = (ArrayList<FormacaoEducacional>) rp.getFormacaoEducacional();
        ArrayList<Idiomas> idiomases = (ArrayList<Idiomas>) rp.getIdiomas();
        ArrayList<InfoAdicionais> infoAdicionaises = (ArrayList<InfoAdicionais>) rp.getInfoAdicionais();
        ArrayList<QualificacoesProfissionais> qualificacoesProfissionaises = (ArrayList<QualificacoesProfissionais>) rp.getQualificacoesProfissionais();
        String nome = rp.getPessoa().getNome();
        String rua = rp.getPessoa().getEndereco();
        String cidade = rp.getPessoa().getCidade();
        String nacionalidade = rp.getPessoa().getNacionalidade();
        String telefone = rp.getPessoa().getTelefone();
        String email = rp.getPessoa().getEmail();
        String nascimento = rp.getPessoa().getNascimento();
        String estadoCivil = rp.getPessoa().getEstadoCivil();

        File pdfFile = new File(docsFolder.getAbsolutePath(), "CurriculumVitae.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(5);
        PdfWriter.getInstance(document, output);
        document.setMargins(20, 20, 30, 30);
        document.setMarginMirroring(false);
        document.open();

        PdfPTable header = new PdfPTable(2);
        header.setWidths(new int[]{2, 12});
        header.setTotalWidth(527);
        header.setLockedWidth(true);
        header.getDefaultCell().setFixedHeight(50);
        header.getDefaultCell().setBorder(Rectangle.BOTTOM);
        header.getDefaultCell().setBorderColor(BaseColor.BLACK);

        DottedLineSeparator dottedline = new DottedLineSeparator();
        dottedline.setOffset(-2);
        dottedline.setGap(2f);

        Bitmap bitmap;
        if (ctrlDadosPessoais.thumbnail == null) {
            Drawable d = ContextCompat.getDrawable(context, R.drawable.student);
            bitmap = ((BitmapDrawable) d).getBitmap();
        } else {
            bitmap = ctrlDadosPessoais.thumbnail;
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapData = stream.toByteArray();
        Image image = null;
        try {
            image = Image.getInstance(bitmapData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        header.addCell(image);

        PdfPCell text = new PdfPCell();
        text.setPaddingBottom(15);
        text.setPaddingLeft(30);
        text.setBorder(Rectangle.BOTTOM);
        text.setBorderColor(BaseColor.BLACK);
        text.addElement(new Phrase(rp.getPessoa().getNome(), new Font(Font.FontFamily.HELVETICA, 24)));
        text.setVerticalAlignment(Element.ALIGN_CENTER);
        header.addCell(text);

        PdfPTable infoProcesso = new PdfPTable(2);
        infoProcesso.setTotalWidth(PageSize.A4.getWidth() - 72);
        infoProcesso.setLockedWidth(true);
        infoProcesso.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPTable leftTable = new PdfPTable(2);
        leftTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        PdfPCell leftCellLabel = new PdfPCell(Phrase.getInstance("\nRua: " +
                "\n\nCidade: " +
                "\nNacionalidade: "));
        leftCellLabel.setBorder(Rectangle.NO_BORDER);
        leftCellLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        PdfPCell leftCell = new PdfPCell(Phrase.getInstance("\n" + rua +
                "\n" + cidade +
                "\n" + nacionalidade + "\n\n" ));
        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftCell.setBorder(Rectangle.NO_BORDER);
        leftTable.addCell(leftCellLabel);
        leftTable.addCell(leftCell);

        PdfPTable rightTable = new PdfPTable(2);
        rightTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell rightCellLabel = new PdfPCell(Phrase.getInstance(("\nData de Nascimento: " +
                "\nTelefone: " +
                "\nE-mail: " +
                "\nEstado Civil: ")));
        rightCellLabel.setBorder(Rectangle.NO_BORDER);
        rightCellLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);

        PdfPCell rightCell = new PdfPCell(Phrase.getInstance("\n" + nascimento + "\n" + telefone +
                "\n"+ email +
                "\n" + estadoCivil + "\n\n" ));
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        rightTable.addCell(rightCellLabel);
        rightTable.addCell(rightCell);

        document.add(header);

        infoProcesso.addCell(leftTable);
        infoProcesso.addCell(rightTable);

        PdfPCell space = new PdfPCell(Phrase.getInstance("\n\n\n"));
        rightCell.setBorder(Rectangle.NO_BORDER);
        rightCell.setHorizontalAlignment(Element.ALIGN_LEFT);

        rightTable.addCell(rightCellLabel);
        rightTable.addCell(rightCell);

        infoProcesso.addCell(rightTable);

        document.add(infoProcesso);

        document.add(dottedline);
        PdfPTable experiencia = new PdfPTable(1);
        experiencia.setTotalWidth(PageSize.A4.getWidth() - 72);
        experiencia.setLockedWidth(true);
        experiencia.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        experiencia.addCell(new Phrase("Experiências Profissionais\n", new Font(Font.FontFamily.HELVETICA, 22)));
        for (ExperienciaProfissional experiencias : experienciaProfissionals) {
            experiencia.addCell(new Phrase(experiencias.getExperiencia()));
        }
        document.add(experiencia);

        document.add(dottedline);
        PdfPTable formacoes = new PdfPTable(1);
        formacoes.setTotalWidth(PageSize.A4.getWidth() - 72);
        formacoes.setLockedWidth(true);
        formacoes.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        formacoes.addCell(new Phrase("Formações Educacionais\n", new Font(Font.FontFamily.HELVETICA, 22)));
        for (FormacaoEducacional formacaoEducacional : formacaoEducacionals) {
            formacoes.addCell(new Phrase(formacaoEducacional.getFormacao()));
        }
        document.add(formacoes);

        document.add(dottedline);
        PdfPTable qualificacoes = new PdfPTable(1);
        qualificacoes.setTotalWidth(PageSize.A4.getWidth() - 72);
        qualificacoes.setLockedWidth(true);
        qualificacoes.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        qualificacoes.addCell(new Phrase("Qualificações Profissionais\n", new Font(Font.FontFamily.HELVETICA, 22)));
        for (QualificacoesProfissionais qualificacoesProfissionais : qualificacoesProfissionaises) {
            qualificacoes.addCell(new Phrase(qualificacoesProfissionais.getQualificacao()));
        }
        document.add(qualificacoes);

        document.add(dottedline);
        PdfPTable idiomas = new PdfPTable(1);
        idiomas.setTotalWidth(PageSize.A4.getWidth() - 72);
        idiomas.setLockedWidth(true);
        idiomas.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        idiomas.addCell(new Phrase("Idiomas\n", new Font(Font.FontFamily.HELVETICA, 22)));
        for (Idiomas idiomas1 : idiomases) {
            idiomas.addCell(new Phrase(idiomas1.getIdioma()));
        }
        document.add(idiomas);

        document.add(dottedline);
        PdfPTable infoAd = new PdfPTable(1);
        infoAd.setTotalWidth(PageSize.A4.getWidth() - 72);
        infoAd.setLockedWidth(true);
        infoAd.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        infoAd.addCell(new Phrase("Informações Adicionais\n", new Font(Font.FontFamily.HELVETICA, 22)));
        for (InfoAdicionais infoAdicionais : infoAdicionaises) {
            infoAd.addCell(new Phrase(infoAdicionais.getInfoAdicional()));
        }
        document.add(infoAd);

        document.add(dottedline);
        PdfPTable objetivo = new PdfPTable(1);
        objetivo.setTotalWidth(PageSize.A4.getWidth() - 72);
        objetivo.setLockedWidth(true);
        objetivo.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        objetivo.addCell(new Phrase("Objetivo\n", new Font(Font.FontFamily.HELVETICA, 22)));
        objetivo.addCell(new Phrase(rp.getObjetivo().getObjetivo()));
        document.add(objetivo);

        document.close();
        return pdfFile;
    }

}
