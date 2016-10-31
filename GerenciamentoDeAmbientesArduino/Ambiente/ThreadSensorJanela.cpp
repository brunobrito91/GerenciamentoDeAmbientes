#include "Arduino.h"
#include "ThreadSensorJanela.h"

ThreadSensorJanela::ThreadSensorJanela(int portSensorJanela):Thread(){
	_portSensorJanela = portSensorJanela;	
	led = _portSensorJanela+ 25;

	pinMode(_portSensorJanela, INPUT);
	pinMode(led, OUTPUT);
	
	digitalWrite(led, verificarEstadoJanela());	
	
	Thread::setInterval(TEMPO_ACIONAMENTO_CONDICIONADOR_AR);
	Thread::enabled = false;
}

boolean ThreadSensorJanela::gerenciarSensorJanela() {
	boolean estadoJanela = verificarEstadoJanela();
	Thread::enabled = true;
	Thread::runned();
	if(estadoJanela == ABERTO){
		digitalWrite(led, LIGADO);
	}else {
		digitalWrite(led, DESLIGADO);
	}
	return estadoJanela;
}

boolean ThreadSensorJanela::verificarEstadoJanela() {
	int valorSensorJanela = lerSensorJanela();
	if (valorSensorJanela == ACIONADO) {
		return ABERTO;
	} else {
		return FECHADO;
	}
}

int ThreadSensorJanela::lerSensorJanela() {
	return lerSensorDigital(_portSensorJanela);
}

int ThreadSensorJanela::lerSensorDigital(int sensor) {
	return digitalRead(sensor);
}

void ThreadSensorJanela::run(){
	Thread::enabled = false;
	Thread::run();
}