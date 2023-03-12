package projekt.runner;

import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.archetype.ProblemGroup;
import projekt.delivery.generator.OrderGenerator;
import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.service.DeliveryService;
import projekt.delivery.simulation.BasicDeliverySimulation;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;
import projekt.runner.handler.ResultHandler;
import projekt.runner.handler.SimulationFinishedHandler;
import projekt.runner.handler.SimulationSetupHandler;

import java.util.HashMap;
import java.util.Map;

import static org.tudalgo.algoutils.student.Student.crash;

public class RunnerImpl implements Runner {

    @Override
    public void run(ProblemGroup problemGroup,
                    SimulationConfig simulationConfig,
                    int simulationRuns,
                    DeliveryService.Factory deliveryServiceFactory,
                    SimulationSetupHandler simulationSetupHandler,
                    SimulationFinishedHandler simulationFinishedHandler,
                    ResultHandler resultHandler) {

        Map<ProblemArchetype, Simulation> simulations = createSimulations(problemGroup, simulationConfig, deliveryServiceFactory);
        Map<RatingCriteria, Double> averageRatings = new HashMap<>();
        for (ProblemArchetype problem : problemGroup.problems()) {
            Simulation simulation = simulations.get(problem);
            for (int i = 0; i < simulationRuns; i++) {
                simulationSetupHandler.accept(simulation, problem, i);
                simulation.runSimulation(simulationConfig.getMillisecondsPerTick());
                if (simulationFinishedHandler.accept(simulation, problem)) {
                    break;
                }
            }
        }
             // TODO: H10.2 - remove if implemented
    }

    @Override
    public Map<ProblemArchetype, Simulation> createSimulations(ProblemGroup problemGroup,
                                                                SimulationConfig simulationConfig,
                                                                DeliveryService.Factory deliveryServiceFactory) {

        Map<ProblemArchetype, Simulation> simulations = new HashMap<>();
        for (ProblemArchetype problem : problemGroup.problems()) {
            OrderGenerator.Factory orderGeneratorFactory = problem.orderGeneratorFactory();
            VehicleManager vehicleManager = problem.vehicleManager();
            Map<RatingCriteria, Rater.Factory> raters = problem.raterFactoryMap();
            long simulationLength = problem.simulationLength();
            String problemName = problem.name();
            DeliveryService deliveryService = deliveryServiceFactory.create(vehicleManager);
            BasicDeliverySimulation simulation = new BasicDeliverySimulation(simulationConfig, raters,deliveryService, orderGeneratorFactory);
            simulations.put(problem, simulation);
        }
        return simulations; // TODO: H10.1 - remove if implemented
    }

}
