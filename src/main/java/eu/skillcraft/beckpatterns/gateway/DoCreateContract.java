package eu.skillcraft.beckpatterns.gateway;

import eu.skillcraft.beckpatterns.preparation.ContractType;
import java.time.LocalDate;
import java.util.UUID;

public record DoCreateContract(ContractType type, String title, LocalDate endDate, UUID id) {

}
