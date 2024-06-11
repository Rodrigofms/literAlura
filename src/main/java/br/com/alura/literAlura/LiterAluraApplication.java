package br.com.alura.literAlura;

import br.com.alura.literAlura.principal.Principal;
import br.com.alura.literAlura.repository.AutorRepository;
import br.com.alura.literAlura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}
	@Autowired
	private AutorRepository autorRepository;

	@Autowired
	private LivroRepository livroRepository;



	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(livroRepository,autorRepository);
		principal.exibeMenu();
	}
}
