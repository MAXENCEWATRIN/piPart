package com.robotson;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;


public class CapteurButee {

	private GpioPinDigitalInput pinCapteur;

	
	
	//constructeur par defaut
	public CapteurButee() {
	}
	
	
	//constructeur par parametre
	public CapteurButee(GpioController gpio, Pin pinCapteur,String name) {
		this.pinCapteur = gpio.provisionDigitalInputPin(pinCapteur,PinPullResistance.PULL_DOWN);
		this.pinCapteur.setName(name);
	}
	
	

	// Ecouteur du capteur
	public static class CapteurButeeEcouteur implements GpioPinListenerDigital {
		
		//lien avec deux moteurs
		private Moteur moteur;
				
				
		public CapteurButeeEcouteur(Moteur mi) {
			super();
			this.moteur=mi;
		}
		

		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
			//moteur.directionGauche();
			
			//pin contient GAUCHE/HAUT/DROITE/BAS
			String pin=event.getPin().toString();
			//state contient HIGH ou LOW
			String state=event.getState().toString();
			
			
			//HORIZONTALGAUCHE
			if(pin.contains("HORIZONTALGAUCHE")){
				if(state.contains("HIGH")){
					System.out.println("APPUI HORIZONTALGAUCHE");
					this.moteur.arreterMoteur();
					this.moteur.setButeeHG(true);
				}
			}
			//HORIZONTALDROIT
			else if(pin.contains("HORIZONTALDROIT")){
				if(state.contains("HIGH")){
					System.out.println("APPUI HORIZONTALDROIT");
					this.moteur.arreterMoteur();
					this.moteur.setButeeHD(true);
				}
			}
			//VERTICALHAUT
			if(pin.contains("VERTICALHAUT")){
				if(state.contains("HIGH")){
					System.out.println("APPUI VERTICALHAUT");
					this.moteur.arreterMoteur();
					this.moteur.setButeeVH(true);
				}
			}
			//VERTICALBAS
			else if(pin.contains("VERTICALBAS")){
				if(state.contains("HIGH")){
					System.out.println("APPUI VERTICALBAS");
					this.moteur.arreterMoteur();
					this.moteur.setButeeVB(true);
				}
			}
			
		}
		
	
	//fin de la classe interne
	}
	
	
	
	// getter / setter
	public GpioPinDigitalInput getPinCapteur() {
		return pinCapteur;
	}

	public void setPinCapteur(GpioPinDigitalInput pinCapteur) {
		this.pinCapteur = pinCapteur;
	}

	
	
//fin de la classe
}
