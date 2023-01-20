package eu.skillcraft.beckpatterns.preparation;

import eu.skillcraft.beckpatterns.preparation.PreparationService.Contract;

interface ContractRepo {

  void save(Contract contract);
}
