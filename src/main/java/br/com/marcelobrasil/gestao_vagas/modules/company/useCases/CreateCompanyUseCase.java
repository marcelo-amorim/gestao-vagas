package br.com.marcelobrasil.gestao_vagas.modules.company.useCases;

import br.com.marcelobrasil.gestao_vagas.exceptions.UserAlreadyExistsException;
import br.com.marcelobrasil.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.marcelobrasil.gestao_vagas.modules.company.repositories.CompanyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateCompanyUseCase {

    @Autowired private CompanyRepository companyRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    public CompanyEntity execute(CompanyEntity companyEntity) {
        this.companyRepository
                .findByUsernameOrEmail(companyEntity.getUsername(), companyEntity.getEmail())
                .ifPresent(
                        (user) -> {
                            throw new UserAlreadyExistsException();
                        });

        var passowrd = passwordEncoder.encode(companyEntity.getPassword());
        companyEntity.setPassword(passowrd);

        return companyRepository.save(companyEntity);
    }
}
