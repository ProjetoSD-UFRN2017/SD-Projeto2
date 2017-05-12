# Projeto Unidade 2
# Alunos:
#                Rian Lopes dos Santos Martins
#		 Bruno Teonácio dos Santos
#		 Natan de Lima Silva
#		 Thárcito Pereira de Souza
# Disciplina: Sistemas Digitais
# Prof: Marcelo


from socket import *
from threading import Thread
import time
#import mraa

# Variaveis de Entrada #
pino_chave = 0 # Pino da chave para ligar o sistema #
pino_sensor = 1 # Pino analogico de leitura do sensor de luminosidade/temperatura #

# Variaveis de saida #
pino_led1 = 2 # Pino do led 1 #
pino_led2 = 3 # Pino do led 2 #
pino_led3 = 4 # Pino do led 3 #
pino_pwm = 5 # Pino do pwm #

# Outras variáveis #
delay_time = 0.05 # Tempo de delay #
flag_chave = 0 # flag_chave == 1, sistema ligado | flag_chave == 0, sistema desligado #
tempo_segundos = 0 # Tempo em segundos #
valor_adc = 0 # Valor do conversor AD #

# Contagem do tempo #
def time_count():
        global tempo_segundos = tempo_segundos + delay_time
        time.sleep(delay_time)

# Leitura do ADC #
def ler_adc():
        # global valor_adc = mraa.Aio(pino_sensor)
        global valor_adc = 10

# Geração da curva #
def curva_pwm(pwm,curva_selec,socket_client):

	valor_pwm = 0
        
        if tempo_segundos >= 0 && tempo_segundos < 30: # 0 - 30 segundos #
                #pwm.write(valor_adc)
		valor_pwm = valor_adc*1 # Valor de teste

        if tempo_segundos >= 30 && tempo_segundos < 60: # 30 - 60 segundos #
                #pwm.write(valor_adc)
		valor_pwm = valor_adc*2 # Valor de teste	

        if tempo_segundos >= 60 && tempo_segundos < 90: # 60 - 90 segundos #
                #pwm.write(valor_adc)
		valor_pwm = valor_adc*3 # Valor de teste		

        if tempo_segundos >= 90 && tempo_segundos < 120: # 90 - 120 segundos #
                #pwm.write(valor_adc)
		valor_pwm = valor_adc*4 # Valor de teste	

        if tempo_segundos >= 120 && tempo_segundos < 180: # 120 - 180 segundos #
                #pwm.write(valor_adc)
		valor_pwm = valor_adc*5 # Valor de teste	

	if(curva_selec == b'1\r\n'):
		print('inserindo valor PWM da curva 1')
		socket_client.sendall("0".encode())
	else
		print('inserindo valor PWM da curva 2')
	
	socket_client.sendall(str(valor_pwm).encode())

# Configuracoes de conexao #
serverName = 'localhost' # ip do servidor
serverPort = 8015
clientSocket = socket(AF_INET, SOCK_STREAM)
clientSocket.connect((serverName,serverPort))

# time.clock()

# Início do sistema #

        # https://github.com/intel-iot-devkit/mraa/blob/master/examples/python/blink-io8.py #
        #led1 = mraa.Gpio(pino_led1)
        #led2 = mraa.Gpio(pino_led2)
        #led3 = mraa.Gpio(pino_led3)
        #key = mraa.Gpio(pino_chave)

        #led1.dir(mraa.DIR_OUT)
        #led2.dir(mraa.DIR_OUT)
        #led3.dir(mraa.DIR_OUT)

        #mraa_pwm1 = mraa.Pwm(pino_pwm)
        #mraa_pwm1.period_us(64)
        #mraa_pwm1.enable(True)

	print('Iniciando o sistema, recebendo curva selecionada...')
	curva = clientSocket.recv(1024)
	print (curva)

        ''' 
	# While Principal (com mraa)
        while True
                time_count()
                if flag_chave == 1
                        ler_adc()
                        curva_pwm(mraa_pwm1,curva,clientSocket)
                        if tempo_segundos > 180 # 3 minutos
                                tempo_segundos = 0
                                flag_chave = 0
                                
                else
                        chave = key.read()
                        if chave == 1
                                tempo_segundos = 0
                                flag_chave = 1
        '''

	# While de teste (sem Thread)
	while True
		time_count()
		if tempo_segundos > 180 # 3 minutos
			break
		ler_adc()
		curva_pwm(0,curva,clientSocket)

	# While de teste (com Thread)
	#
	while True
		time_count()
		if tempo_segundos > 180 # 3 minutos
			break

		# Inicia as threads
		t1 = threading.Thread(target=ler_adc)
		t2 = threading.Thread(target=curva_pwm,args(0,curva,clientSocket,)) # Note o extra ',' (obrigatório)

		# Dispara as threads
		t1.start()
		t2.start()
		
	#
		
# Fim do sistema #
clientSocket.close()
