import dataTemplates.ReleaseInfoCollection;
import dataTemplates.resultTemplate;
import dataTemplates.ReleaseCalendarTemplate;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;

public class mainRunning {

	public static ArrayList<ReleaseCalendarTemplate> releaseInfo = new ArrayList<ReleaseCalendarTemplate> ();
	public static ReleaseInfoCollection obj_ReleaseInfoCollection;
	public static mainProgram objMainProgrm;   
	public static Date releaseStart=null, releaseEnd = null; 
	
	public static int releasenum1 = 0;
	public static int releasenum2 = 0; 
	
	public static ArrayList <resultTemplate> list_resultFormat = new ArrayList <resultTemplate> (); 
	
	public static void main (String args[]) throws Exception {
		
		
		for (int rel=2;rel<18;rel++){
		
			releasenum1=rel; 
			releasenum2=rel+1; 
				
			collectedReleasInfo(); 
				releaseStart=obj_ReleaseInfoCollection.getReleaseDate(releasenum1);
				releaseEnd=obj_ReleaseInfoCollection.getReleaseDate(releasenum2);
				
				 
				
				for (int i=2;i<15;i=i+2){
					objMainProgrm = new mainProgram (); 
					
					list_resultFormat.add(objMainProgrm.mainProgramRun(releaseStart,releaseEnd , i));
					objMainProgrm=null; 
				}
				
				PrintStream out = new PrintStream(new FileOutputStream("output"+releasenum1+".txt"));
				System.setOut(out);
				
				System.out.println(
						"releasenum1,"+
						"releasenum2,"+
						"iterator.daysForReplan,"+
						"iterator.totalValue,"+
						"iterator.totalActualValue,"+
						 "iterator.actftrRatio,"+
						 "iterator.actbugRatio,"+
						 "iterator.actimpRatio,"+
						 "iterator.totalFtrTimeSpent,"+
						 "iterator.totalBugTimeSpent,"+
						 "iterator.totalImpTimeSpent,"+
						 "iterator.prpftrRatio,"+
						 "iterator.prpbugRatio,"+
						 "iterator.prpimpRatio,"+
						 "iterator.distance");
				
				for (resultTemplate iterator: list_resultFormat){
		//			System.out.println(); 
		//			System.out.println("days for replan "+ iterator.daysForReplan); 
		//			System.out.println("Total value proposed - "+ iterator.totalValue);
		//			System.out.println("Total value achieved - "+ iterator.totalActualValue);
		//			System.out.println("Expected ratio Ftr, bug, Imp : "+ iterator.actftrRatio+" "+iterator.actbugRatio+" "+iterator.actimpRatio);
		//			System.out.println("effort spent Ftr "+ iterator.totalFtrTimeSpent + " in percentage "+ iterator.prpftrRatio);
		//			System.out.println("effort spent Bug "+ iterator.totalBugTimeSpent + " in percentage "+ iterator.prpbugRatio);
		//			System.out.println("effort spent Imp "+ iterator.totalImpTimeSpent + " in percentage "+ iterator.prpimpRatio);
					
					System.out.println(
							releasenum1+ ","+
							releasenum2+ ","+
							iterator.daysForReplan+ ","+
							iterator.totalValue + ","+
							iterator.totalActualValue+ ","+
							 iterator.actftrRatio+ ","+
							 iterator.actbugRatio+ ","+
							 iterator.actimpRatio+ ","+
							 iterator.totalFtrTimeSpent+ ","+
							 iterator.totalBugTimeSpent+ ","+
							 iterator.totalImpTimeSpent+ ","+
							 iterator.prpftrRatio+ ","+
							 iterator.prpbugRatio+ ","+
							 iterator.prpimpRatio+ ","+
							 iterator.distance);
				
				}
		
		}
		
		
	}
	
	public static void collectedReleasInfo(){
		
		obj_ReleaseInfoCollection = new ReleaseInfoCollection (releaseInfo); 
		
		//------------Releases from BSQ-Mail project 
		
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-02-05", true, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-05-14", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-05-18", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-05-26", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-06-16", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-05-30", false, "R");
//		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-06-16", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-07-14", false, "NR");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-07-14", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-07-28", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-08-16", false, "NR");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-08-25", false, "NR");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-09-01", false, "NR");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-09-07", false, "NR");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-09-09", false, "NR");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-09-14", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-10-14", false, "NR");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-11-10", false, "NR");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2016-12-22", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2017-03-30", false, "NR");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2017-04-27", false, "R");
		obj_ReleaseInfoCollection.fillUpReleaseInfo("2017-05-11", false, "NR");

}
	
}




