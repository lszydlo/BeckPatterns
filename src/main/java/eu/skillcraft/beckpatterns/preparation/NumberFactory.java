package eu.skillcraft.beckpatterns.preparation;

import eu.skillcraft.beckpatterns.preparation.PreparationService.ContractNumber;
import java.time.Clock;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
// @Factory
class NumberFactory {

  private final AuthPort authPort;
  private final SequencePort sequencePort;
  private final ConfigPort configPort;
  private final CustomerPort customerPort;
  private final Clock clock;

  ContractNumber create(ContractType type) {
    return new ContractNumber(type, YearMonth.now(clock), sequencePort.next(),
        customerPort.getPrefix(), authPort.isAuditor(),
        configPort.isDemo(), customerPort.getType());
  }

  public interface AuthPort {
    boolean isAuditor();
  }

  public interface ConfigPort {
    boolean isDemo();
  }

  public interface CustomerPort {
    String getPrefix();
    CustomerType getType();
  }

  public interface SequencePort {
    Integer next();
  }
}
