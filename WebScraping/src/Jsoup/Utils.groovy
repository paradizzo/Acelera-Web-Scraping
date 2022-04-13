package Jsoup

import au.com.bytecode.opencsv.CSVWriter
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import java.nio.file.Files
import java.nio.file.Paths

class Utils {
    static void pegaInfo() {
        //Declaração de variaveis


        int page = 1
        int pageMedia = 1
        int valorTotal = 0
        int quantidadeDeItems = 0
        Writer writer = Files.newBufferedWriter(Paths.get("pessoas.csv"))
        CSVWriter csvWriter = new CSVWriter(writer)
        List<String[]> linhas = new ArrayList<>()
        // Esse while descobre o valor médio dos Iphones
        println("========================= VERIFICANDO MÉDIA DE VALORES E QUANTIDADE DE ITENS =======================")
        while (pageMedia < 4) {
            println("==================================================================ESTOU NA PAGINA ${pageMedia}========================================")
            Document doc = Jsoup.connect("https://go.olx.com.br/grande-goiania-e-anapolis?o=${pageMedia}&q=Iphone%2011&sp=2").timeout(20000).get()
            Elements body  = doc.select(".kntIvV")
            quantidadeDeItems += body.select(".fvbmlV .bDLpyo").size()
            for (e in body.select(".fvbmlV .bDLpyo")) {
                String valor = e.select(".bDLpyo .kQcyga .gSNULD .fqDYpJ .bGMpGA .bRSExF .bXoWSt .hzqyCO .kaNiaQ").html()
                valor = valor.replaceAll("[^0-9]","")
                int valorInt = Integer.parseInt(valor)
                valorTotal += valorInt
            }
            
            pageMedia += 1
        }

        int valorMedio = valorTotal/quantidadeDeItems
        println(valorTotal)
        println(quantidadeDeItems)
        println("O valor médio é: ${valorMedio}")

        // Esse while pega o resto das info e separa os items pela media
        while (page < 4) {
            println("==================================================================ESTOU NA PAGINA ${page}========================================")
            Document doc = Jsoup.connect("https://go.olx.com.br/grande-goiania-e-anapolis?o=${page}&q=Iphone%2011&sp=2").timeout(20000).get()
            Elements body  = doc.select(".kntIvV")
            for (e in body.select(".fvbmlV .bDLpyo")) {
                String titulo = e.select(".bDLpyo .kQcyga").attr("title")
                String valorIndividual = e.select(".bDLpyo .kQcyga .gSNULD .fqDYpJ .bGMpGA .bRSExF .bXoWSt .hzqyCO .kaNiaQ").html()
                String endereco = e.select('.cmFKIN').attr("title")
                String URL_ANUNCIO = e.select(".bDLpyo .kQcyga").attr("href")
                valorIndividual = valorIndividual.replaceAll("[^0-9]","")
                int valorInt = Integer.parseInt(valorIndividual)
                if (valorInt <= valorMedio) {
                    println("=========")
                    System.out.println("Nome: ${titulo}")
                    System.out.println("Valor do produto: ${valorIndividual}")
                    println("Endereço do vendedor: ${endereco}")
                    println("URL do ánuncio: ${URL_ANUNCIO} ")
                    println("=========")
                    linhas.add(new String[]{"${titulo}","${valorIndividual}","${endereco}","${URL_ANUNCIO}"})
                } else  {
                    println("Produto fora dos padrões")
                }
            }
            page += 1
        }csvWriter.writeAll(linhas)
        csvWriter.flush()
        writer.close()

}
}

