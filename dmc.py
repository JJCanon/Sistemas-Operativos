#Librerías
from pydub import AudioSegment as AS #Manejos de Audio
import argparse as arg #Manejo de parametro desde terminal
import os # Manejo de Archivos
import threading # Manejo de Hilos
import time # manejo de tiempo

# Variables Globales

#Funciones

#Funcion para ver los archivos de una carpeta
def archivosCarpeta(nameFolder):
    
    # Listar todos los archivos en el directorio (la carpeta debe estar en la misma ruta del programa)
    archivos = os.listdir(nameFolder)

    return archivos

# Funcion que transforma los archivos en el formato especificado
def transformarAudio(nameFile,extension=None):
    # separar nombre y formato del archivo de entrada
    name_extension=nameFile.split('.')
    #la libreria solo reconoce los archivos aif como aiff, por tanto se debe cambiar el nombre del la extension
    if (name_extension[1]=="aif"):
        name_extension[1]="aiff"
    # Cargar archivo
    audio=AS.from_file(nameFile,format=name_extension[1])
    if(extension!=None):
        # exportar archivo en formato especificado
        audio.export((name_extension[0]+"1."+extension),format=extension)
        tamaño=os.path.getsize(name_extension[0]+"1."+extension)
        print("el archivo ",name_extension[0]+"1."+extension," tiene un tamaño de: ",tamaño/(1024*1024)," MB")
    else:
        threadsForOneFile(nameFile,name_extension[1])
    
# Funcion para procesar multiples conversiones en hilos separados
def thread(files,extension,fileName):
    inicio=time.time()
    # Lista de Hilos
    threads=[]
    for file in files:
        thread=threading.Thread(target=transformarAudio,args=(fileName+"/"+file,extension))
        threads.append(thread)
        thread.start()  
    # Esperar a que todos los hilos terminen    
    for thread in threads:
        thread.join()
    final=time.time()    
    print("el tiempo de procesar en paralelismo es",final-inicio," segundos")  

# Funcion para procesar un mismo archivo en varios formatos (mp3,ogg,wav)
def threadsForOneFile(file,extensionFile=None):
    
    # Lista de Hilos
    threads=[]
    #Lista de Formatos
    extensiones=["mp3","ogg","wav"]
    for extension in extensiones:
            threadFile=threading.Thread(target=transformarAudio,args=(file,extension))
            threads.append(threadFile)
            threadFile.start()
    # Esperar a que todos los hilos terminen
    for thread in threads:
        thread.join()     
    if extensionFile!=None:
        name_extension=file.split(".")
        for extension in extensiones:
            if extension!=extensionFile:
                os.remove(name_extension[0]+"1."+extension)
    
    
      
    
        
#Main
if __name__=="__main__":
    inicio=time.time()
    #crear argumenparser para los parametros
    parser=arg.ArgumentParser(description="argumentos para la transformación")
    
    # Agregar argumentos
    parser.add_argument('-e',type=str,nargs='?',default=None,
                        help='entrada del -e que contiene el tipo de archivo al que se va a transformar el archivo')
    parser.add_argument('-f',type=str,help='entrada del nombre del archivo a transformar')
    
    #Parsear parametros
    args=parser.parse_args()
    
    #Guardar argumentos en variables
    extension=args.e
    nameFile=args.f
    
    #verificar si el archivo es o no es una carpeta
    esCarpeta=False
    try:
        posicion=nameFile.index('.')
        esCarpeta=False
    except ValueError:
        esCarpeta=True
     
    ##condicional
    # Se especifica la extension y el archivo es una carpeta, 
    # se debe pasar los archivos de la carpeta a al formato especificado       
    if extension is not None and  esCarpeta : 
        #buscar archivos de la carpeta
        archivos=archivosCarpeta(nameFile)
        #funcion tranformar archivos de la carpeta en extensión   
        thread(archivos,extension,nameFile)
        print(0)
    # la extension puede o no ser especificada pero si o si no es carpeta, se debe pasar el archivo al formato especificado
    elif(not esCarpeta):
        #transformar audio a la extension especifica
        inicio=time.time()
        threadsForOneFile(nameFile,extension)
        final=time.time()
        print("el tiempo de paralelismo para un solo archivo es de: ",final-inicio," segundos")
        print(0)
    # No se especifica la extension y no es una carpeta,
    # No se especifica la extension y el archivo es una carpeta
    # se debe retornar error
    elif(extension is None and esCarpeta):
        #es una carpeta pero no se especifica la extensión, marcar error
        print(1)
    final=time.time()
    print("el tiempo total de ejecucion fueron ",final-inicio," segundos")
    