package core.elevator;

import core.API.Elevator;
import core.API.Passenger;
import core.Constants;
import core.ElevatorState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;

@RunWith(MockitoJUnitRunner.class)
public class ElevatorExpensesAnalyserBeanTest {

    @Mock
    private MovementExpensesAnalyser movementExpensesAnalyser;

    @Mock
    private FillingExpensesAnalyser fillingExpensesAnalyser;

    @Mock
    private Elevator elevator;

    @InjectMocks
    private ElevatorExpensesAnalyserBean instance;

    @Test
    public void emptyElevator() throws Exception {
        // GIVEN
        final int floorsToPass = 4;
        final int startFromFloor = 1;
        mockElevator(
                1,
                startFromFloor,
                startFromFloor,
                1.0d,
                ElevatorState.OPENING,
                0,
                new ArrayList<>());
        given(movementExpensesAnalyser
                .calculateMovementTime(eq(1.0d),
                        eq(startFromFloor + floorsToPass),
                        anyVararg()))
                .willReturn(floorsToPass * Constants.TIME_TO_FLOOR);
        given(fillingExpensesAnalyser.calculateFillingExpenses(eq(1), anyVararg())).willReturn(0);
        // WHEN
        int result = instance.calculateExpenses(elevator, 1, 5);

        // THEN
        int expectedTime = ElevatorState.OPENING.getRequiresAtLeastTicks()
                + ElevatorState.FILLING.getRequiresAtLeastTicks()
                + ElevatorState.CLOSING.getRequiresAtLeastTicks()
                + ElevatorState.WAITING.getRequiresAtLeastTicks()
                + Constants.TIME_TO_FLOOR * 4;
        assertThat(result).isEqualTo(expectedTime);
    }

    private void mockElevator(
            int elevatorId,
            int floor,
            int nextFloor,
            double y,
            ElevatorState state,
            int timeOnFloor,
            List<Passenger> passengers
    ) {
        given(elevator.getPassengers()).willReturn(passengers);
        given(elevator.getId()).willReturn(elevatorId);
        given(elevator.getTimeOnFloor()).willReturn(timeOnFloor);
        given(elevator.getState()).willReturn(state.getInt());
        given(elevator.getNextFloor()).willReturn(nextFloor);
        given(elevator.getY()).willReturn(y);
        given(elevator.getFloor()).willReturn(floor);
    }

}