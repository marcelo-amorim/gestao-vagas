package br.com.marcelobrasil.gestao_vagas.modules.candidate.repositories;

import br.com.marcelobrasil.gestao_vagas.modules.candidate.entities.JobApplicationEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobApplicationRepository extends JpaRepository<JobApplicationEntity, UUID> {}
