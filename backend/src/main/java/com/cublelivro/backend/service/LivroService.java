package com.cublelivro.backend.service;

import com.cublelivro.backend.model.Livro;
import com.cublelivro.backend.repository.LivroRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class LivroService {

    private final LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public List<Livro> listarTodos() {
        return repository.findAll();
    }

    public Livro buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado."));
    }

    public Livro salvar(Livro livro) {
        if (livro.getStatus() == null) {
            livro.setStatus("DISPONIVEL");
        }
        return repository.save(livro);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public Livro emprestar(Long id) {
        Livro livro = buscarPorId(id);

        if ("EMPRESTADO".equals(livro.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este livro já está emprestado no momento.");
        }

        livro.setStatus("EMPRESTADO");
        livro.setDataEmprestimo(LocalDate.now());
        livro.setDataDevolucaoPrevista(LocalDate.now().plusDays(7));

        return repository.save(livro);
    }

    public Livro devolver(Long id) {
        Livro livro = buscarPorId(id);
        livro.setStatus("DISPONIVEL");
        livro.setDataEmprestimo(null);
        livro.setDataDevolucaoPrevista(null);
        return repository.save(livro);
    }
}
