package eu.skillcraft.beckpatterns.gateway;

import eu.skillcraft.beckpatterns.preparation.ContractDetailsRM;
import eu.skillcraft.beckpatterns.preparation.ContractType;
import eu.skillcraft.beckpatterns.preparation.GetContractDetails;
import eu.skillcraft.beckpatterns.preparation.PreparationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ContractController {

  private final PreparationService preparationService;


  @PostMapping("/contracts")
  void handlePost(ContractType type) {
    preparationService.create(type);
  }

  @GetMapping("/contracts/{id}")
  ContractDetailsRM handleGet( @PathVariable UUID id) {
    return preparationService.execute(new GetContractDetails(id));
  }

  @PutMapping("/contracts/{id}")
  void handlePut(ContractType type, @PathVariable UUID id) {
    preparationService.create(type, id);
  }


  /**
   *
   * @param query
   */
  @PostMapping
  ResponseEntity<JSONReadModel> handlePost(GetJsonForXml query) {
    JSONReadModel readModel = preparationService.execute(query);
    return ResponseEntity.ok(readModel);
  }

}
