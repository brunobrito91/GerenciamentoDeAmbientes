#ifndef Ambiente_h
#define Ambiente_h

#include "ThreadTemperatura.h"
#include "ThreadSensorJanela.h"
#include "ThreadSensorPresenca.h"

class Ambiente{
	private:
		ThreadTemperatura *_threadTemperatura;
		ThreadSensorJanela *_threadSensorJanela;
		ThreadSensorPresenca *_threadSensorPresenca;
		
		int _portContatorCondicionadorAr;
		int _portFeedbackContatorCondicionadorAr;
	public:
		Ambiente(int portSensorTemperatura, int portSensorJanela, int portSensorPresenca, int portContatorCondicionadorAr, int portFeedbackContatorCondicionadorAr);
		
		int getTemperatura();
		boolean getStatusPresenca();
		boolean getEstadoJanela();
		boolean getEstadoContatorCondicionadorAr();
		
		boolean gerenciarSensorJanela();
		boolean gerenciarSensorPresenca();
		boolean gerenciarContatorCondicionadorAr(boolean force,boolean ligar);
		
		boolean deveExecutarThreadTemperatura();
		boolean deveExecutarThreadSensorJanela();
		boolean deveExecutarThreadSensorPresenca();
		void executarThreadTemperatura();
		void executarThreadSensorJanela();
		void executarThreadSensorPresenca();
};

#endif
