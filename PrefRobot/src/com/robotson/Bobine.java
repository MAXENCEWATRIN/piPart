package com.robotson;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

public class Bobine {

	private GpioPinDigitalOutput pinBobine;

	// constructeur par defaut
	public Bobine() {

	}

	// constructeur par parametres
	public Bobine(GpioController gpio, Pin pinGpio) {

		this.pinBobine = gpio.provisionDigitalOutputPin(pinGpio, PinState.LOW);
	}

	
	
	// getters setters
	public GpioPinDigitalOutput getPinBobine() {
		return pinBobine;
	}

	public void setPinBobine(GpioPinDigitalOutput pinBobine) {
		this.pinBobine = pinBobine;
	}

	// fin de la classe
}
