package br.com.marcelobrasil.gestao_vagas.modules.candidate.useCases;

import br.com.marcelobrasil.gestao_vagas.exceptions.UserNotFoundException;
import br.com.marcelobrasil.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.marcelobrasil.gestao_vagas.modules.candidate.repositories.CandidateRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfileCandidateUseCase {
    @Autowired private CandidateRepository candidateRepository;

    public ProfileCandidateResponseDTO execute(UUID candidateId) throws UsernameNotFoundException {
        var candidate =
                this.candidateRepository
                        .findById(candidateId)
                        .orElseThrow(
                                () -> {
                                    throw new UserNotFoundException();
                                });

        var candidateDTO =
                ProfileCandidateResponseDTO.builder()
                        .id(candidate.getId())
                        .name(candidate.getName())
                        .description(candidate.getDescription())
                        .email(candidate.getEmail())
                        .username(candidate.getUsername())
                        .build();

        return candidateDTO;
    }
}
