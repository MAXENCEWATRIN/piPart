package com.robotson;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;




public class Joystick {

	//Pin GPIO du joystick pour capter les mouvements
	private GpioPinDigitalInput pinEtatGauche;
	private GpioPinDigitalInput pinEtatHaut;
	private GpioPinDigitalInput pinEtatDroite;
	private GpioPinDigitalInput pinEtatBas;
	


	// constructeur par defaut
	public Joystick() {

	}
	

	// contructeur par parametre
	public Joystick(GpioController gpio, Pin pinGaucheGpio, Pin pinDroitGpio,Pin pinHautGpio, Pin pinBasGpio) {

		this.pinEtatGauche = gpio.provisionDigitalInputPin(pinGaucheGpio,PinPullResistance.PULL_DOWN);
		this.pinEtatGauche.setName("GAUCHE");
		this.pinEtatHaut = gpio.provisionDigitalInputPin(pinDroitGpio,PinPullResistance.PULL_DOWN);
		this.pinEtatHaut.setName("HAUT");
		this.pinEtatDroite = gpio.provisionDigitalInputPin(pinHautGpio,PinPullResistance.PULL_DOWN);
		this.pinEtatDroite.setName("DROITE");
		this.pinEtatBas = gpio.provisionDigitalInputPin(pinBasGpio,PinPullResistance.PULL_DOWN);
		this.pinEtatBas.setName("BAS");
	}

	


	
	/*********************************************************************************
	 * 
	 * @author pi
	 *
	 ******************************************************************************/
	// Ecouteur du Joystick
	public static class JoystickEcouteur implements GpioPinListenerDigital {
		
		
		//lien avec deux moteurs
		private Moteur moteur;
			
		
		
		//constructeur par parametres
		public JoystickEcouteur(Moteur mi) {
			super();
			this.moteur=mi;
		}
		

		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
			//moteur.directionGauche();
			
			//pin contient GAUCHE/HAUT/DROITE/BAS
			String pin=event.getPin().toString();
			//state contient HIGH ou LOW
			String state=event.getState().toString();
			
			System.out.println("pin "+pin+ " state "+state);
			
			

			
			//DROITE		
			if(pin.contains("DROITE")){
					if(state.contains("HIGH")){
						this.moteur.directionDroite();
					}else{
						this.moteur.arreterMoteur();
					}
			}
			//GAUCHE
			else if(pin.contains("GAUCHE")){
					if(state.contains("HIGH")){
						this.moteur.directionGauche();
					}else{
						this.moteur.arreterMoteur();
					}
			}
			//HAUT
			if(pin.contains("HAUT")){
					if(state.contains("HIGH")){
						this.moteur.directionHaut();
					}else{
						this.moteur.arreterMoteur();
					}
			}
			//BAS
			else if(pin.contains("BAS")){
					if(state.contains("HIGH")){
						this.moteur.directionBas();
					}else{
						this.moteur.arreterMoteur();
					}
			}
			

		}
		
		
	//fin de la classe ECOUTEUR
	}



	
	//getters setters
	public GpioPinDigitalInput getPinEtatGauche() {
		return pinEtatGauche;
	}


	public void setPinEtatGauche(GpioPinDigitalInput pinEtatGauche) {
		this.pinEtatGauche = pinEtatGauche;
	}




	public GpioPinDigitalInput getPinEtatDroite() {
		return pinEtatDroite;
	}



	public void setPinEtatDroite(GpioPinDigitalInput pinEtatDroite) {
		this.pinEtatDroite = pinEtatDroite;
	}



	public GpioPinDigitalInput getPinEtatHaut() {
		return pinEtatHaut;
	}


	public void setPinEtatHaut(GpioPinDigitalInput pinEtatHaut) {
		this.pinEtatHaut = pinEtatHaut;
	}


	public GpioPinDigitalInput getPinEtatBas() {
		return pinEtatBas;
	}


	public void setPinEtatBas(GpioPinDigitalInput pinEtatBas) {
		this.pinEtatBas = pinEtatBas;
	}


	
	
	
//fin de la classe
}



	
