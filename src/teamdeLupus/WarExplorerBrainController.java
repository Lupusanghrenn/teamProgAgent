package teamdeLupus;


import java.awt.Color;
import java.util.ArrayList;

import edu.warbot.agents.MovableWarAgent;
import edu.warbot.agents.WarAgent;
import edu.warbot.agents.WarResource;
import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.agents.percepts.WarPercept;
import edu.warbot.brains.WarBrain;
import edu.warbot.brains.brains.WarExplorerBrain;
import edu.warbot.communications.WarMessage;

public abstract class WarExplorerBrainController extends WarExplorerBrain {
	
	WTask ctask;
	Sorted_Percepts sp;
	String target;
	
	@Override
	public String action() {
		
		// Develop behaviour here
    	this.sp = new Sorted_Percepts(this.getPercepts(),this.getTeamName());
		
		String toReturn = ctask.exec(this);   // le run de la FSM
		
		if(toReturn == null){
			if (isBlocked())
				setRandomHeading();
			return WarExplorer.ACTION_MOVE;
		} else {
			return toReturn;
		}
	}
	
	static WTask handleMsgs = new WTask(){ 
		String exec(WarBrain bc){return "";}
	};
	
	static WTask returnFoodTask = new WTask(){
		String exec(WarBrain bc){
			WarExplorerBrainController me = (WarExplorerBrainController) bc;
			if(me.isBagEmpty()){
				me.setHeading(me.getHeading() + 180);
				me.target="none";
				me.ctask = getFoodTask;
				return(null);
			}
					
			me.setDebugStringColor(Color.green.darker());
			me.setDebugString("Returning Food");
				
			if(me.isBlocked()){
				me.setRandomHeading();
				me.target="none";
			}

			if(me.target.equals("base") && me.getNbElementsInBag()>0){
	    		if(me.sp.getBases().size()>0){
	    			me.setHeading(me.sp.getClosestBase().getAngle());
	    		}
	    		if(me.sp.getBases().size()>0 && me.sp.getBases().get(0).getDistance()<me.MAX_DISTANCE_GIVE){
	    			//set agent to give
	    			System.out.println("Je give");
	    			me.setDebugString("Je give");
	    			me.setIdNextAgentToGive(me.sp.getBases().get(0).getID());
	    			return WarExplorer.ACTION_GIVE;
	    		}
	    		return WarExplorer.ACTION_MOVE;
	    	}
		    	
	    	if(me.sp.getBases().size()!=0){
	    		me.target="base";
	    		me.setHeading(me.sp.getBases().get(0).getAngle());
	    		return WarExplorer.ACTION_MOVE;
	    	}
	    	
	    	WarMessage m = me.getMessageFromBase();
	    	if(m!=null){
	    		System.out.println("Message recu");
	    		me.target="base";
	    		me.setHeading(m.getAngle());
	    		return WarExplorer.ACTION_MOVE;
	    	}
			return WarExplorer.ACTION_MOVE;
				
			}
		};
	
	static WTask getFoodTask = new WTask(){
		String exec(WarBrain bc){
			WarExplorerBrainController me = (WarExplorerBrainController) bc;
			if(me.isBagFull()){
				me.ctask = returnFoodTask;
				return(null);
			}
			
			if(me.isBlocked())
				me.setRandomHeading();
			
			me.setDebugStringColor(Color.BLACK);
			me.setDebugString("Searching food");
			
			WarAgentPercept foodPercept = me.sp.getClosestRessources();
			
			//Si il y a de la nouriture
			if(foodPercept != null){
				me.broadcastMessageToAgentType(WarAgentType.WarExplorer, "FoundFood", String.valueOf(foodPercept.getAngle()),String.valueOf(foodPercept.getDistance()));
				if(foodPercept.getDistance() > WarResource.MAX_DISTANCE_TAKE){
					me.setHeading(foodPercept.getAngle());
					return(WarExplorer.ACTION_MOVE);
				}else{
					return(WarExplorer.ACTION_TAKE);
				}
			}
			//gestion messageFood
			WarMessage m = me.getMessageAboutFood();
			if(m!=null){
				me.setHeading(m.getAngle());
			}
			return(WarExplorer.ACTION_MOVE);
		}
	};

	
	
	public WarExplorerBrainController() {
		super();
		ctask = getFoodTask; // initialisation de la FSM
		target="none";
		//Sorted_Percepts sp=new Sorted_Percepts(this.getPercepts(),this.getTeamName());
	}

    
		
	
	private WarMessage getMessageAboutFood() {
		for (WarMessage m : getMessages()) {
			if(m.getMessage().equals("FoundFood"))
				return m;
		}
		return null;
	}
	
	private WarMessage getMessageFromBase() {
		for (WarMessage m : getMessages()) {
			if(m.getSenderType().equals(WarAgentType.WarBase))
				return m;
		}
		
		broadcastMessageToAgentType(WarAgentType.WarBase, "Where is the base", "");
		return null;
	}

}


