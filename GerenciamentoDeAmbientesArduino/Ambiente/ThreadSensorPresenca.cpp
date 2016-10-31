#include "Arduino.h"
#include "ThreadSensorPresenca.h"

ThreadSensorPresenca::ThreadSensorPresenca(int portSensorPresenca):Thread(){
	_portSensorPresenca = portSensorPresenca;
	
	led = _portSensorPresenca+25;

	pinMode(_portSensorPresenca, INPUT);	
	pinMode(led, OUTPUT);
	
	digitalWrite(led, verificarPresenca());	
	
	Thread::setInterval(TEMPO_ACIONAMENTO_CONDICIONADOR_AR);
	Thread::enabled = false;
}

boolean ThreadSensorPresenca::gerenciarSensorPresenca(){
	boolean statusPresenca = verificarPresenca();
	Thread::enabled = true;
	if (statusPresenca){
		Thread::setInterval(0);			
		digitalWrite(led, LIGADO);
	}else{
		Thread::setInterval(TEMPO_ACIONAMENTO_CONDICIONADOR_AR);
		digitalWrite(led, DESLIGADO);
	}
	Thread::runned();
	return statusPresenca;
}

boolean ThreadSensorPresenca::verificarPresenca() {
	return lerSensorPresenca();
}

int ThreadSensorPresenca::lerSensorPresenca() {
	return lerSensorDigital(_portSensorPresenca);
}

int ThreadSensorPresenca::lerSensorDigital(int sensor) {
	return digitalRead(sensor);
}

void ThreadSensorPresenca::run(){
	Thread::enabled = false;
	Thread::run();
}