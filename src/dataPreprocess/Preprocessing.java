package dataPreprocess;

import java.util.ArrayList;
import java.util.Date;

import dataTemplates.DataIssueTemplate;

public class Preprocessing {
	
	public ArrayList<DataIssueTemplate> allIssueData; 
	public ArrayList<DataIssueTemplate> listEarlyOpen = new ArrayList<DataIssueTemplate>();
	public ArrayList<DataIssueTemplate> listInClose = new ArrayList<DataIssueTemplate>();
	
	Date releaseStart, releaseEnd;
	
	public double totalCapacity=0, ftrTimeSpent=0,impTimeSpent=0,bugTimeSpent=0; 
	
	
	
	
	public Preprocessing (ArrayList<DataIssueTemplate> tmp_IssueData, Date tmp_releaseStart, Date tmp_releaseEnd){
		this.allIssueData= tmp_IssueData; 
		releaseStart=tmp_releaseStart; 
		releaseEnd=tmp_releaseEnd;
	}
	
	
	
//	public void filteringIssues(){
//		int counter1 = 0, counter2 = 0; 
//		
//		for (DataIssueTemplate iterator: allIssueData){
////			System.out.println(iterator.getStrKey()+"-----");
//			
//			if (iterator.getDateCreated().before(releaseStart) && iterator.getDateResolved()==null){
//				EarlyOpenInandLateClose.add(iterator); 				
//			}
//			
//			if (iterator.getDateCreated().before(releaseStart)&& iterator.getDateResolved()!=null){
//				if(iterator.getDateResolved().after(releaseStart)){
//					EarlyOpen.add(iterator); 
//				}
//			}
//			
//			if (iterator.getDateResolved().after(releaseStart) && iterator.getDateResolved().before(releaseEnd)){
//				InClose.add(iterator); 				
//			}
//			
//			if (iterator.getDateCreated().before(releaseStart) && iterator.getDateResolved().before(releaseEnd)){
//				earlyopenInClose.add(iterator); 				
//			}
//			
//			
//
//			if (iterator.getDateResolved().after(releaseStart) && iterator.getDateResolved().before(replanday)){
//				newforreplan.add(iterator); 				
//			}
//			
//		}
//		
//		System.out.println(counter1+"--------"+counter2);
//	}
	
	
	
	public void filterIssuesEarlyOpen (){
		for (DataIssueTemplate iterator: allIssueData){
			if (iterator.getDateCreated().before(releaseStart) && iterator.getDateResolved()==null){
				listEarlyOpen.add(iterator); 				
			}
			
			if (iterator.getDateCreated().before(releaseStart)&& iterator.getDateResolved()!=null){
				if(iterator.getDateResolved().after(releaseStart)){
					listEarlyOpen.add(iterator); 
				}
			}
	    }
	}
	
	public void filterIssueInClose (){
		for (DataIssueTemplate iterator: allIssueData){
				if (iterator.getDateResolved()!=null){
					if(iterator.getDateResolved().after(releaseStart) && iterator.getDateResolved().before(releaseEnd)){
						listInClose.add(iterator); 
						Date tmpDiffDate =null; 
						if (iterator.getDateUpdated().before(releaseStart)){
							tmpDiffDate = releaseStart; 
						}
						else {
							tmpDiffDate=iterator.getDateUpdated();
						}
						totalCapacity += iterator.getTimespent(tmpDiffDate); 
						
						if (iterator.getIssueTypeValue()==1){
							ftrTimeSpent += iterator.getTimespent(tmpDiffDate); 
						}
						
						else if (iterator.getIssueTypeValue()==2){
							bugTimeSpent += iterator.getTimespent(tmpDiffDate); 
						}
						else if(iterator.getIssueTypeValue()==1){
							impTimeSpent += iterator.getTimespent(tmpDiffDate); 
						}
					}
				}
	    }
	}
	
	
}//end of class
