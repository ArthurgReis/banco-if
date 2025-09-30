package br.com.bancofeira.banco_feira.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.bancofeira.banco_feira.model.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    



}
