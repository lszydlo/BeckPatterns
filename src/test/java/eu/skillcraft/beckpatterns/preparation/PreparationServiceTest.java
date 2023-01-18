package eu.skillcraft.beckpatterns.preparation;

import eu.skillcraft.beckpatterns.preparation.PreparationService.NumberGenerator;
import java.time.Clock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PreparationServiceTest {


  private AuthPort authPort = Mockito.mock(AuthPort.class);
  private SequencePort sequencePort;
  private ConfigPort configPort;
  private PrefixPort prefixPort;
  private Clock clock;

  @Test
  void should_create_number() {

    Mockito.when(authPort.isAuditor()).thenReturn(false);

    // Given
    NumberGenerator generator = new NumberGenerator(authPort, sequencePort, configPort, prefixPort, clock);

    // When
    String next = generator.next(ContractType.Acquisition);

    // Then
    Assertions.assertThat(next).isEqualTo("");

  }
}
