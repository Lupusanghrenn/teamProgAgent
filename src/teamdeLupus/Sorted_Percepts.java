package teamdeLupus;

import java.util.ArrayList;
import java.util.List;

import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarAgentPercept;

public class Sorted_Percepts {
	
	private ArrayList<WarAgentPercept> ressources;
	private ArrayList<WarAgentPercept> ennemies;
	//private ArrayList<WarAgentPercept> allies;
	private ArrayList<WarAgentPercept> bases;
	//private String teamName="teamDeLupus";
	
	public Sorted_Percepts (List<WarAgentPercept> percepts, String teamName){
		//sorted by type and closest in position 1
		this.bases=new ArrayList<WarAgentPercept>();
		//this.allies=new ArrayList<WarAgentPercept>();
		this.ressources=new ArrayList<WarAgentPercept>();
		this.ennemies=new ArrayList<WarAgentPercept>();
		
		if(percepts.size()>0){
			for(WarAgentPercept p : percepts){
				if(p.getType().equals(WarAgentType.WarFood)){
					//sort
					if(ressources.size()>0 && ressources.get(0).getDistance()>p.getDistance()){
						ressources.add(0, p);
					}else{
						ressources.add(p);
					}
					
				}else if(p.getType().equals(WarAgentType.WarBase) && p.getTeamName().equals(teamName)){
					if(bases.size()>0 && bases.get(0).getDistance()>p.getDistance()){
						bases.add(0, p);
					}else{
						bases.add(p);
					}
				}else if(!p.getTeamName().equals(teamName)){
					ennemies.add(p);
				}
				
			}
		}
	}
	
	public ArrayList<WarAgentPercept> getBases(){
		return this.bases;
	}
	
	public WarAgentPercept getClosestBase(){
		if(bases.size()>0){
			return bases.get(0);
		}else{
			return null;
		}
	}
	
	public ArrayList<WarAgentPercept> getRessources(){
		return this.ressources;
	}
	
	public WarAgentPercept getClosestRessources(){
		if(ressources.size()>0){
			return ressources.get(0);
		}else{
			return null;
		}
	}
	
//	public ArrayList<WarAgentPercept> getAllies(){
//		return this.allies;
//	}
	
	public ArrayList<WarAgentPercept> getEnnemies(){
		return this.ennemies;
	}
}
