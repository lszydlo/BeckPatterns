package eu.skillcraft.beckpatterns.preparation;

import eu.skillcraft.beckpatterns.gateway.DoCreateContract;
import eu.skillcraft.beckpatterns.gateway.GetJsonForXml;
import eu.skillcraft.beckpatterns.gateway.JSONReadModel;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
// @ApplicationService
public class PreparationService {

  private final NumberFactory numberFactory;
  private final ContractRepo repo;


  // @CommandHandler
  public void create(ContractType type, UUID id) {
    ContractNumber number = numberFactory.create(type);

    Contract contract = new Contract(type, number);

    repo.save(contract);
  }

  public void create(ContractType type) {
    create(type, UUID.randomUUID());
  }

  public ContractDetailsRM execute(GetContractDetails query) {
    return new ContractDetailsRM();
  }

  public List<ContractDetailsRM> execute(GetContractList query) {
    return List.of(new ContractDetailsRM());
  }

  public void listenOn(ContractWasRead event) {
      Contract contract = repo.load(event.contractId());
      contract.markAsRead();
      repo.save(contract);
  }

  public JSONReadModel execute(GetJsonForXml query) {

    return null;
  }

  @ToString
  // @DomainEntity
  static class Contract {

    private UUID id;
    private ContractType type;
    private String number;
    private LocalDate endDate;
    private boolean wasRead;

    public Contract(@NonNull ContractType type, @NonNull ContractNumber number) {
      this.type = type;
      this.number = number.getNumber();

    }

    public void markAsRead() {
      this.wasRead = true;
    }
  }

  // @ValueObject
  static class ContractNumber {

    private final String number;

    String getNumber() {
      return number;
    }

    ContractNumber(ContractType type, YearMonth now, Integer next, String prefix,
        boolean isAuditor, boolean idDemo, CustomerType customerType) {

      NumberBuilder builder = new NumberBuilder(now, next);

      this.number = switch (customerType) {
        case PREMIUM -> builder.addPrefix(prefix).addDemo(idDemo).addAudit(isAuditor).build();
        case STANDARD -> builder.addDemo(idDemo).build();
        case VIP -> builder.addPrefix(prefix).addDemo(idDemo).build();
        case GOLD -> builder.addPrefix(prefix).addDemo(idDemo).addAudit(isAuditor).addType(type)
            .build();
      };
    }

    private static class NumberBuilder {

      private String number;

      public NumberBuilder(YearMonth now, Integer next) {
        this.number = next + " " + now.getYear() + "/" + now.getMonthValue();
      }

      NumberBuilder addDemo(boolean isDemo) {
        this.number = isDemo ? "DEMO/" + number : number;
        return this;
      }

      NumberBuilder addPrefix(String prefix) {
        this.number = prefix + " " + number;
        return this;
      }

      NumberBuilder addType(ContractType type) {
        this.number = type + " " + number;
        return this;
      }

      NumberBuilder addAudit(boolean isAudit) {
        this.number = isAudit ? number + "/AUDIT" : number;
        return this;
      }

      String build() {
        return number;
      }
    }
  }
}
