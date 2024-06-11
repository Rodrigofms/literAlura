package br.com.alura.literAlura.principal;

import br.com.alura.literAlura.models.*;
import br.com.alura.literAlura.repository.AutorRepository;
import br.com.alura.literAlura.repository.LivroRepository;
import br.com.alura.literAlura.services.ConsumoAPI;
import br.com.alura.literAlura.services.ConverteDados;


import java.util.*;

import static java.util.Comparator.comparing;


public class Principal {

    private AutorRepository AREPOSITORY;
    private LivroRepository LREPOSITORY;


    Scanner SC = new Scanner(System.in);
    private final ConsumoAPI CONSUMO = new ConsumoAPI();
    private final ConverteDados CONVERSOR = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books/?search=";
    List<Livro> livros = new ArrayList<>();
    List<Autor> autores = new ArrayList<>();

    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        LREPOSITORY = livroRepository;
        AREPOSITORY = autorRepository;
    }


    public void exibeMenu() {
        var opcao = -1;

        while (opcao != 0) {
            var menu = """
                    *** Escolha uma Opção ***

                    1 - Buscar livro pelo titulo
                    2 - Buscar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em determinado ano
                    5 - Listar livros em um determinado idioma
                    6 - Top 5 Livros
                    7 - Buscar Autor por nome

                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = SC.nextInt();
            SC.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroPorTitulo();
                    break;
                case 2:
                    buscarLivrosRegistrados();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    listarLivrosEmDeterminadoIdioma();
                    break;
                case 6:
                    obterTop5Livros();
                    break;
                case 7:
                    obterAutorPorNome();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }


    private DadosGerais obterDadosGerais() {
        System.out.println("Digite o titulo do livro: ");
        var nomeLivro = SC.nextLine();
        var json = CONSUMO.obterDados(ENDERECO + nomeLivro.replace(" ", "%20").toLowerCase().trim());
        return CONVERSOR.obterDados(json, DadosGerais.class);
    }

    private void buscarLivroPorTitulo() {

        DadosGerais dadosGerais = obterDadosGerais();

        Optional<DadosLivro> optDadoLivro = dadosGerais.dadosLivros()
                .stream()
                .sorted(comparing(DadosLivro::id))
                .findFirst();

        if (optDadoLivro.isPresent()) {
            DadosLivro dadosLivro = optDadoLivro.get();
            String titulo = dadosLivro.titulo();

            Optional<Livro> optLivro = LREPOSITORY.findByTituloEqualsIgnoreCase(titulo);

            if (optLivro.isPresent()) {
                System.out.println("Esse livro já esta cadastrado");
            } else {
                imprimirDadosLivro(dadosLivro);
                System.out.println("Digite 1 se é o livro que buscava ou 2 se não é o livro");
                var resposta = SC.nextInt();
                SC.nextLine();
                if (resposta == 1) {
                    Autor autor = new Autor(dadosLivro.autores().get(0));
                    Livro livro = new Livro(dadosLivro);
                    autor.setLivro(livro);

                    Optional<Autor> optAutor = AREPOSITORY.findByNomeAutorEqualsIgnoreCase(autor.getNomeAutor());

                    if (optAutor.isPresent()) {
                        Autor autorRegistrado = optAutor.get();
                        livro.setAutor(autorRegistrado);
                        LREPOSITORY.save(livro);
                    } else {
                        AREPOSITORY.save(autor);
                    }
                    System.out.println("Livro salvo com sucesso!");
                } else {
                    System.out.println("Tente colocar mais palavras ao titulo");
                }
            }
        } else {
            System.out.println("Livro nao encontrado");
        }
    }

    private void imprimirDadosLivro(DadosLivro dataLivro) {
        System.out.println("-------Livro---------");
        System.out.println("Titulo: " + dataLivro.titulo());
        dataLivro.autores().forEach(this::imprimirDadosAutor);
        System.out.println("Idioma: " + String.join(" ", dataLivro.idiomas()));
        System.out.println("Numero de downloads: " + dataLivro.quantidadeDownloads());
        System.out.println("Poster: " + dataLivro.formatos().poster());
        System.out.println("---------------------");
        System.out.println("\n");
    }


    private void imprimirDadosAutor(DadosAutores dataAutor) {
        System.out.println("Autor: " + dataAutor.nomeAutor());
    }


    private void buscarLivrosRegistrados() {
        livros = LREPOSITORY.findAll();
        if (livros.isEmpty()) {
            System.out.println("========== Não existem livros registrados ==========");
        }
        livros.sort(Comparator.comparing(Livro::getIdioma));

        System.out.println("=================== Livros ========================");

        livros.forEach(l -> {
            System.out.println("Titulo: " + l.getTitulo());
            System.out.println("Idioma: " + l.getIdioma());
            System.out.println("Downloads: " + l.getQuantidadeDownloads());
            System.out.println("Poster: " + l.getPoster());
            System.out.println("Autor: " + l.getAutor().getNomeAutor());

            System.out.println("-----------------------------------------------------");
        });

    }



    private void listarAutores() {
        autores = AREPOSITORY.findAll();

        if(autores.isEmpty()){
            System.out.println("=========== Não existem autores registrados ============");
        }else{
            System.out.println("=================== Autores ========================");

            autores.sort(Comparator.comparing(Autor::getNomeAutor));
            imprimirAutores(autores);
        }
    }

    private void imprimirAutores(List<Autor> autores) {
        autores.forEach(a -> {
            System.out.println("Nome: " + a.getNomeAutor());
            System.out.println("Nascimento: " + a.getAnoNascimento());
            System.out.println("Morte: " + a.getAnoFalecimento());

            List<String> listaTitulos = new ArrayList<>();
            a.getLivros().forEach(l -> listaTitulos.add(l.getTitulo()));

            System.out.println("Livros: " + listaTitulos);
            System.out.println("-----------------------------------------------------");
        });
    }

    private void listarAutoresVivos(){
        System.out.println("Insira o ano:");
        Integer ano = SC.nextInt();
        SC.nextLine();

        List<Autor> autoresVivos = AREPOSITORY.BuscaAutoresVivosNumAnoDado(ano);

        if(autoresVivos.isEmpty()){
            System.out.println("Não tem registrados autores vivos no ano " + ano);
        }else{
            System.out.println("========= Autores vivos no ano " + ano + " ===============");
            System.out.println('\n');
            imprimirAutores(autoresVivos);
        }
    }

    private void obterTop5Livros(){
        List<Livro> topLivros = LREPOSITORY.findTop5ByOrderByQuantidadeDownloadsDesc();

        if(topLivros.isEmpty()){
            System.out.println("Não tem livros registrados");
        }else{
            System.out.println("================ Top 5 Livros ================");
            System.out.println('\n');
            topLivros.forEach(l->
                    System.out.println("Titulo: " + l.getTitulo() + " , Número de downloads: " + l.getQuantidadeDownloads()));
        }
    }

    private void listarLivrosEmDeterminadoIdioma(){
        String menuIdioma =
                """                                              
                Esreva o idioma por favor
                ---------------------------
                 pt - Português
                 en - Inglês
                 es - Espanhol
                 fr - Francês
                ---------------------------
                """;
        System.out.println(menuIdioma);
        var lingua = SC.nextLine();
        List<Livro> livrosNumaLingua = LREPOSITORY.findByIdiomaContainingIgnoreCase(lingua);

        if(livrosNumaLingua.isEmpty()){
            System.out.println("Não tem livros registrados no idioma " + lingua);

        }else{
            System.out.println("================ Livros no idioma " + lingua + " ================");
            System.out.println('\n');
            livrosNumaLingua.forEach(l->
                    System.out.println("Titulo: " + l.getTitulo() + " , Autor: " + l.getAutor()));
        }
    }

    private void obterAutorPorNome() {
        System.out.println("Inserte o nome do autor que deseja procurar");
        String nome = SC.nextLine();

        Optional<Autor> optAutor = AREPOSITORY.findFirstByNomeAutorContainingIgnoreCase(nome);
        if(optAutor.isPresent()){

            Autor autorBuscado = optAutor.get();
            List<Autor> listaParaImprimir = new ArrayList<>();
            listaParaImprimir.add(autorBuscado);
            System.out.println();
            System.out.println("================== Dados do Autor ==================");
            imprimirAutores(listaParaImprimir);

        }else{
            System.out.println("========== Autor não registrado no banco de dados ============");
            System.out.println('\n');
        }
    }

}




