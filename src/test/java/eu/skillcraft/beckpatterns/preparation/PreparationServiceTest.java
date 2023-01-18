package eu.skillcraft.beckpatterns.preparation;

import eu.skillcraft.beckpatterns.preparation.PreparationService.ContractNumber;
import eu.skillcraft.beckpatterns.preparation.PreparationService.NumberFactory;
import java.time.Clock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PreparationServiceTest {


  private AuthPort authPort = Mockito.mock(AuthPort.class);
  private SequencePort sequencePort;
  private ConfigPort configPort;
  private CustomerPort prefixPort;
  private Clock clock;

  @Test
  void should_create_number() {

    Mockito.when(authPort.isAuditor()).thenReturn(false);

    // Given
    NumberFactory generator = new NumberFactory(authPort, sequencePort, configPort, prefixPort, clock);

    // When
    ContractNumber next = generator.create(ContractType.Acquisition);

    // Then
    Assertions.assertThat(next).isEqualTo(new ContractNumber());

  }
}
