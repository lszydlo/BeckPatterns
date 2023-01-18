package eu.skillcraft.beckpatterns.preparation;

import java.time.Clock;
import java.time.YearMonth;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public class PreparationService {


  private final NumberGenerator generator;
  private final ContractRepo repo;

  public void create(ContractType type) {
    String number = generator.next(type);

    Contract contract = new Contract(type, number);

    repo.save(contract);
  }

  @RequiredArgsConstructor
  static class NumberGenerator {

    private final AuthPort authPort;
    private final SequencePort sequencePort;
    private final ConfigPort configPort;
    private final PrefixPort prefixPort;
    private final Clock clock;

    NumAlgo numAlgo = new NumAlgo();

    public ContractNumber next(ContractType type) {

      return numAlgo.generateNumber(type, YearMonth.now(clock), sequencePort.next(), prefixPort.getPrefix(),
          authPort.isAuditor(), configPort.isDemo());
    }
  }

  static class Contract {

    private final ContractType type;
    private final ContractNumber number;

    public Contract(ContractType type, ContractNumber number) {
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

  static class NumAlgo {

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

  private static class ContractNumber {

  }
}
