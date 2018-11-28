# teamProgAgent

Things to do :

# Divers

Sorted Percept - Done

# Agents

### Explorer :

#### Explorer food
FSM - Done
Cycle recupération/Base etc - Done
Envoie de message "Food" - Done

Si 5 ou + food --> Message engineer : "Build turret"
use this.getAveragePositionOfUnitPercept()

#### Explorer ennemy base
Envoie d'un message aux rocket launcher + heavy si base ennemi


### Turret :

Système d'anticipation des tirs (raven)


### Engineer :

Réagir au message des explores pour construire les tourelles


### Rocket Launcher

Recoit message de Explorer Ennemy Base --> Tire sur la base ennemi (utilisation de trigo)


### Base

message de ping pour compter les troupes à intervalle régulier.

gestion du recrutement des troupes en fonctions des unités manquantes entre autres --> unite low hp envoie un message "i m dead"

Creation d 'une base si une base est attaque et ressources suffisantes


### Heavy + light

protection des zones de nourriture à leur apparition.

attaque des ennemis a proximité



# Role

### Role de recolte

Explorer food

Engineer

(Light or Heavy)

### Role d'attaque

Explorer Ennemy base

Heavy

Light

Rocket Launcher
