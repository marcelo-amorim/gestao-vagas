package br.com.marcelobrasil.gestao_vagas.modules.candidate.useCases;

import br.com.marcelobrasil.gestao_vagas.exceptions.JobNotFoundException;
import br.com.marcelobrasil.gestao_vagas.exceptions.UserNotFoundException;
import br.com.marcelobrasil.gestao_vagas.modules.candidate.entities.JobApplicationEntity;
import br.com.marcelobrasil.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.marcelobrasil.gestao_vagas.modules.candidate.repositories.JobApplicationRepository;
import br.com.marcelobrasil.gestao_vagas.modules.company.repositories.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateJobApplicationUseCase {

    @Autowired private CandidateRepository candidateRepository;

    @Autowired private JobRepository jobRepository;

    @Autowired private JobApplicationRepository jobApplicationRepository;

    public JobApplicationEntity execute(UUID candidateId, UUID jobId) {
        this.candidateRepository
                .findById(candidateId)
                .orElseThrow(
                        () -> {
                            throw new UserNotFoundException();
                        });

        this.jobRepository
                .findById(jobId)
                .orElseThrow(
                        () -> {
                            throw new JobNotFoundException();
                        });

        var jobApplication =
                JobApplicationEntity.builder().candidateId(candidateId).jobId(jobId).build();

        return this.jobApplicationRepository.save(jobApplication);
    }
}
