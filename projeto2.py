# Projeto Unidade 2
# Alunos:
#                Rian Lopes dos Santos Martins
#                Bruno Teonácio dos Santos
#                Natan de Lima Silva
#                Thárcito Pereira de Souza
# Disciplina: Sistemas Digitais
# Prof: Marcelo


from socket import *
import threading
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

# Outras variaveis #
valor_pwm = 0
delay_time = 0.05 # Tempo de delay #
flag_chave = 0 # flag_chave == 1, sistema ligado | flag_chave == 0, sistema desligado #
tempo_segundos = 0 # Tempo em segundos #
valor_adc = 0 # Valor do conversor AD #
curva = "" # Valor da curva

# Configuracoes de conexao #
serverName = 'localhost' # ip do servidor
serverPort = 8015
clientSocket = socket(AF_INET, SOCK_STREAM)
clientSocket.connect((serverName,serverPort))

print('Iniciando o sistema, recebendo curva selecionada...')
curva = clientSocket.recv(1024)
print (curva)

# Contagem do tempo #
def time_count():
        global tempo_segundos
        tempo_segundos = tempo_segundos + delay_time
        time.sleep(delay_time)

# Leitura do ADC #
def ler_adc():
        # global valor_adc = mraa.Aio(pino_sensor)
        global valor_adc
        valor_adc = 10

# Geracao da curva #
def curva1_pwm():

        global valor_pwm
        valor_pwm = 0
        
        if tempo_segundos >= 0 and tempo_segundos < 30: # 0 - 30 segundos #
                #pwm.write(valor_adc)
                valor_pwm = (tempo_segundos*2.56)
                #valor_pwm = valor_pwm*valor_adc # Valor de atualizado pelo sensor

        if tempo_segundos >= 30 and tempo_segundos < 60: # 30 - 60 segundos #
                #pwm.write(valor_adc)
                valor_pwm = 77
                #valor_pwm = valor_pwm*valor_adc # Valor de atualizado pelo sensor

        if tempo_segundos >= 60 and tempo_segundos < 90: # 60 - 90 segundos #
                #pwm.write(valor_adc)
                valor_pwm = (tempo_segundos*3.8) - 151
                #valor_pwm = valor_pwm*valor_adc # Valor de atualizado pelo sensor            

        if tempo_segundos >= 90 and tempo_segundos < 120: # 90 - 120 segundos #
                #pwm.write(valor_adc)
                valor_pwm = 191
                #valor_pwm = valor_pwm*valor_adc # Valor de atualizado pelo sensor      

        if tempo_segundos >= 120 and tempo_segundos < 180: # 120 - 180 segundos #
                #pwm.write(valor_adc)
                valor_pwm = (tempo_segundos*(-3.18)) + 573
                #valor_pwm = valor_pwm*valor_adc # Valor de atualizado pelo sensor    

def curva2_pwm():

        global valor_pwm
        valor_pwm = 0
        
        if tempo_segundos >= 0 and tempo_segundos < 30: # 0 - 30 segundos #
                #pwm.write(valor_adc)
                valor_pwm = (tempo_segundos*6.8)
                #valor_pwm = valor_pwm*valor_adc # Valor de atualizado pelo sensor

        if tempo_segundos >= 30 and tempo_segundos < 150: # 30 - 60 segundos #
                #pwm.write(valor_adc)
                valor_pwm = 205
                #valor_pwm = valor_pwm*valor_adc # Valor de atualizado pelo sensor

        if tempo_segundos >= 150 and tempo_segundos < 180: # 60 - 90 segundos #
                #pwm.write(valor_adc)
                valor_pwm = (tempo_segundos*(-6.8)) + 1230
                #valor_pwm = valor_pwm*valor_adc # Valor de atualizado pelo sensor



# time.clock()

# Início do sistema #

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


''' 
# While Principal (com mraa)
while True:
        time_count()
        if flag_chave == 1:
                ler_adc()
                curva_pwm(mraa_pwm1,curva,clientSocket)
                if tempo_segundos > 180: # 3 minutos
                        tempo_segundos = 0
                        flag_chave = 0

        else:
                chave = key.read()
                if chave == 1:
                        tempo_segundos = 0
                        flag_chave = 1
'''

# While de teste (sem Thread)
# while True:
#         time_count()
#         if tempo_segundos > 180: # 3 minutos
#                 break
#         ler_adc()
#         curva_pwm(0,curva,clientSocket)

# While de teste (com Thread)
#

while True:
        time_count()
        if tempo_segundos > 180: # 3 minutos
                break

        t1 = threading.Thread(target=ler_adc)
        if(curva == b'1\r\n'):
                #print('inserindo valor PWM da curva 1')
                # Inicia as threads
                t2 = threading.Thread(target=curva1_pwm)
                print(valor_pwm)
                clientSocket.send(str(valor_pwm).encode())
                print(tempo_segundos)
                clientSocket.send(str(tempo_segundos).encode())
        else:
                #print('inserindo valor PWM da curva 2')
                # Inicia as threads
                t2 = threading.Thread(target=curva2_pwm)
                print(valor_pwm)
                clientSocket.send(str(valor_pwm).encode())
                print(tempo_segundos)
                clientSocket.send(str(tempo_segundos).encode())


        # Dispara as threads
        t1.start()
        t2.start()


#
                
# Fim do sistema #
clientSocket.close()
