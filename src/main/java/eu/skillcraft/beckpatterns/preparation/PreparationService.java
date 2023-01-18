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
    private final CustomerPort custommerPort;
    private final Clock clock;

    public ContractNumber create(ContractType type) {

      return new ContractNumber(type, YearMonth.now(clock), sequencePort.next(), custommerPort.getPrefix(),
          authPort.isAuditor(), configPort.isDemo());
    }
  }

  static class Contract {

    private final ContractType type;
    private final ContractNumber number;

    public Contract(@NonNull ContractType type, @NonNull ContractNumber number) {
      this.type = type;
      this.number = number;
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
    /*
    STANDARD -> seq date demo
    VIP -> prefix seq date demo
    PREMIUM -> prefix seq date audit demo
    GOLD -> typ prefix seq date audit demo
     */
    public ContractNumber(ContractType type, YearMonth now, Integer next, String prefix,
        boolean auditor, boolean demo) {
      number = generateNumber(type, now,next,prefix, auditor, demo);
    }

    String generateNumber(ContractType type, YearMonth now, Integer next, String prefix1,
        boolean auditor, boolean demo) {
      if(type == null) {
        throw new IllegalArgumentException();
      }

      String number = type + " " + next + " " + now.getYear() + "/" + now.getMonthValue();

      if(prefix1 != null && !prefix1.isBlank()) {
        number = prefix1 + " " + number;
      }

      if(auditor) {
        number =  number + "/AUDIT";
      }

      if(demo) {
        number =  "DEMO/" + number;
      }

      return number;
    }

  }
}
