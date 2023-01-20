package eu.skillcraft.beckpatterns.preparation;

import java.time.YearMonth;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
// @ApplicationService
public class PreparationService {

  private final NumberFactory numberFactory;
  private final ContractRepo repo;

  // @CommandHandler
  public void create(ContractType type) {
    ContractNumber number = numberFactory.create(type);

    Contract contract = new Contract(type, number);

    repo.save(contract);
  }

  @ToString
  // @DomainEntity
  static class Contract {

    private ContractType type;
    private String number;

    public Contract(@NonNull ContractType type, @NonNull ContractNumber number) {
      this.type = type;
      this.number = number.getNumber();
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
