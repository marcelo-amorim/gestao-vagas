package br.com.marcelobrasil.gestao_vagas.modules.candidate.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "candidate")
public class CandidateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Pattern(
            regexp = "^(?!\\s*$).+",
            message = "O campo não deve ser vazio ou conter espaços em branco")
    private String username;

    @Email(message = "Campo de e-mail inválido")
    private String email;

    @Length(min = 10, max = 100, message = "A senha deve ter entre 10 e 100 caracteres")
    private String password;

    private String description;
    private String curriculum;

    @CreationTimestamp private LocalDateTime createdAt;
}
