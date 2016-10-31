#ifndef ThreadSensorPresenca_h
#define ThreadSensorPresenca_h

#include "Thread.h"

class ThreadSensorPresenca: public Thread{
	private:
		int _portSensorPresenca;
		
	    int lerSensorPresenca();
		int lerSensorDigital(int sensor);
			
		const int TEMPO_ACIONAMENTO_CONDICIONADOR_AR = 5000;
		
		const boolean INATIVO = false;
		const boolean ATIVO	= true;
		const int DESLIGADO = LOW;
		const int LIGADO = HIGH;
		
		int led;
	public:
		boolean verificarPresenca();
		
		ThreadSensorPresenca(int portSensorPresenca);
		boolean gerenciarSensorPresenca();
		void run();
};
#endif