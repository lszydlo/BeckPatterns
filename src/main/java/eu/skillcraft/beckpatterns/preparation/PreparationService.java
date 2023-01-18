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

    public String next(ContractType type) {

      if(type == null) {
        throw new IllegalArgumentException();
      }

      YearMonth yearMonth = YearMonth.now(clock);

      String number = type + " " + sequencePort + " " + yearMonth.getYear() + "/" + yearMonth.getMonthValue();

      String prefix = prefixPort.getPrefix();

      if(prefix != null && !prefix.isBlank()) {
        number = prefix + " " + number;
      }

      if(authPort.isAuditor()) {
        number =  number + "/AUDIT";
      }

      if(configPort.isDemo()) {
        number =  "DEMO/" + number;
      }

      return number;
    }
  }

  static class Contract {

    private final ContractType type;
    private final String number;

    public Contract(ContractType type, String number) {

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
}
