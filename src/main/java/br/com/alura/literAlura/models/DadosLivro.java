package br.com.alura.literAlura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivro(Integer id,
                                       @JsonAlias("title") String titulo,
                                       @JsonAlias("authors") List<DadosAutores> autores,
                                       @JsonAlias("languages") List<String> idiomas,
                                       @JsonAlias("download_count") Integer quantidadeDownloads,
                                       @JsonAlias("formats") DadosFormatos formatos){
}



