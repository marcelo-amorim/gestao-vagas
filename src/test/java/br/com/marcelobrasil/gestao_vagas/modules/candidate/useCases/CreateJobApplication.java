package br.com.marcelobrasil.gestao_vagas.modules.candidate.useCases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import br.com.marcelobrasil.gestao_vagas.exceptions.JobNotFoundException;
import br.com.marcelobrasil.gestao_vagas.exceptions.UserNotFoundException;
import br.com.marcelobrasil.gestao_vagas.modules.candidate.entities.CandidateEntity;
import br.com.marcelobrasil.gestao_vagas.modules.candidate.entities.JobApplicationEntity;
import br.com.marcelobrasil.gestao_vagas.modules.candidate.repositories.CandidateRepository;
import br.com.marcelobrasil.gestao_vagas.modules.candidate.repositories.JobApplicationRepository;
import br.com.marcelobrasil.gestao_vagas.modules.company.entities.JobEntity;
import br.com.marcelobrasil.gestao_vagas.modules.company.repositories.JobRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CreateJobApplication {
    @InjectMocks private CreateJobApplicationUseCase createJobApplicationUseCase;

    @Mock private CandidateRepository candidateRepository;

    @Mock private JobRepository jobRepository;

    @Mock private JobApplicationRepository jobApplicationRepository;

    @Test
    @DisplayName("Shout not be able to apply job with candidate not found")
    public void should_not_be_able_apply_job_with_candidate_not_found() {
        try {
            createJobApplicationUseCase.execute(null, null);
        } catch (Exception e) {
            assertInstanceOf(UserNotFoundException.class, e);
        }
    }

    @Test
    @DisplayName("Should not be able to apply job with job not found")
    public void should_not_be_able_apply_job_with_job_not_found() {
        var candidateId = UUID.randomUUID();
        var candidate = new CandidateEntity();
        candidate.setId(candidateId);

        when(candidateRepository.findById(candidateId)).thenReturn(Optional.of(candidate));

        try {
            createJobApplicationUseCase.execute(candidateId, null);
        } catch (Exception e) {
            assertInstanceOf(JobNotFoundException.class, e);
        }
    }

    @Test
    public void should_be_able_to_create_a_new_job_application() {
        var candidateId = UUID.randomUUID();
        var jobId = UUID.randomUUID();

        var jobApplication =
                JobApplicationEntity.builder().candidateId(candidateId).jobId(jobId).build();

        var createdJobApplication = JobApplicationEntity.builder().id(UUID.randomUUID()).build();

        when(candidateRepository.findById(candidateId))
                .thenReturn(Optional.of(new CandidateEntity()));
        when(jobRepository.findById(jobId)).thenReturn(Optional.of(new JobEntity()));

        when(jobApplicationRepository.save(jobApplication)).thenReturn(createdJobApplication);

        var result = createJobApplicationUseCase.execute(candidateId, jobId);

        assertThat(result).hasFieldOrProperty("id");
        assertNotNull(result.getId());
    }
}
