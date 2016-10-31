#include <Ambiente.h>
#include <SPI.h>
#include <Ethernet2.h>

//MONITORACAO_SIMPLES = 0; MONITORACAO_TOTAL = 1; MONITORACAO_CONTROLE_TOTAL = 2;
const int MONITORACAO = 2;

const int QTD_AMBIENTES = 1;

const int PORT_SENSOR_TEMPERATURA[] = {A0, A1, A2};
const int PORT_SENSOR_JANELA[] = {2, 18, 19};
const int PORT_SENSOR_PRESENCA[] = {3, 20, 21};
const int PORT_CONTATOR_CONDICIONADOR_AR[] = {22, 23, 24};
const int PORT_FEEDBACK_CONTATOR_CONDICIONADOR_AR[] = {47, 48, 49};

Ambiente *ambiente[QTD_AMBIENTES];

byte _mac[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED};
IPAddress _ip(101, 99, 100, 191);
IPAddress _ipServidor(101, 99, 100, 111);
//IPAddress _ip(101, 100, 19, 65);
//IPAddress _ipServidor(101, 100, 19, 60);
unsigned int _porta1 = 9876;
unsigned int _porta2 = 6789;

EthernetClient _cliente;
EthernetServer _servidor = EthernetServer(_porta2);

void setup() {
  pinMode(53, OUTPUT);
  pinMode(4, OUTPUT);
  digitalWrite(4, HIGH);

  Serial.begin(9600);
  for (int i = 0; i < QTD_AMBIENTES; i++ ) {
    ambiente[i] = new Ambiente(PORT_SENSOR_TEMPERATURA[i], PORT_SENSOR_JANELA[i], PORT_SENSOR_PRESENCA[i], PORT_CONTATOR_CONDICIONADOR_AR[i], PORT_FEEDBACK_CONTATOR_CONDICIONADOR_AR[i]);
    attachInterrupt(digitalPinToInterrupt(PORT_SENSOR_JANELA[i]), gerenciarSensoresJanela, CHANGE);
    attachInterrupt(digitalPinToInterrupt(PORT_SENSOR_PRESENCA[i]), gerenciarSensoresPresenca, CHANGE);
  }
  Ethernet.begin(_mac, _ip);
  conectarClienteAoServidorExterno();
  iniciarServidorLocal();
}

void loop() {
  delay(10);
  if (!_cliente.connected()) {
    conectarClienteAoServidorExterno();
  }
  for (int i = 0; i < QTD_AMBIENTES; i++ ) {
    if (ambiente[i]->deveExecutarThreadTemperatura()) {
      ambiente[i]->executarThreadTemperatura();
      Serial.print("Ambiente ");
      Serial.println(i);
      Serial.println("Temperatura: ");
      Serial.println(ambiente[i]->getTemperatura());
      Serial.println();

      Serial.println("Janela: ");
      Serial.println(ambiente[i]->getEstadoJanela());
      Serial.println("Presenca: ");
      Serial.println(ambiente[i]->getStatusPresenca());
      Serial.println("Condicionador Ar: ");
      Serial.println(ambiente[i]->getEstadoContatorCondicionadorAr());
      Serial.println();

      byte pacote[4];

      pacote[0] = 'T'; // tipo
      pacote[1] = 2; // quantidade de bytes a ser lido
      pacote[2] = i; //numero do ambiente;
      pacote[3] = ambiente[i]->getTemperatura(); // temperatura

      enviarPacoteAoServidorExterno(pacote, sizeof(pacote));
    }
    if (ambiente[i]->deveExecutarThreadSensorJanela()) {
      ambiente[i]->executarThreadSensorJanela();

      byte pacote[4];

      pacote[0] = 'C'; // tipo
      pacote[1] = 2; // quantidade de bytes a ser lido
      pacote[2] = i;
      pacote[3] = ambiente[i]->getEstadoContatorCondicionadorAr();

      enviarPacoteAoServidorExterno(pacote, sizeof(pacote));

      Serial.println("Janela: ");
      Serial.println(ambiente[i]->getEstadoJanela());
      Serial.println("Presenca: ");
      Serial.println(ambiente[i]->getStatusPresenca());
      Serial.println("Condicionador Ar: ");
      Serial.println(ambiente[i]->getEstadoContatorCondicionadorAr());
      Serial.println();
    }
    if (ambiente[i]->deveExecutarThreadSensorPresenca()) {
      ambiente[i]->executarThreadSensorPresenca();

      byte pacote[4];

      pacote[0] = 'C'; // tipo
      pacote[1] = 2; // quantidade de bytes a ser lido
      pacote[2] = i;
      pacote[3] = ambiente[i]->getEstadoContatorCondicionadorAr();

      enviarPacoteAoServidorExterno(pacote, sizeof(pacote));

      Serial.println("Janela: ");
      Serial.println(ambiente[i]->getEstadoJanela());
      Serial.println("Presenca: ");
      Serial.println(ambiente[i]->getStatusPresenca());
      Serial.println("Condicionador Ar: ");
      Serial.println(ambiente[i]->getEstadoContatorCondicionadorAr());
      Serial.println();
    }
  }
  receberComandoExterno();
}

void gerenciarSensoresJanela() {
  volatile int i = 0;
  volatile int j = 2;
  volatile boolean statusSensorJanela;
  volatile int qtdBytes = QTD_AMBIENTES * 2;
  volatile byte pacote[qtdBytes + 2];

  pacote[0] = 'J'; // tipo
  pacote[1] = qtdBytes; // quantidade de bytes a ser lido
  for (i = 0; i < QTD_AMBIENTES; i++) {
    statusSensorJanela = ambiente[i]->gerenciarSensorJanela();
    pacote[j] = i;
    pacote[++j] = statusSensorJanela;
    j++;
  }
  enviarPacoteAoServidorExterno(pacote, sizeof(pacote));
}

void gerenciarSensoresPresenca() {
  volatile int i = 0;
  volatile int j = 2;
  volatile boolean statusSensorPresenca;
  volatile int qtdBytes = QTD_AMBIENTES * 2;
  volatile byte pacote[qtdBytes + 2];

  pacote[0] = 'P'; // tipo
  pacote[1] = qtdBytes; // quantidade de bytes a ser lido
  for (i = 0; i < QTD_AMBIENTES; i++) {
    statusSensorPresenca = ambiente[i]->gerenciarSensorPresenca();
    pacote[j] = i;
    pacote[++j] = statusSensorPresenca;
    j++;
  }
  enviarPacoteAoServidorExterno(pacote, sizeof(pacote));
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
  pacote[3] = MONITORACAO;//tipoDispositivo

  enviarPacoteAoServidorExterno(pacote, sizeof(pacote));

  gerenciarSensoresJanela();
  gerenciarSensoresPresenca();
}

void iniciarServidorLocal() {
  _servidor.begin();
  Serial.println("Servidor iniciado!");
}

void enviarPacoteAoServidorExterno(volatile byte pacoteEnvio[], int tamanho) {
  /*/////////////////////////////////////////
    Serial.println();
    Serial.println("Enviando pacote...");
    Serial.print("Tamanho Pacote: ");
    Serial.println(tamanho);
    //////////////////////////////////////////*/
    byte pacote[tamanho];
    int i;
    for (i = 0; i < tamanho; i++) {
      pacote[i] = pacoteEnvio[i];
    }
    
  _cliente.write(pacote, tamanho);
  /*/////////////////////////////////////////
    Serial.print("Mensagem: ");
    for (int i = 0; i < tamanho; i++) {
    Serial.print(pacoteEnvio[i]);
    Serial.print(" ");
    }
    Serial.println();
    /////////////////////////////////////////*/
}

int receberPacoteDoServidor() {
  int tamanhoPacote = _cliente.available();
  if (tamanhoPacote > 0) {
    /*/////////////////////////////////////
      Serial.println();
      Serial.println("Recebendo pacote...");
      Serial.print("Tamanho Pacote: ");
      Serial.println(tamanhoPacote);
      /////////////////////////////////////*/
    for (int i = 0; i < tamanhoPacote; i++) {
      //      _pacoteBuffer[i] = _cliente.read();
    }
    /*/////////////////////////////////////
      Serial.print("Mensagem: ");
      for (int i = 0; i < tamanhoPacote; i++) {
      Serial.print(_pacoteBuffer[i]);
      Serial.print(" ");
      }
      Serial.println();
      /////////////////////////////////////*/
    return tamanhoPacote;
  }
  else {
    //Serial.println("Nada disponivel");
    //Serial.println();
    return 0;
  }
}

void receberComandoExterno() {
  EthernetClient cliente = _servidor.available();
  if (cliente.available()) {
    Serial.println("Cliente Conectado");
    int tamanhoPacote = cliente.available();
    if (tamanhoPacote > 0) {
      /////////////////////////////////////
      Serial.println();
      Serial.println("Recebendo pacote...");
      Serial.print("Tamanho Pacote: ");
      Serial.println(tamanhoPacote);
      /////////////////////////////////////*/
      byte pacoteBuffer[tamanhoPacote];
      for (int i = 0; i < tamanhoPacote; i++) {
        pacoteBuffer[i] = cliente.read();
      }
      /////////////////////////////////////
      Serial.print("Mensagem: ");
      for (int i = 0; i < tamanhoPacote; i++) {
        Serial.print(pacoteBuffer[i]);
        Serial.print(" ");
      }
      Serial.println();
      /////////////////////////////////////*/
      interpretarComandoExterno(pacoteBuffer);
    }
  }
}

void interpretarComandoExterno(byte pacote[]) {
  switch (pacote[0]) {
    case 'C': {
        ambiente[pacote[2]]->gerenciarContatorCondicionadorAr(true, pacote[3] == 1);

        byte pacoteEnvio[4];

        pacoteEnvio[0] = 'C'; // tipo
        pacoteEnvio[1] = 2; // quantidade de bytes a ser lido
        pacoteEnvio[2] = pacote[2];
        pacoteEnvio[3] = ambiente[pacote[2]]->getEstadoContatorCondicionadorAr();

        enviarPacoteAoServidorExterno(pacoteEnvio, sizeof(pacoteEnvio));
      }
  }
}

