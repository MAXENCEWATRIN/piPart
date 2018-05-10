package com.robotson;

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;



//Le moteur est un thread autonome
public class Moteur extends Thread {

	private int pasInterval = 2;
	private int pasParRevolution = 200;
	private GpioStepperMotorComponent rotor;
	private byte[] single_step_sequence = { (byte) 0b0001, (byte) 0b0010, (byte) 0b0100, (byte) 0b1000 };
	private boolean actif=false;

	
	
	
	// constructeur par defaut
	public Moteur() {

	}
	

	// constructeur par parametres
	public Moteur(GpioController gpio, Pin bobinePinGpioNord,
			Pin bobinePinGpioOuest, Pin bobinePinGpioSud, Pin bobinePinGpioEst) {

		Bobine bobineNord = new Bobine(gpio, bobinePinGpioNord);
		Bobine bobineOuest = new Bobine(gpio, bobinePinGpioOuest);
		Bobine bobineSud = new Bobine(gpio, bobinePinGpioSud);
		Bobine bobineEst = new Bobine(gpio, bobinePinGpioEst);

		// provision gpio pins #00 to #03 as output pins and ensure in LOW state
		final GpioPinDigitalOutput[] pins = { 
				bobineNord.getPinBobine(),
				bobineOuest.getPinBobine(), 
				bobineSud.getPinBobine(),
				bobineEst.getPinBobine() 
				};

		// this will ensure that the motor is stopped when the program
		// terminates
		gpio.setShutdownOptions(true, PinState.LOW, pins);
		
		// create motor component
	    this.rotor = new GpioStepperMotorComponent(pins);
	    
	    
	    this.rotor.setStepInterval(this.pasInterval);
	 	this.rotor.setStepsPerRevolution(this.pasParRevolution);
	 	this.rotor.setStepSequence(this.single_step_sequence);
	}

	
	

	
	//////// methode mouvement moteur ////////
	public void run() {
		try {
			this.rotor.step(500);
			this.rotor.reverse(100000);
			this.setActif(false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	// getters setters
	public void arreterMoteur(){
		this.rotor.stop();
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


	public GpioStepperMotorComponent getRotor() {
		return rotor;
	}


	public void setRotor(GpioStepperMotorComponent rotor) {
		this.rotor = rotor;
	}


	public byte[] getSingle_step_sequence() {
		return single_step_sequence;
	}


	public void setSingle_step_sequence(byte[] single_step_sequence) {
		this.single_step_sequence = single_step_sequence;
	}


	public boolean isActif() {
		return actif;
	}


	public void setActif(boolean actif) {
		this.actif = actif;
	}
	
	
	

	

	
//fin de la classe	
}
