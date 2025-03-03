package main;

import java.util.Comparator;

import entities.Entity;

/**
 * manages the order of the entities for the draw
 */
public class DepthComparator implements Comparator<Entity>{

/**
 * manages the order in which the gamePanel draws the objects, to ensure realistic depth between the entities moving
 */
	@Override
	public int compare(Entity ent1, Entity ent2) {
		int position;

		if (ent1.kind== "tomb") {
			position= 1;
		}
		else if (ent2.kind== "tomb") {
			position= -1;
		}
		else if (ent1.kind== "power up") {
			position= -1;
		}
		else if (ent2.kind== "power up") {
			position= 1;
		}
		else if (ent1.kind== "bomb" || ent1.kind== "exploded bomb") {
			position= -1;
		}
		else if (ent2.kind== "bomb" || ent2.kind== "exploded bomb") {
			position= 1;
		}
		else if (ent1.kind== "senshiyan" && ent2.kind== "soft wall") {
			position= 1;
		}
		else if (ent1.kind== "soft wall" && ent2.kind== "senshiyan") {
			position= -1;
		}
		else if (ent1.kind== "flames") {
			position= 1;
		}
		else if (ent2.kind== "flames") {
			position= -1;
		}
		else {
			position= ent1.dfY-ent2.dfY;
		}
		
		return position;
	}
}
