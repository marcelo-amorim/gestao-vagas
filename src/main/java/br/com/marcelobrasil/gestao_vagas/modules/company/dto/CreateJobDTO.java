package br.com.marcelobrasil.gestao_vagas.modules.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

import lombok.Data;

@Data
public class CreateJobDTO {
    @Schema(example = "Engenheiro de Software", requiredMode = RequiredMode.REQUIRED)
    private String description;

    @Schema(
            example = "GYMPass, vale-refeição, vale-transporte",
            requiredMode = RequiredMode.REQUIRED)
    private String benefits;

    @Schema(example = "Sênior", requiredMode = RequiredMode.REQUIRED)
    private String level;
}
