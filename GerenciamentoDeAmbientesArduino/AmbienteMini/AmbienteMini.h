#ifndef AmbienteMini_h
#define AmbienteMini_h

#include "ThreadTemperatura.h"

class AmbienteMini{
	private:
		ThreadTemperatura *_threadTemperatura;
		
		int _portContatorCondicionadorAr;
		int _portFeedbackContatorCondicionadorAr;
	public:
		AmbienteMini(int portSensorTemperatura, int portContatorCondicionadorAr, int portFeedbackContatorCondicionadorAr);
		AmbienteMini(int portSensorTemperatura);
		
		int getTemperatura();
		boolean getEstadoContatorCondicionadorAr();
		
		boolean gerenciarContatorCondicionadorAr(boolean ligar);
		
		boolean deveExecutarThreadTemperatura();
		void executarThreadTemperatura();
};

#endif

