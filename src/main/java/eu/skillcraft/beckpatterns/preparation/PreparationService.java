package eu.skillcraft.beckpatterns.preparation;

import java.time.Clock;
import java.time.YearMonth;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public class PreparationService {


  private final NumberFactory numberFactory;
  private final ContractRepo repo;

  public void create(ContractType type) {
    ContractNumber number = numberFactory.create(type);

    Contract contract = new Contract(type, number);

    repo.save(contract);
  }

  @RequiredArgsConstructor
  static class NumberFactory {

    private final AuthPort authPort;
    private final SequencePort sequencePort;
    private final ConfigPort configPort;
    private final CustomerPort customerPort;
    private final Clock clock;

    public ContractNumber create(ContractType type) {

      return new ContractNumber(type, YearMonth.now(clock), sequencePort.next(), customerPort.getPrefix(),
          authPort.isAuditor(), configPort.isDemo(), customerPort.getType());
    }
  }


  static class Contract {

    private ContractType type;
    private String number;

    public Contract(@NonNull ContractType type, @NonNull ContractNumber number) {
      this.type = type;
      this.number = number.toString();
    }

    @Override
    public String toString() {
      return "Contract{" +
          "type=" + type +
          ", number='" + number + '\'' +
          '}';
    }
  }

  static class ContractNumber {

    private final String number;

    static ContractNumber parse(String value) {
      return new ContractNumber(value);
    }

    String getNumber() {
      return number;
    }

    private ContractNumber(String value) {
      this.number = value;
    }

    public ContractNumber(ContractType type, YearMonth now, Integer next, String prefix,
        boolean auditor, boolean demo, CustomerType customerType) {

      NumberBuilder builder = new NumberBuilder(now, next);

      number = switch (customerType) {
        case PREMIUM -> builder.addPrefix(prefix).addDemo(demo).addAudit(auditor).build();
        case STANDARD -> builder.addDemo(demo).build();
        case VIP -> builder.addPrefix(prefix).addDemo(demo).build();
        case GOLD -> builder.addPrefix(prefix).addDemo(demo).addAudit(auditor).addType(type).build();
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
