package br.com.alura.literAlura.models;


import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String idioma;
    private String poster;
    private Integer quantidadeDownloads;
    @ManyToOne
    private Autor autor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }



    public Livro(DadosLivro dadosLivro) {
        this.titulo = dadosLivro.titulo();
        this.poster = dadosLivro.formatos().poster();
        this.idioma = String.join(" ", dadosLivro.idiomas());
        this.quantidadeDownloads = dadosLivro.quantidadeDownloads();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getQuantidadeDownloads() {
        return quantidadeDownloads;
    }

    public void setQuantidadeDownloads(Integer quantidadeDownloads) {
        this.quantidadeDownloads = quantidadeDownloads;
    }


    @Override
    public String toString() {
        return "----- LIVRO -----\n"+
                "Titulo: "+ titulo+"\n"+
                "Idioma: "+ idioma+"\n";
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Livro() {
    }
}
