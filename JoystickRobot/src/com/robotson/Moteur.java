package com.robotson;

import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;


public class Moteur extends Thread{

	private int pasInterval = 2;
	private int pasParRevolution = 200;
	private GpioStepperMotorComponent rotor;
	private int pas=0;
	private boolean buteeHG=false;
	private boolean buteeHD=false;
	private boolean buteeVH=false;
	private boolean buteeVB=false;
	private byte[] single_step_sequence = { (byte) 0b0001, (byte) 0b0010, (byte) 0b0100, (byte) 0b1000 };
	private char direction='N';

	
	private MoteurControle moteurControleThread = new MoteurControle();
	
	
	// constructeur par defaut
	public Moteur() {

	}
	

	// constructeur par parametres
	public Moteur(GpioController gpio, Pin bobinePinGpioNord,
			Pin bobinePinGpioOuest, Pin bobinePinGpioSud, Pin bobinePinGpioEst) {


		// instance des bobines
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

		
		// stop le moteur lorsque le programme est termine
		gpio.setShutdownOptions(true, PinState.LOW, pins);
		
		// creer un composant moteur
                this.rotor = new GpioStepperMotorComponent(pins);
	    
	    
                // parametrage du moteur
                this.rotor.setStepInterval(this.pasInterval);
	 	this.rotor.setStepsPerRevolution(this.pasParRevolution);
	 	this.rotor.setStepSequence(this.single_step_sequence);
	}

	
	

	
	//////// methode mouvement moteur par direction ////////
	public void directionGauche() {
		this.direction='G';
		// deplacer a gauche sauf si on est en butee
		if(!this.buteeHG){
			this.buteeHD=false;
			if(!moteurControleThread.isAlive()) {
				moteurControleThread = new MoteurControle();
				moteurControleThread.start();
            }
		}
	}
	
	
	public void directionDroite() {
		this.direction='D';
		// deplacer a droite sauf si on est en butee
		if(!this.buteeHD){
			this.buteeHG=false;
			if(!moteurControleThread.isAlive()) {
				moteurControleThread = new MoteurControle();
				moteurControleThread.start();
            }
		}
	}
	

	public void directionHaut() {
		this.direction='H';
		// deplacer a gauche sauf si on est en butee
		if(!this.buteeVH){
			this.buteeVB=false;
			if(!moteurControleThread.isAlive()) {
				moteurControleThread = new MoteurControle();
				moteurControleThread.start();
            }
		}
	}
	public void directionBas() {
		this.direction='B';
		// deplacer a droite sauf si on est en butee
		if(!this.buteeVB){
			this.buteeVH=false;
			if(!moteurControleThread.isAlive()) {
				moteurControleThread = new MoteurControle();
				moteurControleThread.start();
            }
		}
	}
	
	
	////////methode mouvement moteur par preference ////////
	public void preference(int pas) {
		this.pas=pas;
		this.direction='N';
		// deplacer a droite sauf si on est en butee
		if(!this.buteeHD){
			this.buteeHG=false;
			if(!moteurControleThread.isAlive()) {
				moteurControleThread = new MoteurControle();
				moteurControleThread.start();
            }
		}
	}
	
	
	// arreter le moteur
	public void arreterMoteur(){
		//System.out.println("STEP ARRETE : ");
		this.rotor.stop();
		this.moteurControleThread.stop();
	}
	
	
	
	// thread moteur
	public class MoteurControle extends Thread {
		
		
		
		// lancement du thread
        public void run() {

        	
        	// mouvement du moteur par une preference
        	if(direction=='N'){
    			rotor.step(pas);
    		}
        	
        	else {
        		
        		// mouvement du moteur par direction
	        	while(true) {
	        		
	        		if(direction=='G'){
	        			rotor.step(-10);
	        			pas-=10;
	        		}
	        		else if(direction=='D'){
	        			rotor.step(10);
	        			pas+=10;
	        		}else if(direction=='H'){
	        			rotor.step(+200);
	        			pas+=200;
	        		}else if(direction=='B'){
	        			rotor.step(-200);
	        			pas-=200;
	        		}
	        		
	        		System.out.println("PAS : "+pas);
	           		
	        		try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        	
        	}
        }
        
    //fin de la classe Thread
    }

	
	

	//getters et setters
	
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




	
// fin de la classe	
}
