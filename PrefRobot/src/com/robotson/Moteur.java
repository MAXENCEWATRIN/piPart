package com.robotson;

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class Moteur extends Thread {

	private int pasInterval = 2;
	private int pasParRevolution = 200;
	private GpioStepperMotorComponent rotor;
	private int pas = 0;
	private boolean buteeHG = false;
	private boolean buteeHD = false;
	private boolean buteeVH = false;
	private boolean buteeVB = false;
	private byte[] single_step_sequence = { (byte) 0b0001, (byte) 0b0010, (byte) 0b0100, (byte) 0b1000 };
	private boolean actif=false;
	
	
	
	private MoteurControle moteurControleThread = new MoteurControle();

	
	// constructeur par defaut
	public Moteur() {

	}
	

	// constructeur par parametres
	public Moteur(GpioController gpio, Pin bobinePinGpioNord, Pin bobinePinGpioOuest, Pin bobinePinGpioSud, Pin bobinePinGpioEst) {

		// instance des bobines
		Bobine bobineNord = new Bobine(gpio, bobinePinGpioNord);
		Bobine bobineOuest = new Bobine(gpio, bobinePinGpioOuest);
		Bobine bobineSud = new Bobine(gpio, bobinePinGpioSud);
		Bobine bobineEst = new Bobine(gpio, bobinePinGpioEst);

		// provision gpio pins #00 to #03 as output pins and ensure in LOW state
		final GpioPinDigitalOutput[] pins = { bobineNord.getPinBobine(),
				bobineOuest.getPinBobine(), bobineSud.getPinBobine(),
				bobineEst.getPinBobine() };

		// stop le moteur lorsque le programme est termine
		gpio.setShutdownOptions(true, PinState.LOW, pins);

		// creer un composant moteur
		this.rotor = new GpioStepperMotorComponent(pins);

		// parametrage du moteur
		this.rotor.setStepInterval(this.pasInterval);
		this.rotor.setStepsPerRevolution(this.pasParRevolution);
		this.rotor.setStepSequence(this.single_step_sequence);
	}

	
	
	
	//////// methode mouvement moteur par preference ////////
	public void preference(int pas) {
		this.pas = pas;
		
		// deplacer a droite sauf si on est en butee
		if (!this.buteeHD) {
			this.buteeHG = false;
			if (!moteurControleThread.isAlive()) {
				moteurControleThread = new MoteurControle(this);
				moteurControleThread.start();
			}
		}
	}

	
	// arreter le moteur
	public void arreterMoteur() {
		// System.out.println("STEP ARRETE : ");
		this.rotor.stop();
		this.moteurControleThread.stop();
	}

	
	
	// thread moteur
	public class MoteurControle extends Thread {

		private Moteur moteur;
		
		//constructeur par defaut
		public MoteurControle(){
			
		}
		//constructeur par parametre
		public MoteurControle(Moteur mi){
			this.moteur=mi;	
		}
		
		
		
		
		// lancement du thread
		public void run() {
			System.out.println("EN ROUTE....");
			rotor.step(pas);
			System.out.println("TERMINE....");
			this.moteur.setActif(false);
		}

		// fin de la classe Thread
	}
	
	
	
	

	// getters et setters
	public boolean isButeeHG() {
		return buteeHG;
	}

	public int getPas() {
		return pas;
	}

	public void setPas(int pas) {
		this.pas = pas;
	}

	public MoteurControle getMoteurControleThread() {
		return moteurControleThread;
	}

	public void setMoteurControleThread(MoteurControle moteurControleThread) {
		this.moteurControleThread = moteurControleThread;
	}

	public void setButeeHG(boolean buteeHG) {
		this.buteeHG = buteeHG;
	}

	public boolean isButeeHD() {
		return buteeHD;
	}

	public void setButeeHD(boolean buteeHD) {
		this.buteeHD = buteeHD;
	}

	public int getPasInterval() {
		return pasInterval;
	}

	public void setPasInterval(int pasInterval) {
		this.pasInterval = pasInterval;
	}

	public int getPasParRevolution() {
		return pasParRevolution;
	}

	public void setPasParRevolution(int pasParRevolution) {
		this.pasParRevolution = pasParRevolution;
	}

	public boolean isButeeVH() {
		return buteeVH;
	}

	public void setButeeVH(boolean buteeVH) {
		this.buteeVH = buteeVH;
	}

	public boolean isButeeVB() {
		return buteeVB;
	}

	public void setButeeVB(boolean buteeVB) {
		this.buteeVB = buteeVB;
	}


	public boolean isActif() {
		return actif;
	}


	public void setActif(boolean actif) {
		this.actif = actif;
	}


	
	
	
	// fin de la classe
}
