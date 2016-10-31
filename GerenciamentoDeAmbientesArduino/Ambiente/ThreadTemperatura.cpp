#include "Arduino.h"
#include "ThreadTemperatura.h"

ThreadTemperatura::ThreadTemperatura(int portSensorTemperatura):Thread(){
	_portSensorTemperatura = portSensorTemperatura;
	gerenciarTemperatura();
	Thread::setInterval(TEMPO_VERIFICACAO_TEMPERATURA);
}

void ThreadTemperatura::gerenciarTemperatura() {
	_temperatura = medirTemperatura();
}

int ThreadTemperatura::medirTemperatura() {
	int valorSensorTemperatura = lerSensorTemperatura();
	return (valorSensorTemperatura * (5.0 / 1023.0) / TENSAO_POR_GRAU_CELSIUS);
}

int ThreadTemperatura::lerSensorTemperatura() {
	return lerSensorAnalogico(_portSensorTemperatura);
}

int ThreadTemperatura::lerSensorAnalogico(int sensor) {
	return analogRead(sensor);
}

void ThreadTemperatura::run(){
	gerenciarTemperatura();
	Thread::run();
}