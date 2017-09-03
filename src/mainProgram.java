//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Random;
//
//import javax.xml.crypto.Data;
//
//import org.uma.jmetal.solution.DoubleSolution;
//
//import dataTemplates.DataIssueTemplate;
//import dataPreprocess.DataReadExcelFiles;
//import dataTemplates.releaseInfoTemplate;
//import dataTemplates.resultTemplate;
//import optimizer.NSGAIIMultiRunner;
//import optimizer.problemDefinition;
//import planning.replanning;
//import dataTemplates.ReleaseInfoCollection;
//import dataTemplates.ReleaseCalendarTemplate;
//import dataPreprocess.Preprocessing;;
//
//public class mainProgram {
//
//	//-------------------------- Details related to input file location -----------------------------
//	
//		public String strFilePath = "C:/Users/Didar/Desktop/N4/";
////				System.getProperty("user.dir")+ "/src/resources/InputOutput/";
//		public String strFileName = "N4.xlsx"; 
//		public String strSheetName = strFileName; 
//		public int intStartingRowofData = 1;
//		
//		public int daysForReplan = 0; 
//		
//		//Key input string for analyzing in this program------------------------
//		public String searchString = ""; 
//		
//		public int bugRatio=0, ftrRatio=0, impRatio=0; 
//	
//		public resultTemplate obj_resultFormat = new resultTemplate(); 
//		
//		
//		public HashMap<String, Integer> excelFileIndex = new HashMap <String, Integer> ();  
//		public ArrayList<DataIssueTemplate> allIssueData = new ArrayList<DataIssueTemplate>();  
//		public ArrayList<DataIssueTemplate> offeredIssueData = new ArrayList<DataIssueTemplate>();
//		public ArrayList<DataIssueTemplate> backlogIssueData = new ArrayList<DataIssueTemplate>();
//		
//		
//		public List<DoubleSolution> transfernonDominatedSolutions;
//		public ArrayList<Date> list_replanning=new ArrayList <Date>(); 
//		
//		public Preprocessing obj_Filtering;  
//		public replanning obj_replanning; 
//		
//		public double dblAvailableCapacity=0, dblUsedCapacity=0; 
//		
//		public problemDefinition obj_problemDefinition; 
//		public NSGAIIMultiRunner obj_NSGAIIMultiRunner;
//		
//		public Date lastPLanning = null, newReplanning = null, releaseStart=null, releaseEnd = null;  
//		
//		public void ReadingExcelsheet (ArrayList<DataIssueTemplate> tmp_IssueData, 
//				int tmp_intStartingRowofData, 
//				String tmp_strFilePath, 
//				String tmp_strFileName, 
//				String tmp_strSheetName){
//			
//			DataReadExcelFiles objDataReadExcelFiles = new DataReadExcelFiles(tmp_IssueData, tmp_intStartingRowofData, tmp_strFilePath, tmp_strFileName, tmp_strSheetName); 
//			excelFileIndex = objDataReadExcelFiles.createColumnIndex(0); 
//			objDataReadExcelFiles.readExcelFiles(false);
//		}
//		
//		
//
//	public resultTemplate mainProgramRun (Date tmpreleaseStart, Date tmpreleaseEnd, int tmpdaysForReplan) throws Exception{
//		ReadingExcelsheet (allIssueData,intStartingRowofData,strFilePath,strFileName,strSheetName); 
//			
//		
//		releaseStart=tmpreleaseStart;
//		releaseEnd=tmpreleaseEnd;
//		daysForReplan = tmpdaysForReplan;
//		
//		//----------------1st turn ---------------
//		
//		list_replanning.add(releaseStart); 
//		obj_Filtering=new Preprocessing(allIssueData, releaseStart, releaseEnd);
//		obj_Filtering.filterIssuesEarlyOpen();
//		obj_Filtering.filterIssueInClose();
//		
//		backlogIssueData=obj_Filtering.listEarlyOpen; 
//		dblAvailableCapacity=obj_Filtering.totalCapacity; 
//		
//		System.out.println("1st turn Backlog issues -----------------------------------");
//		for (int iter=0;iter<backlogIssueData.size();iter++){
//			System.out.println(backlogIssueData.get(iter).getStrKey() + "--"+backlogIssueData.get(iter).getDateCreated()+"--"+backlogIssueData.get(iter).getDateResolved()+"--"+backlogIssueData.get(iter).isOffered());	
//		}
//		
//		
//		bugRatio=(int) (obj_Filtering.bugTimeSpent*100/(obj_Filtering.ftrTimeSpent+obj_Filtering.impTimeSpent+obj_Filtering.bugTimeSpent));
//	    ftrRatio=(int) (obj_Filtering.ftrTimeSpent*100/(obj_Filtering.ftrTimeSpent+obj_Filtering.impTimeSpent+obj_Filtering.bugTimeSpent));
//	    impRatio=(int)(obj_Filtering.impTimeSpent*100/(obj_Filtering.ftrTimeSpent+obj_Filtering.impTimeSpent+obj_Filtering.bugTimeSpent));
//	    
//	    System.out.println("---------Expected ratio Ftr, bug, Imp : "+ ftrRatio+" "+bugRatio+" "+impRatio);
//		
//		obj_problemDefinition = new problemDefinition (backlogIssueData, dblAvailableCapacity, bugRatio, ftrRatio, impRatio); 
//		
//		NSGAIIMultiRunner obj_NSGAIIMultiRunner = new NSGAIIMultiRunner (obj_problemDefinition); 
//		
//			transfernonDominatedSolutions=obj_NSGAIIMultiRunner.NSGARunner();
//			
//			for (int i=0;i<transfernonDominatedSolutions.size();i++){
////			 System.out.println(transfernonDominatedSolutions.get(i));
//				 for (int j=0;j<transfernonDominatedSolutions.get(i).getNumberOfVariables();j++){
//					 System.out.print(transfernonDominatedSolutions.get(i).getVariableValueString(j)+"--");
//				 }
//				 System.out.println(); 
//			}
//		
//		identifyOffered();
//		System.out.println("1st turn identify offered -----------------------------------");
//		for (int iter=0;iter<backlogIssueData.size();iter++){
//			System.out.println(backlogIssueData.get(iter).getStrKey() + "--"+backlogIssueData.get(iter).getDateCreated()+"--"+backlogIssueData.get(iter).getDateResolved()+"--"+backlogIssueData.get(iter).isOffered());	
//		}
//		
//		
//		//------------------------replan----------------------
//		
//		lastPLanning = list_replanning.get(list_replanning.size()-1); 
//		newReplanning = new Date(list_replanning.get(list_replanning.size()-1).getTime()+daysForReplan*24*60*60*1000); 
//		
//		while (newReplanning.before(releaseEnd)){
//			
//			list_replanning.add(newReplanning); 
//			System.out.println("newReplanning----"+newReplanning);
//			obj_replanning = new replanning (allIssueData,backlogIssueData, lastPLanning, newReplanning, bugRatio, ftrRatio, impRatio);
//			obj_replanning.preparePlanningIssues();
//			obj_replanning.replanCapacity();
//			obj_replanning.flashIssueList();
//			obj_replanning.performReplanning();
//			obj_replanning.modifyingIssueList();
//			backlogIssueData=obj_replanning.addNewlyOpenedIssues();
//			
//			dblUsedCapacity =0; 
//			for (int i=0;i<backlogIssueData.size();i++){
//				if (backlogIssueData.get(i).isOffered()==true){
//					offeredIssueData.add(backlogIssueData.get(i)); 
//					
//					dblUsedCapacity += backlogIssueData.get(i).getDefaultTimespent();
//					backlogIssueData.remove(i); 
//				}
//			}
//			
//			System.out.println("backlog after removing offered issues -----------------------------------");
//			for (int iter=0;iter<backlogIssueData.size();iter++){
//				System.out.println(backlogIssueData.get(iter).getStrKey() + "--"+backlogIssueData.get(iter).getDateCreated()+"--"+backlogIssueData.get(iter).getDateResolved()+"--"+backlogIssueData.get(iter).isOffered());	
//			}
//			
//			dblAvailableCapacity = dblAvailableCapacity-dblUsedCapacity; 
//			
//			obj_problemDefinition=null;
//			obj_NSGAIIMultiRunner=null;
//			transfernonDominatedSolutions=null; 
//			
//			obj_problemDefinition = new problemDefinition (backlogIssueData, dblAvailableCapacity, bugRatio, ftrRatio, impRatio); 
//			obj_NSGAIIMultiRunner = new NSGAIIMultiRunner (obj_problemDefinition); 
//			transfernonDominatedSolutions=obj_NSGAIIMultiRunner.NSGARunner();
//			identifyOffered();
//			
//			newReplanning = new Date(list_replanning.get(list_replanning.size()-1).getTime()+daysForReplan*24*60*60*1000);
//		}
//		
//		//adding the very last offered if no more replans. 
//		for (int i=0;i<backlogIssueData.size();i++){
//			if (backlogIssueData.get(i).isOffered()==true){
//				offeredIssueData.add(backlogIssueData.get(i)); 
//				backlogIssueData.remove(i); 
//			}
//		}
//		
//		
//		
//		
//		calculateResults (); 
//		
//		return  obj_resultFormat; 
//		
//		
//		
//		
//	}//main
//	
//	
//	public void identifyOffered (){
//		Random rand = new Random();
//		int solutionChoice = rand.nextInt(transfernonDominatedSolutions.size());
//		
//		System.out.println(solutionChoice + "--------------------------");
//		
//		for (int i=0;i<backlogIssueData.size();i++){
//			if (transfernonDominatedSolutions.get(solutionChoice).getVariableValueString(i).matches("1.0")){
//				backlogIssueData.get(i).setOffered(true);
//			}
//			
//			System.out.print(backlogIssueData.get(i).isOffered()+"--");
//		}
//	}
//	
//	public void calculateResults (){
//		
//		int totalValue = 0, totalActualValue=0; 
//		double totalFtrTimeSpent = 0, totalBugTimeSpent = 0,totalImpTimeSpent = 0; 
//		
//		for (DataIssueTemplate iterator: offeredIssueData){
//			totalValue += iterator.getPriorityValue(); 
//			
//			if (iterator.getIssueTypeValue()==1){
//				totalFtrTimeSpent += iterator.getDefaultTimespent(); 
//			}
//			
//			else if (iterator.getIssueTypeValue()==2){
//				totalBugTimeSpent += iterator.getDefaultTimespent(); 
//			}
//			else if(iterator.getIssueTypeValue()==1){
//				totalImpTimeSpent += iterator.getDefaultTimespent(); 
//			}
//		}
//		
//		for (DataIssueTemplate iterator: obj_Filtering.listInClose){
//			totalActualValue += iterator.getPriorityValue();
//		}
//		
//		obj_resultFormat.daysForReplan=daysForReplan; 
//	    obj_resultFormat.totalActualValue =  totalActualValue; 
//	    obj_resultFormat.totalValue = totalValue; 
//	    obj_resultFormat.actftrRatio=ftrRatio;
//	    obj_resultFormat.actbugRatio = bugRatio;
//	    obj_resultFormat.actimpRatio=impRatio; 
//	    obj_resultFormat.totalFtrTimeSpent=totalFtrTimeSpent;
//	    obj_resultFormat.totalBugTimeSpent=totalBugTimeSpent;
//	    obj_resultFormat.totalImpTimeSpent=totalImpTimeSpent; 
//	    obj_resultFormat.prpftrRatio=(totalFtrTimeSpent*100/(totalFtrTimeSpent+totalBugTimeSpent+totalImpTimeSpent));
//	    obj_resultFormat.prpbugRatio=(totalBugTimeSpent*100/(totalFtrTimeSpent+totalBugTimeSpent+totalImpTimeSpent));
//	    obj_resultFormat.prpimpRatio=(totalImpTimeSpent*100/(totalFtrTimeSpent+totalBugTimeSpent+totalImpTimeSpent));
//	    obj_resultFormat.distance = Math.sqrt(Math.pow((obj_resultFormat.actftrRatio-obj_resultFormat.prpftrRatio),2)+Math.pow((obj_resultFormat.actbugRatio-obj_resultFormat.prpbugRatio),2)+Math.pow((obj_resultFormat.actimpRatio-obj_resultFormat.prpimpRatio),2));
//		
//		System.out.println(); 
//		System.out.println("Total value proposed - "+ totalValue);
//		System.out.println("Total value achieved - "+ totalActualValue);
//		System.out.println("Expected ratio Ftr, bug, Imp : "+ ftrRatio+" "+bugRatio+" "+impRatio);
//		System.out.println("effort spent Ftr "+ totalFtrTimeSpent + " in percentage "+ (totalFtrTimeSpent*100/(totalFtrTimeSpent+totalBugTimeSpent+totalImpTimeSpent)));
//		System.out.println("effort spent Bug "+ totalBugTimeSpent + " in percentage "+ (totalBugTimeSpent*100/(totalFtrTimeSpent+totalBugTimeSpent+totalImpTimeSpent)));
//		System.out.println("effort spent Imp "+ totalImpTimeSpent + " in percentage "+ (totalImpTimeSpent*100/(totalFtrTimeSpent+totalBugTimeSpent+totalImpTimeSpent)));
//		
//		
//	}
//	
//	
//	
//}//class
//
//
//
//
//
//
//
