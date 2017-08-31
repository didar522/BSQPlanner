package planning;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.uma.jmetal.solution.DoubleSolution;

import dataTemplates.DataIssueTemplate;
import optimizer.NSGAIIMultiRunner;
import optimizer.problemDefinition;

public class replanning {
	
	public ArrayList<DataIssueTemplate> allIssueData; 
	public ArrayList<DataIssueTemplate> listEarlyOpen; 
	public ArrayList<DataIssueTemplate> planningIssues=new ArrayList<DataIssueTemplate> (); 
	
	List<DoubleSolution> transfernonDominatedSolutions;
	
	public double spenteffort=0.0; 
	int bugRatio=0;
	int ftrRatio=0;
	int impRatio=0; 
	
	Date lastPlanning = null; 
	Date newReplan = null; 
	
	public replanning (ArrayList<DataIssueTemplate> allIssueData, ArrayList<DataIssueTemplate> listEarlyOpen, Date lastPlanning, Date newReplan,  int bugRatio, int ftrRatio, int impRatio){
		this.listEarlyOpen = listEarlyOpen; 
		this.allIssueData = allIssueData; 
		this.lastPlanning = lastPlanning; 
		this.newReplan = newReplan; 
		this.bugRatio = bugRatio; 
	    this.ftrRatio = ftrRatio; 
	    this.impRatio = impRatio;
	}
	
	public void preparePlanningIssues (){
		for (DataIssueTemplate iterator: listEarlyOpen){
			if (iterator.isOffered()==true){
				planningIssues.add(iterator); 
			}
		}
	}
	
	public void replanCapacity (){
		for (DataIssueTemplate iterator: allIssueData){
			if (iterator.getDateResolved()!=null){
				if(iterator.getDateResolved().after(lastPlanning) && iterator.getDateResolved().before(newReplan)){
					Date tmpDiffDate =null; 
					if (iterator.getDateUpdated().before(lastPlanning)){
						tmpDiffDate = lastPlanning; 
					}
					else {
						tmpDiffDate=iterator.getDateUpdated();
					}
					
					spenteffort += iterator.getTimespent(tmpDiffDate); 
				}
			}
    }
	}
	
	public void performReplanning (){
		problemDefinition obj_problemDefinition = new problemDefinition (planningIssues, spenteffort, bugRatio, ftrRatio, impRatio); 
		NSGAIIMultiRunner obj_NSGAIIMultiRunner = new NSGAIIMultiRunner (obj_problemDefinition); 
		
		try{
			transfernonDominatedSolutions=obj_NSGAIIMultiRunner.NSGARunner();
			
			System.out.println("replanning ----------------------");
			
			for (int i=0;i<transfernonDominatedSolutions.size();i++){
//			 System.out.println(transfernonDominatedSolutions.get(i));
			 for (int j=0;j<transfernonDominatedSolutions.get(i).getNumberOfVariables();j++){
				 System.out.print(transfernonDominatedSolutions.get(i).getVariableValueString(j)+"--");
			 }
			 System.out.println(); 
		 }
		}catch (Exception ex){
			
		}
	}
	
	public void flashIssueList (){
		for (int i=0;i<listEarlyOpen.size();i++){
			listEarlyOpen.get(i).setOffered(false); 
		}
	}
	
	
	public void modifyingIssueList (){
		Random rand = new Random();
		int solutionChoice = rand.nextInt(transfernonDominatedSolutions.size());
		
		System.out.println(solutionChoice + "===================");
		
		for (int i=0;i<planningIssues.size();i++){
			if (transfernonDominatedSolutions.get(solutionChoice).getVariableValueString(i).matches("1.0")){
				
				for (int j=0;j<listEarlyOpen.size();j++){
					if (planningIssues.get(i).getStrKey().matches(listEarlyOpen.get(j).getStrKey())){
						listEarlyOpen.get(j).setOffered(true);
					}
				}
			}
		}
		
		for (int i=0;i<planningIssues.size();i++){
			System.out.print(planningIssues.get(i).isOffered()+"==");
		}
		System.out.println();
		for (int i=0;i<listEarlyOpen.size();i++){
			System.out.print(listEarlyOpen.get(i).isOffered()+"***"); 
		}
	}
	
	public ArrayList<DataIssueTemplate>  addNewlyOpenedIssues (){
		for (DataIssueTemplate iterator: allIssueData){
			if (iterator.getDateCreated().after(lastPlanning) && iterator.getDateCreated().before(newReplan)){
				listEarlyOpen.add(iterator);
			}
		}
		
		return listEarlyOpen; 
	}
	
	
	
	
}
