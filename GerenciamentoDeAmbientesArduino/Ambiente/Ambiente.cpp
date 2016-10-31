#include "Arduino.h"
#include "Ambiente.h"
#include "ThreadTemperatura.h"
#include "ThreadSensorJanela.h"
#include "ThreadSensorPresenca.h"

Ambiente::Ambiente(int portSensorTemperatura, int portSensorJanela, int portSensorPresenca, int portContatorCondicionadorAr, int portFeedbackContatorCondicionadorAr){ 
	_threadTemperatura  = new ThreadTemperatura(portSensorTemperatura);
	_threadSensorJanela  = new ThreadSensorJanela(portSensorJanela);
	_threadSensorPresenca  = new ThreadSensorPresenca(portSensorPresenca);

	_portContatorCondicionadorAr = portContatorCondicionadorAr;
	_portFeedbackContatorCondicionadorAr = portFeedbackContatorCondicionadorAr;

	pinMode(_portContatorCondicionadorAr, OUTPUT);
	pinMode(_portFeedbackContatorCondicionadorAr, INPUT);

	digitalWrite(_portContatorCondicionadorAr, getEstadoContatorCondicionadorAr());
}

int Ambiente::getTemperatura(){
	return _threadTemperatura->_temperatura;
}

boolean Ambiente::getStatusPresenca(){
	return _threadSensorPresenca->verificarPresenca();
}

boolean Ambiente::getEstadoJanela(){
	return _threadSensorJanela->verificarEstadoJanela();
}

boolean Ambiente::getEstadoContatorCondicionadorAr(){
	return digitalRead(_portFeedbackContatorCondicionadorAr);
}

boolean Ambiente::gerenciarSensorJanela(){
	return _threadSensorJanela->gerenciarSensorJanela();
}

boolean Ambiente::gerenciarSensorPresenca(){
	return _threadSensorPresenca->gerenciarSensorPresenca();
}

boolean Ambiente::gerenciarContatorCondicionadorAr(boolean force, boolean ligar){
	if(force){
		digitalWrite(_portContatorCondicionadorAr, ligar);
	}else{
		boolean FECHADO = false;
		if((getStatusPresenca() == true) && (getEstadoJanela() == FECHADO)){
			digitalWrite(_portContatorCondicionadorAr, HIGH);
		}else{
			digitalWrite(_portContatorCondicionadorAr, LOW);
		}
	}
	return getEstadoContatorCondicionadorAr();
}

boolean Ambiente::deveExecutarThreadTemperatura(){
	boolean resposta = _threadTemperatura->shouldRun(); 
	return resposta;
}

void Ambiente::executarThreadTemperatura(){
	Serial.println("ExecutarThreadTemperatura");
	_threadTemperatura->run();
}

boolean Ambiente::deveExecutarThreadSensorJanela(){
	return _threadSensorJanela->shouldRun();		
}

void Ambiente::executarThreadSensorJanela(){
	gerenciarContatorCondicionadorAr(false,false);
	_threadSensorJanela->run();
}

boolean Ambiente::deveExecutarThreadSensorPresenca(){
	boolean ABERTO = true;
	if(getEstadoJanela() == ABERTO){
		return false;
	}else{
		return _threadSensorPresenca->shouldRun();	
	}
}

void Ambiente::executarThreadSensorPresenca(){
	gerenciarContatorCondicionadorAr(false,false);
	_threadSensorPresenca->run();
}