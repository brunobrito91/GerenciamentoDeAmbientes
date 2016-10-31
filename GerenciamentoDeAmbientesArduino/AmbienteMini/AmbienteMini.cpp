#include "Arduino.h"
#include "AmbienteMini.h"
#include "ThreadTemperatura.h"

AmbienteMini::AmbienteMini(int portSensorTemperatura, int portContatorCondicionadorAr, int portFeedbackContatorCondicionadorAr){ 
	_threadTemperatura  = new ThreadTemperatura(portSensorTemperatura);
	
	_portContatorCondicionadorAr = portContatorCondicionadorAr;
	_portFeedbackContatorCondicionadorAr = portFeedbackContatorCondicionadorAr;

	pinMode(_portContatorCondicionadorAr, OUTPUT);
	pinMode(_portFeedbackContatorCondicionadorAr, INPUT);

	digitalWrite(_portContatorCondicionadorAr, getEstadoContatorCondicionadorAr());
}

AmbienteMini::AmbienteMini(int portSensorTemperatura){ 
	_threadTemperatura  = new ThreadTemperatura(portSensorTemperatura);
}

int AmbienteMini::getTemperatura(){
	return _threadTemperatura->_temperatura;
}

boolean AmbienteMini::getEstadoContatorCondicionadorAr(){
	return digitalRead(_portFeedbackContatorCondicionadorAr);
}

boolean AmbienteMini::gerenciarContatorCondicionadorAr(boolean ligar){
	digitalWrite(_portContatorCondicionadorAr, ligar);
	return getEstadoContatorCondicionadorAr();
}

boolean AmbienteMini::deveExecutarThreadTemperatura(){
	return _threadTemperatura->shouldRun();
}

void AmbienteMini::executarThreadTemperatura(){
	_threadTemperatura->run();
}