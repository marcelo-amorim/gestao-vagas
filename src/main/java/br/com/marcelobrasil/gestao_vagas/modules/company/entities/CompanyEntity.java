package br.com.marcelobrasil.gestao_vagas.modules.company.entities;

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

import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Entity(name = "company")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Pattern(
            regexp = "^(?!\\s*$).+",
            message = "This field must not be empty or contain only whitespace.")
    private String username;

    @Email(message = "Invalid email address.")
    private String email;

    @Length(min = 10, max = 100, message = "Password must be between 10 and 100 characters.")
    private String password;

    private String website;
    private String name;
    private String description;
}
