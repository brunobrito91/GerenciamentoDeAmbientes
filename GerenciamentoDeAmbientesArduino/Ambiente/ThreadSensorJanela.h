#ifndef ThreadSensorJanela_h
#define ThreadSensorJanela_h

#include "Thread.h"

class ThreadSensorJanela: public Thread{
	private:
		int _portSensorJanela;
	
	    int lerSensorJanela();
		int lerSensorDigital(int sensor);
		
		const boolean FECHADO = false;
		const boolean ABERTO = true;

		const int ACIONADO = HIGH;
		const int DESLIGADO = LOW;
		const int LIGADO = HIGH;
		
		const int TEMPO_ACIONAMENTO_CONDICIONADOR_AR = 5000;
		
		int led;
	public:
		boolean verificarEstadoJanela();

		ThreadSensorJanela(int portSensorJanela);
		boolean gerenciarSensorJanela();
		void run();
};
#endif