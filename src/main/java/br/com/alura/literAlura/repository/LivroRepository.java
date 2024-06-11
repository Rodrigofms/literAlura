package br.com.alura.literAlura.repository;

import br.com.alura.literAlura.models.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findTop5ByOrderByQuantidadeDownloadsDesc();
    List<Livro> findByIdiomaContainingIgnoreCase(String idioma);
    Optional<Livro> findByTituloEqualsIgnoreCase(String titulo);
}
