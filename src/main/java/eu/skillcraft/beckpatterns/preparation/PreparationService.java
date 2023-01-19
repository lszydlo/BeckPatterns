package eu.skillcraft.beckpatterns.preparation;

import java.time.Clock;
import java.time.YearMonth;
import java.util.function.Function;
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
          authPort.isAuditor(), configPort.isDemo(), custommerPort.getType());
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
    GOLD -> typ prefix seq date demo
     */
    public ContractNumber(ContractType type, YearMonth now, Integer next, String prefix,
        boolean auditor, boolean demo, CustomerType customerType) {

      NumberBuilder builder = new NumberBuilder(now,next);

      number = switch (customerType) {
        case PREMIUM -> addAudit(auditor, addPrefix(prefix,addDemo(demo,getBasicNumb(now,next))));
        case STANDARD -> builder.addDemo(demo).build();
        case VIP -> builder.addPrefix(prefix).addDemo(demo).build();
        case GOLD -> addType(type,addPrefix(prefix,addDemo(demo,getBasicNumb(now,next))));
      };

    }

    private ContractNumber(String number) {
      this.number = number;
    }





    class NumberBuilder {


      public NumberBuilder(YearMonth now, Integer next) {}



    }



    private String addType(ContractType type, String number) {
      return type + " " + number;
    }

    private String addDemo(boolean demo, String number) {
      return demo ? number + "/DEMO" : number;
    }

    private String addAudit(boolean auditor, String number) {
      return auditor ? number + "/AUDIT" : number;
    }

    private String addPrefix(String prefix1, String number) {
      return prefix1 != null && !prefix1.isBlank() ? prefix1 + " " + number : number;
    }

    private String getBasicNumb(YearMonth now, Integer next) {
      return  next + " " + now.getYear() + "/" + now.getMonthValue();
    }

  }
}
