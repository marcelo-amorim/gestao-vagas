package br.com.marcelobrasil.gestao_vagas.modules.candidate.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "candidate")
public class CandidateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Schema(
            example = "João da Silva",
            requiredMode = RequiredMode.REQUIRED,
            description = "Nome do candidato")
    private String name;

    @Schema(
            example = "joao.silva",
            requiredMode = RequiredMode.REQUIRED,
            description = "Usuário de acesso do candidato")
    @Pattern(
            regexp = "^(?!\\s*$).+",
            message = "This field must not be empty or contain only whitespace.")
    private String username;

    @Schema(
            example = "joao.silva@email.com",
            requiredMode = RequiredMode.REQUIRED,
            description = "E-mail do candidato")
    @Email(message = "Invalid email address.")
    private String email;

    @Schema(
            example = "Password@1234",
            minLength = 10,
            maxLength = 100,
            requiredMode = RequiredMode.REQUIRED,
            description = "Senha de acesso do candidato")
    @Length(min = 10, max = 100, message = "Password must be between 10 and 100 characters.")
    private String password;

    @Schema(example = "Engenheiro de Software", description = "Breve descrição do candidato")
    private String description;

    private String curriculum;

    @CreationTimestamp private LocalDateTime createdAt;
}
