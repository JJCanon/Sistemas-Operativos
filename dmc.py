#Librerías
from pydub import AudioSegment as AS #Manejos de Audio
import argparse as arg #Manejo de parametro desde terminal
import os # Manejo de Archivos
import threading  #manejo de Hilos
import multiprocessing as MP #manejo de procesadores
from multiprocessing import Pool
import time
# Variables Globales

#Funciones

#Funcion para ver los archivos de una carpeta
def archivosCarpeta(nameFolder):
    
    # Listar todos los archivos en el directorio (la carpeta debe estar en la misma ruta del programa)
    archivos = os.listdir(nameFolder)

    return archivos

# Funcion que transforma los archivos en el formato especificado
def transformarAudio(nameFile,extensionFile=None):
    # Lista de Hilos
    threads=[]
    #Lista de Formatos
    extensiones=["mp3","ogg","wav"]
    for extension in extensiones:
        if extension!=extensionFile:
            threadFile=threading.Thread(target=changeFormatFile,args=(nameFile,extension))
            threads.append(threadFile)
            threadFile.start()
    # Esperar a que todos los hilos terminen
    for thread in threads:
        thread.join()
    
    


# Funcion transformar con hilos
def changeFormatFile(nameFile,extension):
    # separar nombre y formato del archivo de entrada
    #print(nameFile)
    name_extension=nameFile.split('.')
    #lista de extensiones
    if (name_extension[1]=="aif"):
        name_extension[1]="aiff"
    audio=AS.from_file(nameFile,format=name_extension[1])
    # exportar archivo en formato especificado
    audio.export((name_extension[0]+"1."+extension),format=extension)
    """imprimir tamaño del archivo"""


# Funcion para crear argumetos
def agruparArgumentos(nameFolder,nameFiles,extension=None):
    argumentos=[]
    for nameFile in nameFiles:
         argumento=(nameFolder+"/"+nameFile,extension)
         argumentos.append(argumento)
    return argumentos
     
     
def changeFormatFolder(archivos,extension,nameFolder):
    processesnum=MP.cpu_count()
    argumentos=agruparArgumentos(nameFolder,archivos,extension)
    # Crear grupo de procesos
    with Pool(processes=processesnum) as pool:
        # Ejecutar la funsion en paralelo para una lista de números
        resultados=pool.starmap(changeFormatFile,argumentos)
    
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
        changeFormatFolder(archivos,extension,nameFile)
        #funcion tranformar archivos de la carpeta en extensión
        print(0)
    #se especifica la extension y no es carpeta, se debe pasar el archivo al formato especificado
    elif(extension is not None and not esCarpeta):
        #transformar audio a la extension especifica
        #transformarAudio(nameFile,extension)
        print(0)
    # No se especifica la extension y no es una carpeta,
    # se debe transformar el archivo en los formatos posibles  
    elif(extension is None and not esCarpeta):
        #pasar archivo a los tres tipos de archivos mp3,ogg,wav
        #transformarAudio(nameFile)
        print(0)
    # No se especifica la extension y el archivo es una carpeta
    # se debe retornar error
    elif(extension is None and esCarpeta):
        #es una carpeta pero no se especifica la extensión, marcar error
        print(1)
    final=time.time()
    
    print(final-inicio," segundos")
    