package petshop.model.services;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import petshop.model.business.ServicoBusiness;
import petshop.model.controllers.PetController;
import petshop.model.dtos.response.*;

public class PdfService {

    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Gerar PDF Pets cadastrado
        public void gerarRelatorioPets(String path, RelatorioPetDTO relatorio) {

            Document document = new Document();

            try {
                PdfWriter.getInstance(document, new FileOutputStream(path+"//Relatório pets - "+dtf.format(LocalDateTime.now())+".pdf", false));
                document.open();
                Font fontTitle = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);
                Paragraph title = new Paragraph(new Phrase(14F , "Relatorio pets cadastrados", fontTitle));
                title.setAlignment(Element.ALIGN_CENTER);

                document.add(title);
                document.add(new Paragraph(" "));
                document.add(new Paragraph(("Total pets cadastrados: "+relatorio.getTotalPetsCadastrados())));
                document.add(new Paragraph(("Pet com maior ponto de fidelidade: "+relatorio.getPetComMaiorPontoDeFidelidade())));
                document.add(new Paragraph(("Quantidade de pets inativos: "+relatorio.getNumeroPetsInativos())));
                document.add(new Paragraph(("Tipo de pet mais cadastrado: "+relatorio.getTipoMaisCadastrado())));
                document.add(new Paragraph(" "));

                if(!relatorio.getListaPets().isEmpty()) {
                    Font fontTable = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
                    Paragraph tableTitle = new Paragraph(new Phrase(14F, "Lista de pets", fontTable));
                    title.setAlignment(Element.ALIGN_CENTER);
                    document.add(tableTitle);
                    document.add(new Paragraph(" "));
                    PdfPTable tabelaPets = new PdfPTable(9);
                    tabelaPets.setTotalWidth(new float[]{
                            20, 80, 80, 90, 60, 60, 60, 60, 60
                    });
                    tabelaPets.setLockedWidth(true);

                    PdfPCell header1 = new PdfPCell(new Phrase("ID"));
                    header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabelaPets.addCell(header1);

                    PdfPCell header2 = new PdfPCell(new Phrase("NOME"));
                    header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabelaPets.addCell(header2);

                    PdfPCell header3 = new PdfPCell(new Phrase("DATA NASCIMENTO"));
                    header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabelaPets.addCell(header3);

                    PdfPCell header4 = new PdfPCell(new Phrase("NOME DONO"));
                    header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabelaPets.addCell(header4);

                    PdfPCell header5 = new PdfPCell(new Phrase("RAÇA"));
                    header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabelaPets.addCell(header5);

                    PdfPCell header6 = new PdfPCell(new Phrase("TIPO ANIMAL"));
                    header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabelaPets.addCell(header6);

                    PdfPCell header7 = new PdfPCell(new Phrase("SEXO"));
                    header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabelaPets.addCell(header7);

                    PdfPCell header8 = new PdfPCell(new Phrase("ATIVO"));
                    header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabelaPets.addCell(header8);

                    PdfPCell header9 = new PdfPCell(new Phrase("PONTOS DE FIDELIDADE"));
                    header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    tabelaPets.addCell(header9);

                    relatorio.getListaPets().forEach(pet -> {
                        tabelaPets.addCell(pet.getIdPet().toString());
                        tabelaPets.addCell(pet.getNome());
                        tabelaPets.addCell(pet.getDataNascimento());
                        tabelaPets.addCell(pet.getNomeDono());
                        tabelaPets.addCell(pet.getRaca());
                        tabelaPets.addCell(pet.getTipoAnimal());
                        tabelaPets.addCell(pet.getSexo());
                        tabelaPets.addCell(pet.getAtivo());
                        tabelaPets.addCell(pet.getPontosFidelidade().toString());
                    });
                    document.add(tabelaPets);
                }

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (DocumentException e) {

                e.printStackTrace();
            } finally {
                document.close();
            }
//
//            try {
//                Desktop.getDesktop().open(new File(path+"//Relatório pets - "+dtf.format(LocalDateTime.now())));
//            } catch (IOException e) {
//
//                e.printStackTrace();
//            }

        }


    // Gerar PDF Pets cadastrado
    public void gerarRelatorioServicos(String path, RelatorioServicoDTO relatorio) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(path+"//Relatório serviços - "+dtf.format(LocalDateTime.now())+".pdf", false));
            document.open();
            Font fontTitle = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);
            Paragraph title = new Paragraph(new Phrase(14F , "Relatorio serviços cadastrados", fontTitle));
            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Número de serviços: "+relatorio.getNumeroServicos()));
            document.add(new Paragraph("Número de serviços inativos: "+relatorio.getNumeroServicosInativos()));
            document.add(new Paragraph("Nome do serviço mais barato: "+relatorio.getServicoMaisBarato()));
            document.add(new Paragraph("Nome do serviço mais caro: "+relatorio.getServicoMaisCaro()));
            document.add(new Paragraph("Nome do serviço mais utilizado: "+relatorio.getServicoMaisUtilizado()));
            document.add(new Paragraph("Nome do último serviço cadastrado: "+relatorio.getUltimoServicoCadastrado()));

            try {
                document.add(new Paragraph(" "));
                Font fontTable = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
                Paragraph tableTitle = new Paragraph(new Phrase(14F , "Lista de serviços", fontTable));
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(tableTitle);
                document.add(new Paragraph(" "));
                AtomicInteger count = new AtomicInteger(1);
                PdfPTable tabelaServicos = new PdfPTable(5);
                tabelaServicos.setTotalWidth(new float[]{
                        20, 120, 120, 140, 140
                });
                tabelaServicos.setLockedWidth(true);

                PdfPCell header1 = new PdfPCell(new Phrase("#"));
                header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaServicos.addCell(header1);

                PdfPCell header2 = new PdfPCell(new Phrase("Serviço"));
                header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaServicos.addCell(header2);

                PdfPCell header3 = new PdfPCell(new Phrase("Valor"));
                header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaServicos.addCell(header3);

                PdfPCell header4 = new PdfPCell(new Phrase("Descrição"));
                header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaServicos.addCell(header4);

                PdfPCell header5 = new PdfPCell(new Phrase("Status"));
                header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaServicos.addCell(header5);

                relatorio.getListaServicos().forEach(servico ->{
                    tabelaServicos.addCell(count.toString());
                    tabelaServicos.addCell(servico.getNome());
                    tabelaServicos.addCell(servico.getValor());
                    tabelaServicos.addCell(servico.getDescricao());
                    tabelaServicos.addCell(servico.getStatus());
                    count.getAndIncrement();
                });
                document.add(tabelaServicos);


            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

    }

        //Formatar valor
        private String formatarValor(Double valor) {
            DecimalFormat decimalFormat = new DecimalFormat("R$ 0.00");
            return decimalFormat.format(valor);

        }

    public void gerarRelatorioAtendimento(String path, RelatorioAtendimentoDTO relatorio) {
        Document document = new Document();
        try{
            //AQUI EU PASSO O PATH E COM O NOME DO RELATÓRIO
            PdfWriter.getInstance(document, new FileOutputStream(path+"//Relatório atendimentos - "+dtf.format(LocalDateTime.now())+".pdf", false));
            document.open();
            Font fontTitle = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.BLACK);
            Paragraph title = new Paragraph(new Phrase(14F , "Relatorio Atendimentos", fontTitle));
            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Número de atendimentos: "+(relatorio.getNumeroAtendimentos())));
            document.add(new Paragraph("Atendimentos agendados: "+(relatorio.getAtendimentosAgendados())));
            document.add(new Paragraph("Atendimentos realizados: "+(relatorio.getAtendimentosRealizados())));
            document.add(new Paragraph("Atendimentos cancelados: "+(relatorio.getAtendimentosCancelados())));
            document.add(new Paragraph("Data do último atendimento: "+(relatorio.getUltimoAtendimento())));
            document.add(new Paragraph("Tipo do pet mais atendido: "+(relatorio.getTipoPetMaisAtendido())));
            document.add(new Paragraph("Valor total dos atendimentos (Realizados): "+(relatorio.getValorTotalAtendimentosRealizados())));

            if(!relatorio.getListaAtendimentos().isEmpty()){
                document.add(new Paragraph(" "));
                Font fontTable = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
                Paragraph tableTitle = new Paragraph(new Phrase(14F , "Lista de atendimentos", fontTable));
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(tableTitle);
                document.add(new Paragraph(" "));
                AtomicInteger count = new AtomicInteger(1);
                PdfPTable tabelaAtendimentos = new PdfPTable(7);
                tabelaAtendimentos.setTotalWidth(new float[]{
                        20, 80, 80, 100, 100, 80, 80
                });
                tabelaAtendimentos.setLockedWidth(true);

                PdfPCell header1 = new PdfPCell(new Phrase("#"));
                header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaAtendimentos.addCell(header1);

                PdfPCell header2 = new PdfPCell(new Phrase("Pet"));
                header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaAtendimentos.addCell(header2);

                PdfPCell header3 = new PdfPCell(new Phrase("Tipo do pet"));
                header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaAtendimentos.addCell(header3);

                PdfPCell header4 = new PdfPCell(new Phrase("Nome do serviço"));
                header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaAtendimentos.addCell(header4);

                PdfPCell header5 = new PdfPCell(new Phrase("Valor serviço"));
                header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaAtendimentos.addCell(header5);

                PdfPCell header6 = new PdfPCell(new Phrase("Data do atendimento"));
                header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaAtendimentos.addCell(header6);

                PdfPCell header7 = new PdfPCell(new Phrase("Status atendimento"));
                header1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabelaAtendimentos.addCell(header7);

                relatorio.getListaAtendimentos().forEach(atendimento ->{
                    tabelaAtendimentos.addCell(count.toString());
                    tabelaAtendimentos.addCell(atendimento.getPetNome());
                    tabelaAtendimentos.addCell(atendimento.getPetTipoAnimal());
                    tabelaAtendimentos.addCell(atendimento.getServicoNome());
                    tabelaAtendimentos.addCell(atendimento.getServicoValor());
                    tabelaAtendimentos.addCell(atendimento.getDataAtendimento());
                    tabelaAtendimentos.addCell(atendimento.getStatusAtendimento());
                    count.getAndIncrement();
                });
                document.add(tabelaAtendimentos);
            }

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
