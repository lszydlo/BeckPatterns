package eu.skillcraft.beckpatterns.gateway;

import eu.skillcraft.beckpatterns.preparation.ContractType;
import eu.skillcraft.beckpatterns.preparation.PreparationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContractController {

  private final PreparationService preparationService;


  @PostMapping
  void create(ContractType type) {
    preparationService.create(type);
  }

}
