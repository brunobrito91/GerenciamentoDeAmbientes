#include <AmbienteMini.h>
#include <Ethernet.h>

//MONITORACAO_SIMPLES = 0; MONITORACAO_TOTAL = 1; MONITORACAO_CONTROLE_TOTAL = 2;
const int MONITORACAO_SIMPLES = 0;

const int QTD_AMBIENTES = 6;

const int PORT_SENSOR_TEMPERATURA[] = {A0, A1, A2, A3, A4, A5};

AmbienteMini *ambiente[QTD_AMBIENTES];

byte _mac[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED};
IPAddress _ip(101, 99, 100, 190);
IPAddress _ipServidor(101, 99, 100, 111);
//IPAddress _ip(101, 100, 19, 68);
//IPAddress _ipServidor(101, 100, 19, 60);

unsigned int _porta1 = 9876;

EthernetClient _cliente;

byte temperatura[QTD_AMBIENTES] = {0, 0, 0, 0, 0, 0};

void setup() {
  Serial.begin(9600);
  Ethernet.begin(_mac, _ip);
  conectarClienteAoServidorExterno();
  for (int i = 0; i < QTD_AMBIENTES; i++ ) {
    ambiente[i] = new AmbienteMini(PORT_SENSOR_TEMPERATURA[i]);
  }
}

void loop() {
  delay(10);
  if (!_cliente.connected()) {
    conectarClienteAoServidorExterno();
  }
  for (int i = 0; i < QTD_AMBIENTES; i++ ) {
    if (ambiente[i]->deveExecutarThreadTemperatura()) {
      ambiente[i]->executarThreadTemperatura();

      int temp = ambiente[i]->getTemperatura();
      Serial.print("Ambiente ");
      Serial.println(i);
      Serial.println("Temperatura: ");
      Serial.println(temp);
      Serial.println();

      int qtdBytes = 2;
      byte pacote[qtdBytes + 2];

      //        if (temp != temperatura[i]) {
      temperatura[i] = temp;
      pacote[0] = 'T'; // tipo
      pacote[1] = qtdBytes; // quantidade de bytes a ser lido
      pacote[2] = i; //numero do ambiente;
      pacote[3] = temp; // temperatura

      enviarPacoteAoServidorExterno(pacote, sizeof(pacote));
      //        }
    }
  }

  //  delay(3000);
  //
  //  for (int i=0; i<2; i++) {
  //    int valorSensorTemperatura = analogRead(PORT_SENSOR_TEMPERATURA[i]);
  //    float temperatura =  (valorSensorTemperatura * (5.0 / 1023.0) / 0.01);
  //    Serial.println("Valor Sensor: ");
  //    Serial.println(valorSensorTemperatura);
  //    Serial.println("Temperatura: ");
  //    Serial.println(temperatura);
  //  }
}

void conectarClienteAoServidorExterno() {
  _cliente.stop();
  Serial.println("Desconectou do Servidor!");

  if (_cliente.connect(_ipServidor, _porta1)) {
    Serial.println("Conectado ao Servidor!");
    Serial.println();
    enviarComandoInicializacao();
  }
}

void enviarComandoInicializacao() {
  byte pacote[4];
  pacote[0] = 'I';
  pacote[1] = 2;
  pacote[2] = QTD_AMBIENTES;
  pacote[3] = MONITORACAO_SIMPLES;//tipoDispositivo

  enviarPacoteAoServidorExterno(pacote, sizeof(pacote));
}

void enviarPacoteAoServidorExterno(byte pacoteEnvio[], int tamanho) {
  /*/////////////////////////////////////////
    Serial.println();
    Serial.println("Enviando pacote...");
    Serial.print("Tamanho Pacote: ");
    Serial.println(tamanho);
    //////////////////////////////////////////*/
  _cliente.write(pacoteEnvio, tamanho);
  /*/////////////////////////////////////////
    Serial.print("Mensagem: ");
    for (int i = 0; i < tamanho; i++) {
    Serial.print(pacoteEnvio[i]);
    Serial.print(" ");
    }
    Serial.println();
    /////////////////////////////////////////*/
}
