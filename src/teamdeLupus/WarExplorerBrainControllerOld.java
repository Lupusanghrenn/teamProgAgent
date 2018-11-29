package teamdeLupus;

import java.lang.reflect.Method;
import java.util.List;

import edu.warbot.agents.agents.WarExplorer;
import edu.warbot.agents.enums.WarAgentCategory;
import edu.warbot.agents.enums.WarAgentType;
import edu.warbot.agents.percepts.WarAgentPercept;
import edu.warbot.brains.brains.WarExplorerBrain;
import edu.warbot.communications.WarMessage;

public abstract class WarExplorerBrainControllerOld extends WarExplorerBrain {
	
	List<WarAgentPercept> ar;
	Sorted_Percepts sp;
	List<WarMessage> messages;
	String target="none";

    public WarExplorerBrainControllerOld() {
        super();
    }

    @Override
    public String action() {
    	this.ar = this.getPercepts();
    	this.sp = new Sorted_Percepts(this.getPercepts(),this.getTeamName());
    	this.messages = this.getMessages();
    	

    	if (isBlocked()){
            setRandomHeading();
            return WarExplorer.ACTION_MOVE;
        }
    	
    	//Bag full
    	if(this.isBagFull() || (target.equals("base") && this.getNbElementsInBag()>0)){
    		return bagIsFull();
    	}
    	//else if(ar.size()>0){
    	else if(sp.getRessources().size()>0){
    		return collecteRessources();
    		
    	}
    	else if(messages.size()>0){
    		String m = traitementMessage();
    		if(m!=null){
    			return m;
    		}
    		
    	}
    	//this.setDebugString(target);
        return WarExplorer.ACTION_MOVE;
    }
    
    public String bagIsFull(){
    	this.setDebugString("BagIsFull");
    	if(target.equals("base") && this.getNbElementsInBag()>0){
    		if(sp.getBases().size()>0){
    			this.setHeading(sp.getBases().get(0).getAngle());
    		}
    		if(sp.getBases().size()>0 && sp.getBases().get(0).getDistance()<this.MAX_DISTANCE_GIVE){
    			//set agent to give
    			System.out.println("Je give");
    			this.setDebugString("Je give");
    			this.setIdNextAgentToGive(sp.getBases().get(0).getID());
    			return WarExplorer.ACTION_GIVE;
    		}
    		return WarExplorer.ACTION_MOVE;
    	}
    	
    	if(sp.getBases().size()!=0){
    		target="base";
    		this.setHeading(sp.getBases().get(0).getAngle());
    		return WarExplorer.ACTION_MOVE;
    	}
		this.broadcastMessageToAgentType(WarAgentType.WarBase, "Where is the base");
		for(WarMessage m : messages){
			if(m.getMessage().equals("I'm here")){
				//on se dirige vers la bouffe
				target="base";
				this.setHeading(m.getAngle());
				return WarExplorer.ACTION_MOVE;
			}
		}
		return WarExplorer.ACTION_IDLE;
    }
    
    public String collecteRessources(){
    	//détection de ressources
		this.setDebugString("Cherche Ressources");
		if(target.equals("food")){
			if(sp.getRessources().size()==0){
				//ressourcesDisparu
				target="none";
			}else {
				WarAgentPercept p = sp.getRessources().get(0);
				if(p.getDistance()<1d){
					target="none";
					return WarExplorer.ACTION_TAKE;
				}else{
					this.setHeading(p.getAngle());
					return WarExplorer.ACTION_MOVE;
				}
			}
		}
		
		if(sp.getRessources().size()>0){
			//ressources detecté
			target="food";
			WarAgentPercept p = sp.getRessources().get(0);
			this.broadcastMessageToAgentType(WarAgentType.WarExplorer, "FoundFood", String.valueOf(p.getAngle()),String.valueOf(p.getDistance()));
			if(p.getDistance()<1d){
				target="none";
				return WarExplorer.ACTION_TAKE;
			}else{
				this.setHeading(p.getAngle());
				return WarExplorer.ACTION_MOVE;
			}
		}
		return WarExplorer.ACTION_MOVE;
    }
    
    public String traitementMessage(){
    	for(WarMessage m : messages){
			if(m.getMessage().equals("FoundFood")){
				//on se dirige vers la bouffe
				this.setHeading(m.getAngle());
				return WarExplorer.ACTION_MOVE;
			}
		}
    	return null;
    }
    
    

}
