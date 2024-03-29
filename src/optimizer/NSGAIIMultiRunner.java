package optimizer;

//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU Lesser General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.zdt.*;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.*;
import org.uma.jmetal.util.experiment.util.ExperimentAlgorithm;
import org.uma.jmetal.util.experiment.util.ExperimentProblem;
import optimizer.problemDefinition; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* Example of experimental study based on solving the ZDT problems with four versions of NSGA-II, each
* of them applying a different crossover probability (from 0.7 to 1.0).
*
* This experiment assumes that the reference Pareto front are not known, so the names of files containing
* them and the directory where they are located must be specified.
*
* Six quality indicators are used for performance assessment.
*
* The steps to carry out the experiment are:
* 1. Configure the experiment
* 2. Execute the algorithms
* 3. Generate the reference Pareto fronts
* 4. Compute the quality indicators
* 5. Generate Latex tables reporting means and medians
* 6. Generate Latex tables with the result of applying the Wilcoxon Rank Sum Test
* 7. Generate Latex tables with the ranking obtained by applying the Friedman test
* 8. Generate R scripts to obtain boxplots
*
* @author Antonio J. Nebro <antonio@lcc.uma.es>
*/
public class NSGAIIMultiRunner {
private static final int INDEPENDENT_RUNS = 1 ;

List<DoubleSolution> transfernonDominatedSolutions; 

problemDefinition obj_problemDefinition; 
GenerateReferenceParetoSetAndFrontFromDoubleSolutions_Didar obj_GenerateReferenceParetoSetAndFrontFromDoubleSolutions_Didar; 

public NSGAIIMultiRunner (problemDefinition tmp_problemDefinition){
	this.obj_problemDefinition = tmp_problemDefinition; 
}



public List<DoubleSolution> NSGARunner()throws IOException {
//if (args.length != 1) {
//  throw new JMetalException("Needed arguments: experimentBaseDirectory") ;
//}
//String experimentBaseDirectory = "C:/Users/Didar/OneDrive/Work Cloud/Eclipse workbench 2017/Workbench/JMetalTest/" ; 
String experimentBaseDirectory = "C:/Users/Didar/Desktop/N4/";

List<ExperimentProblem<IntegerSolution>> problemList = new ArrayList<>();
problemList.add(new ExperimentProblem<>(obj_problemDefinition));
//problemList.add(new ExperimentProblem<>(new ZDT2()));
//problemList.add(new ExperimentProblem<>(new ZDT3()));
//problemList.add(new ExperimentProblem<>(new ZDT4()));
//problemList.add(new ExperimentProblem<>(new ZDT6()));

List<ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>> algorithmList =
        configureAlgorithmList(problemList);

Experiment<IntegerSolution, List<IntegerSolution>> experiment =
    new ExperimentBuilder<IntegerSolution, List<IntegerSolution>>("NSGAIIStudy2")
        .setAlgorithmList(algorithmList)
        .setProblemList(problemList)
        .setExperimentBaseDirectory(experimentBaseDirectory)
        .setOutputParetoFrontFileName("FUN")
        .setOutputParetoSetFileName("VAR")
        .setReferenceFrontDirectory(experimentBaseDirectory+"/referenceFronts")
        .setIndicatorList(Arrays.asList(
            new Epsilon<IntegerSolution>(), new Spread<IntegerSolution>(), new GenerationalDistance<IntegerSolution>(),
            new PISAHypervolume<IntegerSolution>(),
            new InvertedGenerationalDistance<IntegerSolution>(), new InvertedGenerationalDistancePlus<IntegerSolution>()))
        .setIndependentRuns(INDEPENDENT_RUNS)
        .setNumberOfCores(8)
        .build();

new ExecuteAlgorithms<>(experiment).run();
obj_GenerateReferenceParetoSetAndFrontFromDoubleSolutions_Didar = new GenerateReferenceParetoSetAndFrontFromDoubleSolutions_Didar(experiment);
obj_GenerateReferenceParetoSetAndFrontFromDoubleSolutions_Didar.run(); 
//new ComputeQualityIndicators<>(experiment).run() ;
//new GenerateLatexTablesWithStatistics(experiment).run() ;
//new GenerateWilcoxonTestTablesWithR<>(experiment).run() ;
//new GenerateFriedmanTestTables<>(experiment).run();
//new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).run() ;

transfernonDominatedSolutions = obj_GenerateReferenceParetoSetAndFrontFromDoubleSolutions_Didar.getTransferNonDominatedSolutions(); 

return transfernonDominatedSolutions; 

}

/**
* The algorithm list is composed of pairs {@link Algorithm} + {@link Problem} which form part of
* a {@link ExperimentAlgorithm}, which is a decorator for class {@link Algorithm}. The {@link
* ExperimentAlgorithm} has an optional tag component, that can be set as it is shown in this example,
* where four variants of a same algorithm are defined.
*/
static List<ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>> configureAlgorithmList(
      List<ExperimentProblem<IntegerSolution>> problemList) {
List<ExperimentAlgorithm<IntegerSolution, List<IntegerSolution>>> algorithms = new ArrayList<>();

for (int i = 0; i < problemList.size(); i++) {
  Algorithm<List<IntegerSolution>> algorithm = new NSGAIIBuilder<>(
          problemList.get(i).getProblem(),
          new IntegerSBXCrossover(1.0, 5),
          new IntegerPolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 10.0))
          .setMaxEvaluations(25000)
          .setPopulationSize(100)
          .build();
  algorithms.add(new ExperimentAlgorithm<>(algorithm, "NSGAIIa", problemList.get(i).getTag()));
}

for (int i = 0; i < problemList.size(); i++) {
  Algorithm<List<IntegerSolution>> algorithm = new NSGAIIBuilder<>(
          problemList.get(i).getProblem(),
          new IntegerSBXCrossover(1.0, 20.0),
          new IntegerPolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 20.0))
          .setMaxEvaluations(25000)
          .setPopulationSize(100)
          .build();
  algorithms.add(new ExperimentAlgorithm<>(algorithm, "NSGAIIb", problemList.get(i).getTag()));
}

for (int i = 0; i < problemList.size(); i++) {
  Algorithm<List<IntegerSolution>> algorithm = new NSGAIIBuilder<>(problemList.get(i).getProblem(), new IntegerSBXCrossover(1.0, 40.0),
          new IntegerPolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 40.0))
          .setMaxEvaluations(25000)
          .setPopulationSize(100)
          .build();
  algorithms.add(new ExperimentAlgorithm<>(algorithm, "NSGAIIc", problemList.get(i).getTag()));
}

for (int i = 0; i < problemList.size(); i++) {
  Algorithm<List<IntegerSolution>> algorithm = new NSGAIIBuilder<>(problemList.get(i).getProblem(), new IntegerSBXCrossover(1.0, 80.0),
          new IntegerPolynomialMutation(1.0 / problemList.get(i).getProblem().getNumberOfVariables(), 80.0))
          .setMaxEvaluations(25000)
          .setPopulationSize(100)
          .build();
  algorithms.add(new ExperimentAlgorithm<>(algorithm, "NSGAIId", problemList.get(i).getTag()));
}

return algorithms;
}

}