package com.robotson;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class Initialisation {

	private GpioPinDigitalInput pinCapteurHG; 
	private GpioPinDigitalInput pinCapteurVB;
	//lien avec deux moteurs
	private Moteur moteurHorizontal;
	private Moteur moteurVertical;
	
	

	//une seule instance
	private static final Initialisation instance = new Initialisation();
    
    private Initialisation(){
    	
    }

    public static Initialisation getInstance(){
        return instance;
    }
	
	
	
	//constructeur par defaut
	public void setParametres(Moteur moteurHorizontal,Moteur moteurVertical,GpioController gpio, Pin pinCapteurHG, String namePinCapteurHG, Pin pinCapteurVB, String namePinCapteurVB) {
		this.moteurHorizontal=moteurHorizontal;
		this.moteurVertical=moteurVertical;
		this.pinCapteurHG = gpio.provisionDigitalInputPin(pinCapteurHG,PinPullResistance.PULL_DOWN);
		this.pinCapteurHG.setName(namePinCapteurHG);
		this.pinCapteurVB = gpio.provisionDigitalInputPin(pinCapteurVB,PinPullResistance.PULL_DOWN);
		this.pinCapteurVB.setName(namePinCapteurVB);
	}
	
	
	
	
	// Ecouteur du capteur
	public static class CapteurButeeEcouteur implements GpioPinListenerDigital {
		
		//lien avec deux moteurs
		private Moteur moteur;
				
				
		public CapteurButeeEcouteur(Moteur mi) {
			this.moteur=mi;
		}
		

		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
			//moteur.directionGauche();
			
			//pin contient GAUCHE/HAUT/DROITE/BAS
			String pin=event.getPin().toString();
			//state contient HIGH ou LOW
			String state=event.getState().toString();
			
			
			//BUTEEHG
			if(pin.contains("BUTEEHG")){
				if(state.contains("HIGH")){
					this.moteur.arreterMoteur();
					this.moteur.interrupt();
				}
			}
			
			//BUTEEVB
			if(pin.contains("BUTEEVB")){
				if(state.contains("HIGH")){
					this.moteur.arreterMoteur();
					this.moteur.interrupt();
				}
			}
		}
	
	}


	// demarrer les threads
	public void demarrer(){
		this.moteurHorizontal.start();
		this.moteurVertical.start();
	}
	
	
	
	// getter / setter
	public GpioPinDigitalInput getPinCapteurHG() {
		return pinCapteurHG;
	}

	public void setPinCapteurHG(GpioPinDigitalInput pinCapteurHG) {
		this.pinCapteurHG = pinCapteurHG;
	}

	public GpioPinDigitalInput getPinCapteurVB() {
		return pinCapteurVB;
	}

	public void setPinCapteurVB(GpioPinDigitalInput pinCapteurVB) {
		this.pinCapteurVB = pinCapteurVB;
	}
	
//fin de la classe
}
