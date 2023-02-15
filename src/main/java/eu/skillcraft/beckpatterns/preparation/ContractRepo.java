package eu.skillcraft.beckpatterns.preparation;

import eu.skillcraft.beckpatterns.preparation.PreparationService.Contract;
import java.util.UUID;

interface ContractRepo {

  void save(Contract contract);

  Contract load(UUID contractId);
}
