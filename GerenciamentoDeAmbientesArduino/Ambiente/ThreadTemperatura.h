#ifndef ThreadTemperatura_h
#define ThreadTemperatura_h

#include "Thread.h"

class ThreadTemperatura: public Thread{
	private:
		int _portSensorTemperatura;
		
		int medirTemperatura();
		int lerSensorTemperatura();
		int lerSensorAnalogico(int sensor);
		
		const float TENSAO_POR_GRAU_CELSIUS = 0.01;		
		const long TEMPO_VERIFICACAO_TEMPERATURA = 60000;
	public:
		int _temperatura;
		
		ThreadTemperatura(int portSensorTemperatura);
		void gerenciarTemperatura();
		void run();
};
#endif