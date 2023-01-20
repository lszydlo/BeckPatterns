package eu.skillcraft.beckpatterns.preparation;

import eu.skillcraft.beckpatterns.preparation.PreparationService.ContractNumber;
import java.time.Clock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PreparationServiceTest {


  private NumberFactory.AuthPort authPort = Mockito.mock(NumberFactory.AuthPort.class);
  private NumberFactory.SequencePort sequencePort;
  private NumberFactory.ConfigPort configPort;
  private NumberFactory.CustomerPort prefixPort;
  private Clock clock;

  @Test
  void should_create_number() {

    Mockito.when(authPort.isAuditor()).thenReturn(false);

    // Given
    NumberFactory generator = new NumberFactory(authPort, sequencePort, configPort, prefixPort, clock);

    // When
    ContractNumber next = generator.create(ContractType.Acquisition);

    // Then
    //Assertions.assertThat(next).isEqualTo(new ContractNumber("sds"));

  }
}
