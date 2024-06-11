package br.com.alura.literAlura.repository;

import br.com.alura.literAlura.models.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.anoNascimento <= :ano AND a.anoFalecimento>:ano")
    List<Autor> BuscaAutoresVivosNumAnoDado(Integer ano);

    Optional<Autor> findByNomeAutorEqualsIgnoreCase(String nomeAutor);

    Optional<Autor> findFirstByNomeAutorContainingIgnoreCase(String nomeAutor);
}